package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.mapper.AccountMapper;
import com.innowise.secret_santa.model.dto.AccountDto;
import com.innowise.secret_santa.repository.AccountRepository;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.message_services.SystemMessageService;
import com.innowise.secret_santa.service.page_services.PageService;
import com.innowise.secret_santa.service.password_service.PasswordService;
import com.innowise.secret_santa.service.role_service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.innowise.secret_santa.constants.TestConstants.ACCOUNTS_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_AUTHENTICATION_RESPONSE;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_CHANGE_PASSWORD;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_WITH_PROFILE;
import static com.innowise.secret_santa.constants.TestConstants.EMAIL;
import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_CHANGE_PASSWORD_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_CHANGE_ROLE_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_CREATE_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_DELETE_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.OLD_PASSWORD;
import static com.innowise.secret_santa.constants.TestConstants.PAGEABLE;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_ACCOUNT_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.PASSWORD;
import static com.innowise.secret_santa.constants.TestConstants.REGISTRATION_LOGIN_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.ROLE_USER;
import static com.innowise.secret_santa.constants.TestConstants.ROLE_ENUM_USER;
import static com.innowise.secret_santa.constants.TestConstants.SETTING_ROLES_ENUM_ADD;
import static com.innowise.secret_santa.constants.TestConstants.TYPE_MESSAGE_CHANGE;
import static com.innowise.secret_santa.constants.TestConstants.TYPE_MESSAGE_CREATE;
import static com.innowise.secret_santa.constants.TestConstants.TYPE_MESSAGE_DELETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private LoggerService<Long> logger;
    @Mock
    private SystemMessageService messageService;
    @Mock
    private PasswordService passwordService;
    @Mock
    private PageService<AccountDto> pageService;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void should_Save_Account_In_DataBase_If_Successful() {

        given(accountRepository.existsByEmail(EMAIL)).willReturn(false);
        given(accountMapper.toAccount(REGISTRATION_LOGIN_ACCOUNT)).willReturn(ACCOUNT_WITH_PROFILE);
        given(roleService.giveRoleByRoleName(ROLE_ENUM_USER)).willReturn(ROLE_USER);
        given(accountRepository.save(ACCOUNT_WITH_PROFILE)).willReturn(ACCOUNT_WITH_PROFILE);
        doNothing().when(messageService).messageService(TYPE_MESSAGE_CREATE, ID, EMAIL);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_CREATE_ACCOUNT, EMAIL);

        accountService.createdAccount(REGISTRATION_LOGIN_ACCOUNT);

        then(accountRepository).should(times(1)).existsByEmail(EMAIL);
        then(accountMapper).should(times(1)).toAccount(REGISTRATION_LOGIN_ACCOUNT);
        then(roleService).should(times(1)).giveRoleByRoleName(ROLE_ENUM_USER);
        then(accountRepository).should(times(1)).save(ACCOUNT_WITH_PROFILE);
        verify(messageService, times(1)).messageService(TYPE_MESSAGE_CREATE, ID, EMAIL);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_CREATE_ACCOUNT, EMAIL);
    }

    @Test
    void should_Get_AccountAuthenticationResponse_By_AccountEmail() {
        given(accountRepository.findAccountByEmail(EMAIL)).willReturn(ACCOUNT_WITH_PROFILE);
        given(accountMapper.toAccountAuthenticationResponse(ACCOUNT_WITH_PROFILE)).willReturn(ACCOUNT_AUTHENTICATION_RESPONSE);

        assertEquals(ACCOUNT_AUTHENTICATION_RESPONSE, accountService.getAccountAuthByEmail(EMAIL));

        then(accountRepository).should(times(1)).findAccountByEmail(EMAIL);
        then(accountMapper).should(times(1)).toAccountAuthenticationResponse(ACCOUNT_WITH_PROFILE);
    }

    @Test
    void should_Delete_Account_By_Id() {
        given(accountRepository.findById(ID)).willReturn(Optional.of(ACCOUNT_WITH_PROFILE));
        doNothing().when(accountRepository).delete(ACCOUNT_WITH_PROFILE);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_DELETE_ACCOUNT, ID);
        doNothing().when(messageService).messageService(TYPE_MESSAGE_DELETE, ID, EMAIL);

        accountService.deleteAccount(ID);

        then(accountRepository).should(times(1)).findById(ID);
        verify(accountRepository, times(1)).delete(ACCOUNT_WITH_PROFILE);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_DELETE_ACCOUNT, ID);
        verify(messageService, times(1)).messageService(TYPE_MESSAGE_DELETE, ID, EMAIL);

    }

    @Test
    void should_Get_AccountDto_By_AccountId() {
        given(accountRepository.findById(ID)).willReturn(Optional.of(ACCOUNT_WITH_PROFILE));
        given(accountMapper.toAccountDto(ACCOUNT_WITH_PROFILE)).willReturn(ACCOUNT_DTO);

        assertEquals(ACCOUNT_DTO, accountService.getAccountDtoById(ID));

        then(accountRepository).should(times(1)).findById(ID);
        then(accountMapper).should(times(1)).toAccountDto(ACCOUNT_WITH_PROFILE);
    }

    @Test
    void should_Change_Password_Account_By_AccountChangePassword() {
        given(accountRepository.findById(ID)).willReturn(Optional.of(ACCOUNT_WITH_PROFILE));
        doNothing().when(passwordService).comparePasswords(OLD_PASSWORD, PASSWORD);
        given(accountRepository.save(ACCOUNT_WITH_PROFILE)).willReturn(ACCOUNT_WITH_PROFILE);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_CHANGE_PASSWORD_ACCOUNT, EMAIL);
        doNothing().when(messageService).messageService(TYPE_MESSAGE_CHANGE, ID, EMAIL);
        given(accountMapper.toAccountDto(ACCOUNT_WITH_PROFILE)).willReturn(ACCOUNT_DTO);

        assertEquals(ACCOUNT_DTO, accountService.changePasswordAccount(ID, ACCOUNT_CHANGE_PASSWORD));

        then(accountRepository).should(times(1)).findById(ID);
        verify(passwordService, times(1)).comparePasswords(OLD_PASSWORD, PASSWORD);
        then(accountRepository).should(times(1)).save(ACCOUNT_WITH_PROFILE);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_CHANGE_PASSWORD_ACCOUNT, EMAIL);
        verify(messageService, times(1)).messageService(TYPE_MESSAGE_CHANGE, ID, EMAIL);
        then(accountMapper).should(times(1)).toAccountDto(ACCOUNT_WITH_PROFILE);
    }

    @Test
    void should_Add_Or_Delete_Role_From_Account() {
        given(accountRepository.findById(ID)).willReturn(Optional.of(ACCOUNT_WITH_PROFILE));
        given(accountRepository.save(ACCOUNT_WITH_PROFILE)).willReturn(ACCOUNT_WITH_PROFILE);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_CHANGE_ROLE_ACCOUNT, ID, ROLE_ENUM_USER.getRole());

        accountService.addOrDeleteRoleToAccount(ID, ROLE_ENUM_USER, SETTING_ROLES_ENUM_ADD);

        then(accountRepository).should(times(1)).findById(ID);
        then(accountRepository).should(times(1)).save(ACCOUNT_WITH_PROFILE);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_CHANGE_ROLE_ACCOUNT, ID, ROLE_ENUM_USER.getRole());
    }

    @Test
    void should_Get_PageDtoResponse_With_All_Accounts(){
        given(pageService.getPage(PAGES_DTO)).willReturn(PAGEABLE);
        given(accountRepository.findAll(PAGEABLE)).willReturn(PAGE_ACCOUNT);
        given(accountMapper.toAccountDto(ACCOUNT_WITH_PROFILE)).willReturn(ACCOUNT_DTO);
        given(pageService.getPagesDtoResponse(PAGES_DTO, ACCOUNTS_DTO)).willReturn(PAGES_DTO_RESPONSE_ACCOUNT_DTO);

        assertEquals(PAGES_DTO_RESPONSE_ACCOUNT_DTO, accountService.getAllAccounts(PAGES_DTO));

        then(pageService).should(times(1)).getPage(PAGES_DTO);
        then(accountRepository).should(times(1)).findAll(PAGEABLE);
        then(accountMapper).should(times(1)).toAccountDto(ACCOUNT_WITH_PROFILE);
        then(pageService).should(times(1)).getPagesDtoResponse(PAGES_DTO, ACCOUNTS_DTO);
    }


}