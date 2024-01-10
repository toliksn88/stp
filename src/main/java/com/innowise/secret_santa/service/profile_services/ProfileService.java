package com.innowise.secret_santa.service.profile_services;

import com.innowise.secret_santa.model.dto.ProfileDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;

public interface ProfileService {

    void createdProfile(ProfileDto profile, Long idAccount);

    ProfileDto getProfileDtoById(Long idProfile);

    PagesDtoResponse<Object> getAllProfiles(PagesDto pages);

    ProfileDto updateProfile(Long idProfile, ProfileDto profileDto);

    void deleteProfile(Long idAccount);

    ProfileDto getCurrencyProfile(Long idAuthenticationAccount);
}

