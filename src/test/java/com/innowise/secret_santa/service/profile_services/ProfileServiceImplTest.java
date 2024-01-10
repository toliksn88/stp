package com.innowise.secret_santa.service.profile_services;

import com.innowise.secret_santa.mapper.ProfileMapper;
import com.innowise.secret_santa.model.dto.ProfileDto;
import com.innowise.secret_santa.repository.ProfileRepository;
import com.innowise.secret_santa.service.account_services.AccountProfileService;
import com.innowise.secret_santa.service.address_services.AddressProfileService;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.page_services.PageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_WITHOUT_PROFILE;
import static com.innowise.secret_santa.constants.TestConstants.ADDRESS;
import static com.innowise.secret_santa.constants.TestConstants.ADDRESS_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_CREATED_PROFILE;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_DELETE_PROFILE;
import static com.innowise.secret_santa.constants.TestConstants.PAGEABLE;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_PROFILE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_PROFILE;
import static com.innowise.secret_santa.constants.TestConstants.PROFILE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PROFILE_WITHOUT_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.PROFILE_WITH_ACCOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ProfileMapper profileMapper;
    @Mock
    private LoggerService<ProfileDto> logger;
    @Mock
    private AccountProfileService accountService;
    @Mock
    private PageService<ProfileDto> pageService;
    @Mock
    private AddressProfileService addressProfileService;
    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void should_Save_Profile_In_DataBase_FromProfileDto_And_AccountId() {
        given(profileMapper.toProfile(PROFILE_DTO)).willReturn(PROFILE_WITHOUT_ACCOUNT);
        given(accountService.getAccountById(ID)).willReturn(ACCOUNT_WITHOUT_PROFILE);
        given(profileRepository.save(PROFILE_WITH_ACCOUNT)).willReturn(PROFILE_WITH_ACCOUNT);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_CREATED_PROFILE, ID);

        profileService.createdProfile(PROFILE_DTO, ID);

        then(profileMapper).should(times(1)).toProfile(PROFILE_DTO);
        then(accountService).should(times(1)).getAccountById(ID);
        then(profileRepository).should(times(1)).save(PROFILE_WITH_ACCOUNT);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_CREATED_PROFILE, ID);
    }

    @Test
    void should_delete_Profile_In_DataBase_By_AccountId() {
        given(profileRepository.findProfileByAccountId(ID)).willReturn(PROFILE_WITH_ACCOUNT);
        doNothing().when(profileRepository).delete(PROFILE_WITH_ACCOUNT);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_DELETE_PROFILE, ID);

        profileService.deleteProfile(ID);

        then(profileRepository).should(times(1)).findProfileByAccountId(ID);
        verify(profileRepository, times(1)).delete(PROFILE_WITH_ACCOUNT);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_DELETE_PROFILE, ID);
    }

    @Test
    void should_Get_ProfileDto_By_AccountId() {
        given(profileRepository.findProfileByAccountId(ID)).willReturn(PROFILE_WITH_ACCOUNT);
        given(profileMapper.toProfileDto(PROFILE_WITH_ACCOUNT)).willReturn(PROFILE_DTO);

        assertEquals(PROFILE_DTO, profileService.getCurrencyProfile(ID));

        then(profileRepository).should(times(1)).findProfileByAccountId(ID);
        then(profileMapper).should(times(1)).toProfileDto(PROFILE_WITH_ACCOUNT);
    }

    @Test
    void should_Get_ProfileDto_By_ProfileId() {
        given(profileRepository.findById(ID)).willReturn(Optional.of(PROFILE_WITH_ACCOUNT));
        given(profileMapper.toProfileDto(PROFILE_WITH_ACCOUNT)).willReturn(PROFILE_DTO);

        assertEquals(PROFILE_DTO, profileService.getProfileDtoById(ID));

        then(profileRepository).should(times(1)).findById(ID);
        then(profileMapper).should(times(1)).toProfileDto(PROFILE_WITH_ACCOUNT);
    }

    @Test
    void should_Update_Profile_And_Save_In_DataBase() {
        given(profileRepository.findProfileByAccountId(ID)).willReturn(PROFILE_WITH_ACCOUNT);
        given(addressProfileService.changeAddressData(ADDRESS,ADDRESS_DTO)).willReturn(ADDRESS);
        given(profileRepository.save(PROFILE_WITH_ACCOUNT)).willReturn(PROFILE_WITH_ACCOUNT);
        given(profileMapper.toProfileDto(PROFILE_WITH_ACCOUNT)).willReturn(PROFILE_DTO);

        assertEquals(PROFILE_DTO, profileService.updateProfile(ID, PROFILE_DTO));

        then(profileRepository).should(times(1)).findProfileByAccountId(ID);
        then(addressProfileService).should(times(1)).changeAddressData(ADDRESS,ADDRESS_DTO);
        then(profileRepository).should(times(1)).save(PROFILE_WITH_ACCOUNT);
        then(profileMapper).should(times(1)).toProfileDto(PROFILE_WITH_ACCOUNT);
    }

    @Test
    void should_Get_PageDtoResponse_With_All_ProfileDto() {
        given(pageService.getPage(PAGES_DTO)).willReturn(PAGEABLE);
        given(profileRepository.findAll(PAGEABLE)).willReturn(PAGE_PROFILE);
        given(profileMapper.toProfileDto(PROFILE_WITH_ACCOUNT)).willReturn(PROFILE_DTO);
        given(pageService.getPagesDtoResponse(PAGES_DTO, List.of(PROFILE_DTO))).willReturn(PAGES_DTO_RESPONSE_PROFILE_DTO);

        assertEquals(PAGES_DTO_RESPONSE_PROFILE_DTO, profileService.getAllProfiles(PAGES_DTO));

        then(pageService).should(times(1)).getPage(PAGES_DTO);
        then(profileRepository).should(times(1)).findAll(PAGEABLE);
        then(profileMapper).should(times(1)).toProfileDto(PROFILE_WITH_ACCOUNT);
        then(pageService).should(times(1)).getPagesDtoResponse(PAGES_DTO, List.of(PROFILE_DTO));
    }
}