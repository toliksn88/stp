package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.model.postgres.Account;

public interface AccountProfileService{

    Account getAccountById(Long id);
}
