package com.innowise.secret_santa.service.profile_services;

import com.innowise.secret_santa.exception.IncorrectDataException;
import com.innowise.secret_santa.exception.MapperException;
import com.innowise.secret_santa.exception.NoDataFoundException;
import com.innowise.secret_santa.mapper.ProfileMapper;
import com.innowise.secret_santa.model.dto.ProfileDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.model.postgres.Account;
import com.innowise.secret_santa.model.postgres.Address;
import com.innowise.secret_santa.model.postgres.Profile;
import com.innowise.secret_santa.repository.ProfileRepository;
import com.innowise.secret_santa.service.account_services.AccountProfileService;
import com.innowise.secret_santa.service.address_services.AddressProfileService;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.page_services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService, ProfileGamePlayerService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final LoggerService<ProfileDto> logger;
    private final AccountProfileService accountService;
    private final PageService<ProfileDto> pageService;
    private final AddressProfileService addressProfileService;


    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository,
                              ProfileMapper profileMapper,
                              LoggerService<ProfileDto> logger,
                              AccountProfileService accountService,
                              PageService<ProfileDto> pageService,
                              AddressProfileService addressProfileService) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.logger = logger;
        this.accountService = accountService;
        this.pageService = pageService;
        this.addressProfileService = addressProfileService;
    }

    @Override
    @Transactional
    public void createdProfile(ProfileDto profile, Long idAccount) {

        Optional.ofNullable(profile)
                .map(profileMapper::toProfile)
                .map(item -> setAccountToProfile(item, idAccount))
                .map(profileRepository::save)
                .ifPresent(log -> logger.loggerInfo(
                        "Account by id: {}, created profile",
                        idAccount
                ));
    }

    private Profile setAccountToProfile(Profile profile, Long idAccount) {

        Account accountById = accountService
                .getAccountById(idAccount);

        checkProfileForAccountIsEmpty(accountById);
        profile.setAccount(accountById);
        return profile;
    }

    private void checkProfileForAccountIsEmpty(Account account) {

        if (account.getProfile() != null) {
            throw new IncorrectDataException("Profile for account already exist");
        }
    }

    @Override
    @Transactional
    public void deleteProfile(Long idAccount) {

        Profile profile = Optional.ofNullable(idAccount)
                .map(this::getProfileByAccountId)
                .orElseThrow(() -> new NoDataFoundException("Profile for your account not found"));
        
        profileRepository.delete(profile);
        logger.loggerInfo(
                "Profile delete for account by id: ",
                idAccount);
    }

    @Override
    public ProfileDto getCurrencyProfile(Long idAuthenticationAccount) {
        return Optional.ofNullable(idAuthenticationAccount)
                .map(this::getProfileByAccountId)
                .map(profileMapper::toProfileDto)
                .orElseThrow(() -> new NoDataFoundException("Profile for your account not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileDto getProfileDtoById(Long idProfile) {

        return Optional.ofNullable(idProfile)
                .flatMap(profileRepository::findById)
                .map(profileMapper::toProfileDto)
                .orElseThrow(() -> new MapperException("Profile by id: " + idProfile + " not found"));

    }

    @Override
    @Transactional(readOnly = true)
    public PagesDtoResponse<Object> getAllProfiles(PagesDto pages) {
        Page<ProfileDto> listProfiles = profileRepository.findAll(pageService.getPage(pages))
                .map(profileMapper::toProfileDto);
        if (listProfiles.isEmpty()) {
            throw new NoDataFoundException("Profiles not found");
        }
        return pageService.getPagesDtoResponse(pages, listProfiles.getContent());
    }

    @Override
    @Transactional
    public ProfileDto updateProfile(Long idAccount, ProfileDto profileDto) {

        ProfileDto profileData = Optional.ofNullable(idAccount)
                .map(this::getProfileByAccountId)
                .map(profile -> changeProfileData(profile, profileDto))
                .map(profileRepository::save)
                .map(profileMapper::toProfileDto)
                .orElseThrow(() -> new MapperException("Error in update profile data"));

        logger.loggerInfo("Profile update for account with id: {}", idAccount);
        return profileData;
    }

    private Profile changeProfileData(Profile oldProfileData, ProfileDto newProfileData) {

        String name = newProfileData.getName();

        if (!name.isBlank() && !name.equals(oldProfileData.getName())) {
            oldProfileData.setName(name);
        }
        Address address = addressProfileService
                .changeAddressData(oldProfileData.getAddress(), newProfileData.getAddress());
        oldProfileData.setAddress(address);

        return oldProfileData;
    }

    @Override
    public Profile getProfileByAccountId(Long id) {
        return Optional.ofNullable(id)
                .map(profileRepository::findProfileByAccountId)
                .orElseThrow(() -> new NoDataFoundException("Current account don't have a profile"));
    }
}