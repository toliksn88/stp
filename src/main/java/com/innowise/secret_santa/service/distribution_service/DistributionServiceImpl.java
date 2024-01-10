package com.innowise.secret_santa.service.distribution_service;

import com.innowise.secret_santa.exception.NoDataFoundException;
import com.innowise.secret_santa.mapper.DistributionMapper;
import com.innowise.secret_santa.model.TypeMessage;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.DistributionResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.model.postgres.Distribution;
import com.innowise.secret_santa.model.postgres.Game;
import com.innowise.secret_santa.model.postgres.Player;
import com.innowise.secret_santa.repository.DistributionRepository;
import com.innowise.secret_santa.service.game_service.GameDistributionService;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.message_services.SystemMessageService;
import com.innowise.secret_santa.service.page_services.PageService;
import com.innowise.secret_santa.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DistributionServiceImpl implements DistributionService {

    private final GameDistributionService gameDistributionService;
    private final DistributionRepository distributionRepository;
    private final SystemMessageService sentMessagesService;
    private final LoggerService<Long> loggerService;
    private final DistributionMapper distributionMapper;
    private final PageService<DistributionResponseDto> pageService;

    @Autowired
    public DistributionServiceImpl(GameDistributionService gameDistributionService,
                                   DistributionRepository distributionRepository,
                                   SystemMessageService sentMessagesService,
                                   LoggerService<Long> loggerService,
                                   DistributionMapper distributionMapper,
                                   PageService<DistributionResponseDto> pageService) {
        this.gameDistributionService = gameDistributionService;
        this.distributionRepository = distributionRepository;
        this.sentMessagesService = sentMessagesService;
        this.loggerService = loggerService;
        this.distributionMapper = distributionMapper;
        this.pageService = pageService;
    }


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void createDistributions() {
        List<Game> allGamesAfterCurrentDate = gameDistributionService.getAllGamesAfterCurrentDate();

        for (Game game : allGamesAfterCurrentDate) {
            distributionRepository.saveAll(distribution(game));
            gameDistributionService.changeStatusGameInFinish(game);
            loggerService.loggerInfo("Distribution for game: {0} finished", game.getNameGame());
        }
    }

    private List<Distribution> distribution(Game game) {

        List<Player> players = getPlayersFromGame(game);
        Collections.shuffle(players);
        List<Distribution> distributions = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            if (i + 1 != players.size()) {
                distributions.add(buildDistribution(players.get(i), players.get(i + 1), game));
                sendMessageAboutDistribution(players.get(i), players.get(i + 1));
            } else {
                distributions.add(buildDistribution(players.get(i), players.get(0), game));
                sendMessageAboutDistribution(players.get(i), players.get(0));
            }
        }
        return distributions;
    }

    private List<Player> getPlayersFromGame(Game game) {
        List<Player> players = game.getPlayers();
        if (players.isEmpty()) {
            throw new NoDataFoundException("Not enough players in the game: " + game.getNameGame());
        }
        return players;
    }

    private Distribution buildDistribution(Player from, Player to, Game game) {
        loggerService.loggerInfo("Player: {0} have to present player: {1} from game: {2}"
                , from.getId(), to.getId(), game.getNameGame());

        return Distribution.builder()
                .game(game)
                .senderPlayer(from)
                .targetPlayer(to)
                .timeCreated(CalendarUtils.getFormatDate(LocalDateTime.now()))
                .build();
    }

    private void sendMessageAboutDistribution(Player fromGift, Player toGift) {

        sentMessagesService.messageService(TypeMessage.DISTRIBUTION, fromGift.getProfile().getAccount().getId(),
                fromGift.getProfile().getAccount().getEmail(), toGift.getProfile().getAccount().getEmail());

        loggerService.loggerInfo("Account by email {}, get email about your player",
                fromGift.getProfile().getAccount().getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponseDto> getDistributionsForCurrentAccount(Long accountId) {
        loggerService.loggerInfo("Account by id: {} look your distributions", accountId);

        return Optional.of(accountId)
                .map(distributionRepository::findAllBySenderPlayerProfileAccountId)
                .map(distributions -> distributions
                        .stream()
                        .map(distributionMapper::toDistributionResponseDto)
                        .collect(Collectors.toList()))
                .map(this::checkListEmpty)
                .orElseThrow(() -> new NoDataFoundException("Don't find distribution for current account"));
    }

    private List<DistributionResponseDto> checkListEmpty(List<DistributionResponseDto> distributionsResponseDto) {
        if (distributionsResponseDto.isEmpty()) {
            throw new NoDataFoundException("Don't find distribution");
        }
        return distributionsResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public DistributionResponseDto getDistributionCurrentAccountByNameGame(Long accountId, String nameGame) {
        loggerService.loggerInfo("Account by id: {0} look your distributions for game: {1}"
                , accountId, nameGame);

        return Optional.ofNullable(distributionRepository
                        .findDistributionByGameNameGameAndSenderPlayerProfileAccountId(nameGame, accountId))
                .map(distributionMapper::toDistributionResponseDto)
                .orElseThrow(
                        () -> new NoDataFoundException("For current account not distributions for game: " + nameGame));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponseDto> getAllDistributionForOrganizerByNameGame(Long idAccount, String gameName) {

        List<DistributionResponseDto> distributionsResponseDto =
                Optional.of(distributionRepository.findAllByGameNameGameAndGameOrganizerAccountId(gameName, idAccount))
                        .map(distributions -> distributions
                                .stream()
                                .map(distributionMapper::toDistributionResponseDto)
                                .collect(Collectors.toList()))
                        .map(this::checkListEmpty)
                        .orElseThrow(() -> new NoDataFoundException("You aren't organizer for game: "
                                + gameName
                                + " or in game don't have enough players"));

        loggerService.loggerInfo("Account by id: {0}, get all distributions for game: {1}"
                , idAccount, gameName);
        return distributionsResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public PagesDtoResponse<Object> getAllDistributions(PagesDto pages) {
        Page<DistributionResponseDto> listDistributions = distributionRepository.findAll(pageService.getPage(pages))
                .map(distributionMapper::toDistributionResponseDto);
        if (listDistributions.isEmpty()) {
            throw new NoDataFoundException("Distributions not found");
        }
        loggerService.loggerInfo("Use method getAllDistributions");
        return pageService.getPagesDtoResponse(pages, listDistributions.getContent());
    }
}