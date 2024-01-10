package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.exception.IncorrectDataException;
import com.innowise.secret_santa.model.dto.request_dto.RegistrationLoginAccount;
import com.innowise.secret_santa.model.dto.response_dto.AccountAuthenticationResponse;
import com.innowise.secret_santa.service.password_service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountAuthenticationServiceImpl implements AccountAuthenticationService {


    private final AccountEncodingService service;
    private final PasswordService passwordService;

    @Autowired
    public AccountAuthenticationServiceImpl(AccountEncodingService service,
                                            PasswordService passwordService) {
        this.service = service;
        this.passwordService = passwordService;
    }

    public AccountAuthenticationResponse getAuthenticationAccount(RegistrationLoginAccount account) {

        AccountAuthenticationResponse accountAuthentication = Optional.ofNullable(account)
                .map(RegistrationLoginAccount::getEmail)
                .map(service::getAccountAuthByEmail)
                .orElseThrow(() -> new IncorrectDataException("Email is incorrect"));

        passwordService.comparePasswords(account.getPassword(), accountAuthentication.getPassword());


        return accountAuthentication;
    }
}