package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.exception.IncorrectDataException;
import com.innowise.secret_santa.service.password_service.PasswordService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_AUTHENTICATION_RESPONSE;
import static com.innowise.secret_santa.constants.TestConstants.EMAIL;
import static com.innowise.secret_santa.constants.TestConstants.REGISTRATION_LOGIN_ACCOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;


class AccountAuthenticationServiceImplTest {


    @Mock
    private AccountEncodingService accountEncodingService = mock(AccountEncodingService.class);
    @Mock
    private PasswordService passwordService = mock(PasswordService.class);
    @InjectMocks
    private AccountAuthenticationService accountAuthenticationService =
            new AccountAuthenticationServiceImpl(accountEncodingService, passwordService);


    @Test
    void should_AccountAuthenticationResponse_When_GetAuthenticationAccountSuccessful() {
        given(accountEncodingService.getAccountAuthByEmail(EMAIL)).willReturn(ACCOUNT_AUTHENTICATION_RESPONSE);
        doNothing().when(passwordService).comparePasswords(REGISTRATION_LOGIN_ACCOUNT.getPassword(),
                ACCOUNT_AUTHENTICATION_RESPONSE.getPassword());

        assertEquals(ACCOUNT_AUTHENTICATION_RESPONSE,
                accountAuthenticationService.getAuthenticationAccount(REGISTRATION_LOGIN_ACCOUNT));
    }
    @Test
    void throws_IncorrectDataException_When_GetAuthenticationAccountNotFound(){
        IncorrectDataException ex = assertThrows(IncorrectDataException.class
                , () -> accountAuthenticationService.getAuthenticationAccount(REGISTRATION_LOGIN_ACCOUNT)
                , "Expected getAuthenticationAccount to throw, but it didn't");

        assertTrue(ex.getMessage().contains("Email is incorrect"));
    }
}