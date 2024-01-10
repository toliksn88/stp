package com.innowise.secret_santa.service.player_services;

import com.innowise.secret_santa.mapper.PlayerMapper;
import com.innowise.secret_santa.model.dto.response_dto.PlayerResponseDto;
import com.innowise.secret_santa.repository.PlayerRepository;
import com.innowise.secret_santa.service.account_services.AccountRoleService;
import com.innowise.secret_santa.service.game_service.GamePlayerService;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.page_services.PageService;
import com.innowise.secret_santa.service.profile_services.ProfileGamePlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.innowise.secret_santa.constants.TestConstants.GAME_REGISTRATION;
import static com.innowise.secret_santa.constants.TestConstants.GAME_WITHOUT_PLAYERS;
import static com.innowise.secret_santa.constants.TestConstants.GAME_WITH_ORGANIZER;
import static com.innowise.secret_santa.constants.TestConstants.GAME_WITH_PLAYERS;
import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_CREATED_PLAYER;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_DELETE_PLAYER;
import static com.innowise.secret_santa.constants.TestConstants.NAME_GAME;
import static com.innowise.secret_santa.constants.TestConstants.PAGEABLE;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_PLAYER_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_PLAYER;
import static com.innowise.secret_santa.constants.TestConstants.PLAYER;
import static com.innowise.secret_santa.constants.TestConstants.PLAYERS;
import static com.innowise.secret_santa.constants.TestConstants.PLAYERS_RESPONSE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PLAYERS_WITH_GAME;
import static com.innowise.secret_santa.constants.TestConstants.PLAYER_REQUEST_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PLAYER_RESPONSE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PLAYER_WITH_GAME;
import static com.innowise.secret_santa.constants.TestConstants.PROFILE_WITH_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.ROLE_ENUM_PLAYER;
import static com.innowise.secret_santa.constants.TestConstants.SETTING_ROLES_ENUM_ADD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GamePlayerService gamePlayerService;
    @Mock
    private PlayerMapper playerMapper;
    @Mock
    private ProfileGamePlayerService profileGamePlayerService;
    @Mock
    private PageService<PlayerResponseDto> pageService;
    @Mock
    private LoggerService<Long> loggerService;
    @Mock
    private AccountRoleService accountRoleService;
    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void should_Save_Player_In_DataBase() {
        given(playerMapper.toPlayer(PLAYER_REQUEST_DTO)).willReturn(PLAYER);
        given(profileGamePlayerService.getProfileByAccountId(ID)).willReturn(PROFILE_WITH_ACCOUNT);
        given(gamePlayerService.getGameByName(NAME_GAME)).willReturn(GAME_WITH_PLAYERS);
        doNothing().when(accountRoleService).addOrDeleteRoleToAccount(ID, ROLE_ENUM_PLAYER, SETTING_ROLES_ENUM_ADD);
        given(playerRepository.save(PLAYER)).willReturn(PLAYER);
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_CREATED_PLAYER, ID);

        playerService.savePlayer(GAME_REGISTRATION, PLAYER_REQUEST_DTO, ID);

        then(playerMapper).should(times(1)).toPlayer(PLAYER_REQUEST_DTO);
        then(profileGamePlayerService).should(times(1)).getProfileByAccountId(ID);
        then(gamePlayerService).should(times(1)).getGameByName(NAME_GAME);
        verify(accountRoleService, times(1)).addOrDeleteRoleToAccount(ID, ROLE_ENUM_PLAYER, SETTING_ROLES_ENUM_ADD);
        then(playerRepository).should(times(1)).save(PLAYER);
        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_CREATED_PLAYER, ID);
    }

    @Test
    void should_Get_Player_By_Id() {
        given(playerRepository.findById(ID)).willReturn(Optional.of(PLAYER));
        given(playerMapper.toPlayerResponseDto(PLAYER)).willReturn(PLAYER_RESPONSE_DTO);

        assertEquals(PLAYER_RESPONSE_DTO, playerService.getPlayerById(ID));

        then(playerRepository).should(times(1)).findById(ID);
        then(playerMapper).should(times(1)).toPlayerResponseDto(PLAYER);
    }

    @Test
    void should_Delete_Player_By_Name_Game() {
        given(gamePlayerService.getGameByName(NAME_GAME)).willReturn(GAME_WITH_PLAYERS);
        doNothing().when(playerRepository).delete(PLAYER);
        given(playerRepository.findAllByProfileAccountId(ID)).willReturn(PLAYERS);
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_DELETE_PLAYER, ID, NAME_GAME);

        playerService.deletePlayerByNameGame(NAME_GAME, ID);

        then(gamePlayerService).should(times(1)).getGameByName(NAME_GAME);
        verify(playerRepository, times(1)).delete(PLAYER);
        then(playerRepository).should(times(1)).findAllByProfileAccountId(ID);
        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_DELETE_PLAYER, ID, NAME_GAME);
    }

    @Test
    void should_Get_PageDtoResponse_With_All_Players() {
        given(pageService.getPage(PAGES_DTO)).willReturn(PAGEABLE);
        given(playerRepository.findAll(PAGEABLE)).willReturn(PAGE_PLAYER);
        given(playerMapper.toPlayerResponseDto(PLAYER)).willReturn(PLAYER_RESPONSE_DTO);
        given(pageService.getPagesDtoResponse(PAGES_DTO, PLAYERS_RESPONSE_DTO)).willReturn(PAGES_DTO_RESPONSE_PLAYER_DTO);

        assertEquals(PAGES_DTO_RESPONSE_PLAYER_DTO, playerService.getAllPlayers(PAGES_DTO));

        then(pageService).should(times(1)).getPage(PAGES_DTO);
        then(playerRepository).should(times(1)).findAll(PAGEABLE);
        then(playerMapper).should(times(1)).toPlayerResponseDto(PLAYER);
        then(pageService).should(times(1)).getPagesDtoResponse(PAGES_DTO, PLAYERS_RESPONSE_DTO);

    }

    @Test
    void should_Change_Player() {
        given(playerRepository.findPlayerByGameNameGameAndProfileAccountId(NAME_GAME, ID)).willReturn(PLAYER_WITH_GAME);
        given(playerRepository.save(PLAYER_WITH_GAME)).willReturn(PLAYER_WITH_GAME);
        given(playerMapper.toPlayerResponseDto(PLAYER_WITH_GAME)).willReturn(PLAYER_RESPONSE_DTO);

        assertEquals(PLAYER_RESPONSE_DTO, playerService.changePlayer(PLAYER_REQUEST_DTO, ID, NAME_GAME));

        then(playerRepository).should(times(1)).findPlayerByGameNameGameAndProfileAccountId(NAME_GAME, ID);
        then(playerRepository).should(times(1)).save(PLAYER_WITH_GAME);
        then(playerMapper).should(times(1)).toPlayerResponseDto(PLAYER);

    }

    @Test
    void should_Get_All_Players_For_Game() {
        given(gamePlayerService.getGameByName(NAME_GAME)).willReturn(GAME_WITH_ORGANIZER);
        given(profileGamePlayerService.getProfileByAccountId(ID)).willReturn(PROFILE_WITH_ACCOUNT);
        given(playerMapper.toPlayerResponseDto(PLAYER_WITH_GAME)).willReturn(PLAYER_RESPONSE_DTO);

        assertEquals(PLAYERS_RESPONSE_DTO, playerService.getAllPlayersFromGame(NAME_GAME, ID));

        then(gamePlayerService).should(times(1)).getGameByName(NAME_GAME);
        then(profileGamePlayerService).should(times(1)).getProfileByAccountId(ID);
        then(playerMapper).should(times(1)).toPlayerResponseDto(PLAYER_WITH_GAME);
    }

    @Test
    void should_Get_Current_Players(){
        given(playerRepository.findAllByProfileAccountId(ID)).willReturn(PLAYERS);
        given(playerMapper.toPlayerResponseDto(PLAYER)).willReturn(PLAYER_RESPONSE_DTO);

        assertEquals(PLAYERS_RESPONSE_DTO, playerService.getCurrentPlayers(ID));

        then(playerRepository).should(times(1)).findAllByProfileAccountId(ID);
        then(playerMapper).should(times(1)).toPlayerResponseDto(PLAYER);
    }
}