package com.innowise.secret_santa.controller;

import com.innowise.secret_santa.constants_message.Constants;
import com.innowise.secret_santa.model.dto.ProfileDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.service.profile_services.ProfileService;
import com.innowise.secret_santa.util.HandleAuthorities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/accounts/profiles")
@Api("Profile Rest Controller")
@Validated
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{idProfile}")
    @ApiOperation("Get profile by id")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable("idProfile")
                                                     @Positive(message = Constants.NOT_NEGATIVE_ID)
                                                     Long idProfile) {
        ProfileDto profileDtoByAccount = profileService.getProfileDtoById(idProfile);

        return new ResponseEntity<>(profileDtoByAccount, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("Get currency profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileDto> getCurrencyProfile() {
        ProfileDto profile = profileService.getCurrencyProfile(HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation("save a profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HttpStatus> createProfile(@RequestBody @Valid ProfileDto profile) {
        profileService.createdProfile(profile, HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @ApiOperation("delete account by id")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HttpStatus> deleteProfile() {
        profileService.deleteProfile(HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping
    @ApiOperation("Change profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileDto> changeProfile(@RequestBody @Valid ProfileDto newProfileData) {
        ProfileDto profile = profileService.updateProfile(HandleAuthorities.getIdAuthenticationAccount(),
                newProfileData);

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation("Getting all profiles")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<PagesDtoResponse<Object>> getAllProfiles
            (@RequestParam(defaultValue = "5") int size,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(required = false, defaultValue = "name") String sort) {
        PagesDtoResponse<Object> allProfiles = profileService.getAllProfiles(
                PagesDto
                        .builder()
                        .sort(sort)
                        .size(size)
                        .page(page)
                        .build());

        return new ResponseEntity<>(allProfiles, HttpStatus.OK);
    }
}