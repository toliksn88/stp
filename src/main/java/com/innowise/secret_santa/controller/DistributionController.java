package com.innowise.secret_santa.controller;

import com.innowise.secret_santa.constants_message.Constants;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.DistributionResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.service.distribution_service.DistributionService;
import com.innowise.secret_santa.util.HandleAuthorities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/accounts/profiles/players/distributions")
@Api("Controller distributions")
@Validated
public class DistributionController {

    private final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @GetMapping("/all")
    @ApiOperation("Get all distributions")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<PagesDtoResponse<Object>> getAllDistributions
            (@RequestParam(defaultValue = "5") int size,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(required = false, defaultValue = "timeCreated") String sort) {

        PagesDtoResponse<Object> allDistributions = distributionService.getAllDistributions(
                PagesDto
                        .builder()
                        .sort(sort)
                        .size(size)
                        .page(page)
                        .build());

        return new ResponseEntity<>(allDistributions, HttpStatus.OK);
    }

    @GetMapping("/all/{nameGame}")
    @ApiOperation("Get all distributions for organizer")
    @PreAuthorize("hasPermission('ROLE_ORGANIZER', authentication.principal.authorities) or " +
            "hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<List<DistributionResponseDto>> getAllDistributionsForCurrentOrganizer(
            @PathVariable("nameGame")
            @NotBlank(message = Constants.NOT_NULL_FIELD) String nameGame) {

        List<DistributionResponseDto> distributions = distributionService.getAllDistributionForOrganizerByNameGame(
                HandleAuthorities.getIdAuthenticationAccount(),
                nameGame);

        return new ResponseEntity<>(distributions, HttpStatus.OK);
    }

    @GetMapping("/{nameGame}")
    @ApiOperation("Get distribution for current account by name's game")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DistributionResponseDto> getDistributionCurrentAccountByGameName
            (@PathVariable("nameGame")
             @NotBlank(message = Constants.NOT_NULL_FIELD) String nameGame) {

        DistributionResponseDto distribution = distributionService.getDistributionCurrentAccountByNameGame(
                HandleAuthorities.getIdAuthenticationAccount(),
                nameGame);

        return new ResponseEntity<>(distribution, HttpStatus.OK);
    }

    @GetMapping("currentAll")
    @ApiOperation("Get all distributions for current account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DistributionResponseDto>> getDistributionCurrentAccountByGameName() {

        List<DistributionResponseDto> distributions = distributionService
                .getDistributionsForCurrentAccount(HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(distributions, HttpStatus.OK);
    }
}