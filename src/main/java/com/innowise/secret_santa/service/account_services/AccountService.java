package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.model.dto.AccountDto;
import com.innowise.secret_santa.model.dto.request_dto.AccountChangePassword;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;

public interface AccountService {
    void deleteAccount(Long id);

    AccountDto getAccountDtoById(Long id);

    AccountDto changePasswordAccount(Long id, AccountChangePassword account);

    PagesDtoResponse<Object> getAllAccounts(PagesDto pages);
}