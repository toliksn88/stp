package com.innowise.secret_santa.controller;

import com.innowise.secret_santa.model.dto.request_dto.RegistrationLoginAccount;
import com.innowise.secret_santa.model.dto.response_dto.AccountAuthenticationResponse;
import com.innowise.secret_santa.security.JwtToken;
import com.innowise.secret_santa.service.account_services.AccountAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Api("Authentication account")
public class AuthenticationController {
    private final AccountAuthenticationService service;
    private final JwtToken token;

    public AuthenticationController(AccountAuthenticationService service, JwtToken token) {
        this.service = service;
        this.token = token;
    }

    @PostMapping("/login")
    @ApiOperation("Authentication")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<HttpStatus> authenticationAccount(@RequestBody @Valid RegistrationLoginAccount account) {

        HttpHeaders responseHeader = new HttpHeaders();
        AccountAuthenticationResponse authenticationAccount = service.getAuthenticationAccount(account);
        responseHeader.set
                (
                        "Authorization",
                        token.getJWTToken
                                (
                                        authenticationAccount.getEmail(),
                                        authenticationAccount.getRole()
                                )
                );
        return ResponseEntity.ok().headers(responseHeader).body(HttpStatus.OK);
    }

    @GetMapping("/logout")
    @ApiOperation("Logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HttpStatus> logout() {

        SecurityContextHolder.clearContext();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}