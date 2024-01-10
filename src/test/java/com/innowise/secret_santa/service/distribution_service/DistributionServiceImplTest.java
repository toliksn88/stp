package com.innowise.secret_santa.service.distribution_service;

import com.innowise.secret_santa.mapper.DistributionMapper;
import com.innowise.secret_santa.model.dto.response_dto.DistributionResponseDto;
import com.innowise.secret_santa.repository.DistributionRepository;
import com.innowise.secret_santa.service.game_service.GameDistributionService;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.message_services.SystemMessageService;
import com.innowise.secret_santa.service.page_services.PageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.innowise.secret_santa.constants.TestConstants.ACCOUNTS_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_WITH_PROFILE;
import static com.innowise.secret_santa.constants.TestConstants.DISTRIBUTION;
import static com.innowise.secret_santa.constants.TestConstants.DISTRIBUTIONS;
import static com.innowise.secret_santa.constants.TestConstants.DISTRIBUTION_RESPONSE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.DISTRIBUTION_WITH_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.EMAIL;
import static com.innowise.secret_santa.constants.TestConstants.GAMES_WITH_ACCOUNTS;
import static com.innowise.secret_santa.constants.TestConstants.GAME_WITH_PLAYERS;
import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_ABOUT_DISTRIBUTION;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_ACCOUNT_EMAIL;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_GET_ALL_DISTRIBUTIONS_FOR_ORGANIZER;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_INFO_DISTRIBUTIONS;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_LOOK_ACCOUNT_DISTRIBUTION;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_LOOK_DISTRIBUTION;
import static com.innowise.secret_santa.constants.TestConstants.NAME_GAME;
import static com.innowise.secret_santa.constants.TestConstants.PAGEABLE;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_ACCOUNT_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_DISTRIBUTION_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_DISTRIBUTION;
import static com.innowise.secret_santa.constants.TestConstants.TYPE_MESSAGE_DISTRIBUTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DistributionServiceImplTest {

    @Mock
    private GameDistributionService gameDistributionService;
    @Mock
    private DistributionRepository distributionRepository;
    @Mock
    private SystemMessageService sentMessagesService;
    @Mock
    private LoggerService<Long> loggerService;
    @Mock
    private DistributionMapper distributionMapper;
    @Mock
    private PageService<DistributionResponseDto> pageService;
    @InjectMocks
    private DistributionServiceImpl distributionService;

    @Test
    void should_Create_Distribution_In_Time() {
        given(gameDistributionService.getAllGamesAfterCurrentDate()).willReturn(List.of(GAMES_WITH_ACCOUNTS));
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_INFO_DISTRIBUTIONS, ID, ID, NAME_GAME);
        doNothing().when(sentMessagesService).messageService(TYPE_MESSAGE_DISTRIBUTION, ID, EMAIL, EMAIL);
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_ACCOUNT_EMAIL, EMAIL);
        doNothing().when(gameDistributionService).changeStatusGameInFinish(GAME_WITH_PLAYERS);
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_ABOUT_DISTRIBUTION, NAME_GAME);

        distributionService.createDistributions();

        then(gameDistributionService).should(times(1)).getAllGamesAfterCurrentDate();
        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_INFO_DISTRIBUTIONS, ID, ID, NAME_GAME);
        verify(sentMessagesService, times(1)).messageService(TYPE_MESSAGE_DISTRIBUTION, ID, EMAIL, EMAIL);
        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_ACCOUNT_EMAIL, EMAIL);
        verify(gameDistributionService, times(1)).changeStatusGameInFinish(GAME_WITH_PLAYERS);
        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_ABOUT_DISTRIBUTION, NAME_GAME);
    }
    @Test
    void should_Get_All_Distributions_For_Current_Account(){
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_LOOK_DISTRIBUTION, ID);
        given(distributionRepository.findAllBySenderPlayerProfileAccountId(ID)).willReturn(DISTRIBUTIONS);
        given(distributionMapper.toDistributionResponseDto(DISTRIBUTION)).willReturn(DISTRIBUTION_RESPONSE_DTO);

        assertEquals(List.of(DISTRIBUTION_RESPONSE_DTO), distributionService.getDistributionsForCurrentAccount(ID));

        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_LOOK_DISTRIBUTION, ID);
        then(distributionRepository).should(times(1)).findAllBySenderPlayerProfileAccountId(ID);
        then(distributionMapper).should(times(1)).toDistributionResponseDto(DISTRIBUTION);
    }
    @Test
    void should_Get_Distribution_Current_Account_By_NameGame(){
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_LOOK_ACCOUNT_DISTRIBUTION, ID, NAME_GAME);
        given(distributionRepository.findDistributionByGameNameGameAndSenderPlayerProfileAccountId(NAME_GAME, ID)).willReturn(DISTRIBUTION_WITH_ACCOUNT);
        given(distributionMapper.toDistributionResponseDto(DISTRIBUTION_WITH_ACCOUNT)).willReturn(DISTRIBUTION_RESPONSE_DTO);

        assertEquals(DISTRIBUTION_RESPONSE_DTO, distributionService.getDistributionCurrentAccountByNameGame(ID, NAME_GAME));

        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_LOOK_ACCOUNT_DISTRIBUTION, ID, NAME_GAME);
        then(distributionRepository).should(times(1)).findDistributionByGameNameGameAndSenderPlayerProfileAccountId(NAME_GAME, ID);
        then(distributionMapper).should(times(1)).toDistributionResponseDto(DISTRIBUTION_WITH_ACCOUNT);

    }

    @Test
    void should_Get_All_Distributions_For_Organizer_By_NameGame(){
        given(distributionRepository.findAllByGameNameGameAndGameOrganizerAccountId(NAME_GAME, ID)).willReturn(List.of(DISTRIBUTION_WITH_ACCOUNT));
        given(distributionMapper.toDistributionResponseDto(DISTRIBUTION_WITH_ACCOUNT)).willReturn(DISTRIBUTION_RESPONSE_DTO);
        doNothing().when(loggerService).loggerInfo(LOGGER_MESSAGE_GET_ALL_DISTRIBUTIONS_FOR_ORGANIZER, ID, NAME_GAME);

        assertEquals(List.of(DISTRIBUTION_RESPONSE_DTO), distributionService.getAllDistributionForOrganizerByNameGame(ID, NAME_GAME));

        then(distributionRepository).should(times(1)).findAllByGameNameGameAndGameOrganizerAccountId(NAME_GAME, ID);
        then(distributionMapper).should(times(1)).toDistributionResponseDto(DISTRIBUTION_WITH_ACCOUNT);
        verify(loggerService, times(1)).loggerInfo(LOGGER_MESSAGE_GET_ALL_DISTRIBUTIONS_FOR_ORGANIZER, ID, NAME_GAME);
    }

    @Test
    void should_Get_PagesDtoResponse_With_All_DistributionsResponseDto(){
        given(pageService.getPage(PAGES_DTO)).willReturn(PAGEABLE);
        given(distributionRepository.findAll(PAGEABLE)).willReturn(PAGE_DISTRIBUTION);
        given(distributionMapper.toDistributionResponseDto(DISTRIBUTION_WITH_ACCOUNT)).willReturn(DISTRIBUTION_RESPONSE_DTO);
        given(pageService.getPagesDtoResponse(PAGES_DTO, List.of(DISTRIBUTION_RESPONSE_DTO))).willReturn(PAGES_DTO_RESPONSE_DISTRIBUTION_DTO);

        assertEquals(PAGES_DTO_RESPONSE_DISTRIBUTION_DTO, distributionService.getAllDistributions(PAGES_DTO));

        then(pageService).should(times(1)).getPage(PAGES_DTO);
        then(distributionRepository).should(times(1)).findAll(PAGEABLE);
        then(distributionMapper).should(times(1)).toDistributionResponseDto(DISTRIBUTION_WITH_ACCOUNT);
        then(pageService).should(times(1)).getPagesDtoResponse(PAGES_DTO, List.of(DISTRIBUTION_RESPONSE_DTO));

    }


}