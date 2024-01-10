package com.innowise.secret_santa.service.game_service;

import com.innowise.secret_santa.mapper.GameMapper;
import com.innowise.secret_santa.model.dto.response_dto.GameResponseDto;
import com.innowise.secret_santa.repository.GameRepository;
import com.innowise.secret_santa.service.account_services.AccountRoleService;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.page_services.PageService;
import com.innowise.secret_santa.service.profile_services.ProfileGamePlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.innowise.secret_santa.constants.TestConstants.DATE;
import static com.innowise.secret_santa.constants.TestConstants.GAMES;
import static com.innowise.secret_santa.constants.TestConstants.GAMES_RESPONSE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.GAME_REQUEST_DTO;
import static com.innowise.secret_santa.constants.TestConstants.GAME_RESPONSE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.GAME_WITHOUT_PLAYERS;
import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_ABOUT_CHANGE_STATUS_GAME;
import static com.innowise.secret_santa.constants.TestConstants.NAME_GAME;
import static com.innowise.secret_santa.constants.TestConstants.PAGEABLE;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_GAMES_RESPONSE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_GAME;
import static com.innowise.secret_santa.constants.TestConstants.PROFILE_WITH_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.ROLE_ENUM_ORGANIZER;
import static com.innowise.secret_santa.constants.TestConstants.SETTING_ROLES_ENUM_DELETE;
import static com.innowise.secret_santa.constants.TestConstants.STATUS_GAME_START;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameMapper gameMapper;
    @Mock
    private ProfileGamePlayerService profileGameService;
    @Mock
    private AccountRoleService accountGameService;
    @Mock
    private PageService<GameResponseDto> pageService;
    @Mock
    private LoggerService<Long> logger;
    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void should_Save_Game_In_DataBase_And_Return_GameResponseDto() {
        given(gameRepository.existsByNameGame(NAME_GAME)).willReturn(false);
        given(gameMapper.toGameFromGameRequestDto(GAME_REQUEST_DTO)).willReturn(GAME_WITHOUT_PLAYERS);
        given(profileGameService.getProfileByAccountId(ID)).willReturn(PROFILE_WITH_ACCOUNT);
        given(gameRepository.save(GAME_WITHOUT_PLAYERS)).willReturn(GAME_WITHOUT_PLAYERS);
        given(gameMapper.toGameResponseDto(GAME_WITHOUT_PLAYERS)).willReturn(GAME_RESPONSE_DTO);

        assertEquals(GAME_RESPONSE_DTO, gameService.createGame(GAME_REQUEST_DTO, ID));

        then(gameRepository).should(times(1)).existsByNameGame(NAME_GAME);
        then(gameMapper).should(times(1)).toGameFromGameRequestDto(GAME_REQUEST_DTO);
        then(profileGameService).should(times(1)).getProfileByAccountId(ID);
        then(gameRepository).should(times(1)).save(GAME_WITHOUT_PLAYERS);
        then(gameMapper).should(times(1)).toGameResponseDto(GAME_WITHOUT_PLAYERS);
    }

    @Test
    void should_Get_Game_By_Id() {
        given(gameRepository.findById(ID)).willReturn(Optional.of(GAME_WITHOUT_PLAYERS));
        given(gameMapper.toGameResponseDto(GAME_WITHOUT_PLAYERS)).willReturn(GAME_RESPONSE_DTO);

        assertEquals(GAME_RESPONSE_DTO, gameService.getGameById(ID));

        then(gameRepository).should(times(1)).findById(ID);
        then(gameMapper).should(times(1)).toGameResponseDto(GAME_WITHOUT_PLAYERS);
    }

    @Test
    void should_Delete_Game_In_DataBase() {
        given(gameRepository.findAllByOrganizerAccountId(ID)).willReturn(List.of(GAME_WITHOUT_PLAYERS));
        doNothing().when(accountGameService).addOrDeleteRoleToAccount(ID, ROLE_ENUM_ORGANIZER, SETTING_ROLES_ENUM_DELETE);
        doNothing().when(gameRepository).delete(GAME_WITHOUT_PLAYERS);

        gameService.deleteGame(ID, NAME_GAME);

        then(gameRepository).should(times(1)).findAllByOrganizerAccountId(ID);
        verify(accountGameService, times(1)).addOrDeleteRoleToAccount(ID, ROLE_ENUM_ORGANIZER, SETTING_ROLES_ENUM_DELETE);
        verify(gameRepository, times(1)).delete(GAME_WITHOUT_PLAYERS);
    }

    @Test
    void should_Change_Game_And_Return_GameResponseDto() {
        given(gameRepository.findAllByOrganizerAccountId(ID)).willReturn(List.of(GAME_WITHOUT_PLAYERS));
        given(gameRepository.save(GAME_WITHOUT_PLAYERS)).willReturn(GAME_WITHOUT_PLAYERS);
        given(gameMapper.toGameResponseDto(GAME_WITHOUT_PLAYERS)).willReturn(GAME_RESPONSE_DTO);

        assertEquals(GAME_RESPONSE_DTO, gameService.changeGame(GAME_REQUEST_DTO, ID, NAME_GAME));

        then(gameRepository).should(times(1)).findAllByOrganizerAccountId(ID);
        then(gameRepository).should(times(1)).save(GAME_WITHOUT_PLAYERS);
        then(gameMapper).should(times(1)).toGameResponseDto(GAME_WITHOUT_PLAYERS);
    }

    @Test
    void should_Get_Game_By_Name() {
        given(gameRepository.findGameByNameGame(NAME_GAME)).willReturn(GAME_WITHOUT_PLAYERS);

        assertEquals(GAME_WITHOUT_PLAYERS, gameService.getGameByName(NAME_GAME));

        then(gameRepository).should(times(1)).findGameByNameGame(NAME_GAME);
    }

    @Test
    void should_Get_PagesDtoResponse_With_All_Games() {
        given(pageService.getPage(PAGES_DTO)).willReturn(PAGEABLE);
        given(gameRepository.findAll(PAGEABLE)).willReturn(PAGE_GAME);
        given(gameMapper.toGameResponseDto(GAME_WITHOUT_PLAYERS)).willReturn(GAME_RESPONSE_DTO);
        given(pageService.getPagesDtoResponse(PAGES_DTO, GAMES_RESPONSE_DTO)).willReturn(PAGES_DTO_RESPONSE_GAMES_RESPONSE_DTO);

        assertEquals(PAGES_DTO_RESPONSE_GAMES_RESPONSE_DTO, gameService.getAllGames(PAGES_DTO));

        then(pageService).should(times(1)).getPage(PAGES_DTO);
        then(gameRepository).should(times(1)).findAll(PAGEABLE);
        then(gameMapper).should(times(1)).toGameResponseDto(GAME_WITHOUT_PLAYERS);
        then(pageService).should(times(1)).getPagesDtoResponse(PAGES_DTO, GAMES_RESPONSE_DTO);

    }

    @Test
    void should_Get_All_Games_After_Date() {
        given(gameRepository.findAllByStatusGameAndTimeEndBefore(STATUS_GAME_START,
                DATE))
                .willReturn(GAMES);

        assertEquals(GAMES, gameService.getAllGamesAfterCurrentDate());
    }

    @Test
    void should_Change_Status_Game_In_Finish(){
        given(gameRepository.save(GAME_WITHOUT_PLAYERS)).willReturn(GAME_WITHOUT_PLAYERS);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_ABOUT_CHANGE_STATUS_GAME, NAME_GAME);

        gameService.changeStatusGameInFinish(GAME_WITHOUT_PLAYERS);

        then(gameRepository).should(times(1)).save(GAME_WITHOUT_PLAYERS);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_ABOUT_CHANGE_STATUS_GAME, NAME_GAME);
    }
}