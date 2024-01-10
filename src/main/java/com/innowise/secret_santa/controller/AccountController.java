package com.innowise.secret_santa.controller;

import com.innowise.secret_santa.constants_message.Constants;
import com.innowise.secret_santa.exception.IncorrectDataException;
import com.innowise.secret_santa.model.dto.AccountDto;
import com.innowise.secret_santa.model.dto.request_dto.AccountChangePassword;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.service.account_services.AccountService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/accounts")
@Api("Account Rest Controller")
@Validated
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    @ApiOperation("Get account by id")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("id")
                                                 @Positive(message = Constants.NOT_NEGATIVE_ID)
                                                 Long id) {

        AccountDto userById = accountService.getAccountDtoById(id);

        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @GetMapping("")
    @ApiOperation("Get currency account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDto> getCurrencyAccount() {

        AccountDto currencyAccount = accountService.getAccountDtoById(HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(currencyAccount, HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation("Delete account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HttpStatus> deleteAccount() {

        accountService.deleteAccount(HandleAuthorities.getIdAuthenticationAccount());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping
    @ApiOperation("Change account's password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDto> changePassword(@RequestBody @Valid AccountChangePassword accountChangePassword) {
        checkNewPasswords(accountChangePassword.getNewPassword(), accountChangePassword.getNewPassword2());
        AccountDto account = accountService.changePasswordAccount(HandleAuthorities.getIdAuthenticationAccount(),
                accountChangePassword);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    private void checkNewPasswords(String password1, String password2) {
        if (!password1.equals(password2)) {
            throw new IncorrectDataException("Passwords do not match");
        }
    }

    @GetMapping("/all")
    @ApiOperation("Read all accounts")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<PagesDtoResponse<Object>> getAllAccounts
            (@RequestParam(defaultValue = "5") int size,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(required = false, defaultValue = "email") String sort) {

        PagesDtoResponse<Object> allAccounts = accountService.getAllAccounts(
                PagesDto
                        .builder()
                        .sort(sort)
                        .size(size)
                        .page(page)
                        .build());

        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }
}