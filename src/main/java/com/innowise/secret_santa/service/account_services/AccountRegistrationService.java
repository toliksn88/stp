package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.model.dto.request_dto.RegistrationLoginAccount;

public interface AccountRegistrationService {

    void createdAccount(RegistrationLoginAccount account);

}
