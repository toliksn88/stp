package com.innowise.secret_santa.service.player_services;

import com.innowise.secret_santa.exception.IncorrectDataException;
import com.innowise.secret_santa.exception.NoAccessException;
import com.innowise.secret_santa.exception.NoDataFoundException;
import com.innowise.secret_santa.mapper.PlayerMapper;
import com.innowise.secret_santa.model.RoleEnum;
import com.innowise.secret_santa.model.SettingRolesEnum;
import com.innowise.secret_santa.model.StatusGame;
import com.innowise.secret_santa.model.TypeGame;
import com.innowise.secret_santa.model.dto.request_dto.GameRegistration;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.request_dto.PlayerRequestDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.model.dto.response_dto.PlayerResponseDto;
import com.innowise.secret_santa.model.postgres.Game;
import com.innowise.secret_santa.model.postgres.Player;
import com.innowise.secret_santa.repository.PlayerRepository;
import com.innowise.secret_santa.service.account_services.AccountRoleService;
import com.innowise.secret_santa.service.game_service.GamePlayerService;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.page_services.PageService;
import com.innowise.secret_santa.service.profile_services.ProfileGamePlayerService;
import com.innowise.secret_santa.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final GamePlayerService gamePlayerService;
    private final PlayerMapper playerMapper;
    private final ProfileGamePlayerService profileGamePlayerService;
    private final PageService<PlayerResponseDto> pageService;
    private final LoggerService<Long> loggerService;
    private final AccountRoleService accountRoleService;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             GamePlayerService gamePlayerService,
                             PlayerMapper playerMapper,
                             ProfileGamePlayerService profileGamePlayerService,
                             PageService<PlayerResponseDto> pageService,
                             LoggerService<Long> loggerService,
                             AccountRoleService accountRoleService) {
        this.playerRepository = playerRepository;
        this.gamePlayerService = gamePlayerService;
        this.playerMapper = playerMapper;
        this.profileGamePlayerService = profileGamePlayerService;
        this.pageService = pageService;
        this.loggerService = loggerService;
        this.accountRoleService = accountRoleService;
    }

    @Override
    @Transactional
    public void savePlayer(GameRegistration gameRegistration, PlayerRequestDto playerRequestDto, Long idAccount) {

        Optional.ofNullable(playerRequestDto)
                .map(playerMapper::toPlayer)
                .map(player -> setProfileInPlayer(idAccount, player))
                .map(player -> setGameInPlayer(gameRegistration, player))
                .map(player -> checkAndAddSetPlayerRole(player, idAccount))
                .map(this::setDateCreated)
                .map(playerRepository::save)
                .ifPresent(player -> loggerService.loggerInfo("Account by id: {}, created player"
                        , idAccount));
    }

    private Player setProfileInPlayer(Long idAccount, Player player) {
        player.setProfile(Optional.ofNullable(idAccount)
                .map(profileGamePlayerService::getProfileByAccountId)
                .orElseThrow(() -> new NoDataFoundException("Profile by account id: " + idAccount + " not found")));
        return player;
    }

    private Player setGameInPlayer(GameRegistration gameRegistration, Player player) {
        player.setGame(Optional.ofNullable(gameRegistration.getNameGame())
                .map(gamePlayerService::getGameByName)
                .map(game -> checkPlayers(game, player))
                .map(this::checkDateGame)
                .map(game -> checkPassword(gameRegistration.getPassword(), game))
                .orElseThrow(() -> new NoDataFoundException("Game by name: " + gameRegistration.getNameGame() + " not found")));
        return player;
    }

    private Game checkPlayers(Game game, Player player) {
        if (game.getPlayers().stream().anyMatch(item -> item.getProfile().getId().equals(player.getProfile().getId()))) {
            throw new IncorrectDataException("You are already in this game");
        }
        return game;

    }

    private Game checkDateGame(Game game) {
        if (game.getStatusGame().equals(StatusGame.FINISH)) {
            throw new NoAccessException("Game with name: " + game.getNameGame() + " finishes");
        }
        return game;
    }

    private Game checkPassword(String password, Game game) {

        if (game.getTypeGame().equals(TypeGame.CLOSED)) {
            if (!password.isBlank() && game.getPassword().equals(password)) {
                return game;
            }
            throw new NoAccessException("You have to enter correct password for participation in the game");
        }
        return game;
    }

    private Player checkAndAddSetPlayerRole(Player player, Long idAccount) {
        if (player.getProfile()
                .getAccount()
                .getRole()
                .stream()
                .anyMatch(role -> role.getRoleName().equals(RoleEnum.ROLE_PLAYER))) {
            return player;
        }
        accountRoleService.addOrDeleteRoleToAccount(idAccount, RoleEnum.ROLE_PLAYER, SettingRolesEnum.ADD);
        return player;
    }

    private Player setDateCreated(Player player) {
        player.setTimeRegistration(CalendarUtils.getFormatDate(LocalDateTime.now()));
        return player;
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponseDto getPlayerById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(playerRepository::findById)
                .map(playerMapper::toPlayerResponseDto)
                .orElseThrow(() -> new NoDataFoundException("Player by id: " + id + " not found"));
    }

    @Override
    @Transactional
    public void deletePlayerByNameGame(String nameGame, Long idAccount) {
        Player player = Optional.ofNullable(nameGame)
                .map(gamePlayerService::getGameByName)
                .map(Game::getPlayers)
                .map(players -> checkAvailabilityPlayer(idAccount, players))
                .orElseThrow(() -> new NoDataFoundException("You don't have game with name: " + nameGame));

        Optional.ofNullable(player)
                .ifPresent(playerRepository::delete);
        deleteRolePlayer(idAccount);
        loggerService.loggerInfo("Account by id: {0} deleted from game: {1}", idAccount, nameGame);
    }

    private Player checkAvailabilityPlayer(Long accountId, List<Player> playersGame) {
        return playersGame
                .stream()
                .filter(player -> player.getProfile().getAccount().getId().equals(accountId))
                .findAny()
                .orElseThrow(() -> new NoDataFoundException("You don't participate in this game"));
    }

    private void deleteRolePlayer(Long accountId) {

        List<Player> allByProfileAccountId = playerRepository.findAllByProfileAccountId(accountId);
        if (allByProfileAccountId.isEmpty()) {
            accountRoleService.addOrDeleteRoleToAccount(accountId, RoleEnum.ROLE_PLAYER, SettingRolesEnum.DELETE);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PagesDtoResponse<Object> getAllPlayers(PagesDto pages) {
        Page<PlayerResponseDto> listPlayers = playerRepository.findAll(pageService.getPage(pages))
                .map(playerMapper::toPlayerResponseDto);
        if (listPlayers.isEmpty()) {
            throw new NoDataFoundException("Players not found");
        }
        return pageService.getPagesDtoResponse(pages, listPlayers.getContent());
    }

    @Override
    @Transactional
    public PlayerResponseDto changePlayer(PlayerRequestDto playerRequestDto, Long idAccount, String nameGame) {

        return Optional.ofNullable(idAccount)
                .map(id-> playerRepository.findPlayerByGameNameGameAndProfileAccountId(nameGame, id))
                .map(player -> changeDataPlayer(player, playerRequestDto))
                .map(playerRepository::save)
                .map(playerMapper::toPlayerResponseDto)
                .orElseThrow(() -> new NoDataFoundException("You don't play games"));
    }

    private Player changeDataPlayer(Player player, PlayerRequestDto playerRequestDto) {
        String unnecessaryThings = playerRequestDto.getUnnecessaryThings();
        String necessaryThings = playerRequestDto.getNecessaryThings();

        if (!unnecessaryThings.isBlank() && !unnecessaryThings.equals(player.getUnnecessaryThings())) {
            player.setUnnecessaryThings(unnecessaryThings);
        }
        if (!necessaryThings.isBlank() && !necessaryThings.equals(player.getNecessaryThings())) {
            player.setNecessaryThings(necessaryThings);
        }
        return player;
    }

    @Override
    public List<PlayerResponseDto> getAllPlayersFromGame(String nameGame, Long idAuthenticationAccount) {

        Game gameByName = gamePlayerService.getGameByName(nameGame);

        checkOrganizer
                (
                        gameByName.getOrganizer().getId(),
                        profileGamePlayerService.getProfileByAccountId(idAuthenticationAccount).getId()
                );

        return Optional.of(gameByName)
                .map(Game::getPlayers)
                .map(players -> players
                        .stream()
                        .map(playerMapper::toPlayerResponseDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new NoDataFoundException("Game: " + nameGame + " doesn't have players"));
    }

    private void checkOrganizer(Long organizerId, Long currentProfileId) {
        if (!organizerId.equals(currentProfileId)) {
            throw new NoAccessException(" Your account doesn't have permission");
        }
    }

    @Override
    public List<PlayerResponseDto> getCurrentPlayers(Long idAccount) {

        List<Player> players = playerRepository.findAllByProfileAccountId(idAccount);
        List<PlayerResponseDto> playersDto = new ArrayList<>();
        if (players.isEmpty()) {
            throw new NoDataFoundException("You don't play games");
        }
        for (Player player : players) {
            playersDto.add(playerMapper.toPlayerResponseDto(player));
        }
        return playersDto;
    }
}