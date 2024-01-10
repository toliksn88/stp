package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.model.dto.response_dto.AccountAuthenticationResponse;

public interface AccountEncodingService {

    AccountAuthenticationResponse getAccountAuthByEmail(String email);
}