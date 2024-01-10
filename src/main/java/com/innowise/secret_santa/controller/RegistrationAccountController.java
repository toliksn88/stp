package com.innowise.secret_santa.controller;

import com.innowise.secret_santa.model.dto.request_dto.RegistrationLoginAccount;
import com.innowise.secret_santa.service.account_services.AccountRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Api("Registration account")
public class RegistrationAccountController {
    private final AccountRegistrationService service;

    public RegistrationAccountController(AccountRegistrationService service) {
        this.service = service;
    }

    @PostMapping("/registration")
    @ApiOperation("Registration")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<HttpStatus> registrationAccount(@RequestBody @Valid RegistrationLoginAccount account) {
        service.createdAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}