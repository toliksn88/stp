package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.exception.IncorrectDataException;
import com.innowise.secret_santa.exception.MapperException;
import com.innowise.secret_santa.exception.NoDataFoundException;
import com.innowise.secret_santa.exception.SaveDataException;
import com.innowise.secret_santa.mapper.AccountMapper;
import com.innowise.secret_santa.model.RoleEnum;
import com.innowise.secret_santa.model.SettingRolesEnum;
import com.innowise.secret_santa.model.TypeMessage;
import com.innowise.secret_santa.model.dto.AccountDto;
import com.innowise.secret_santa.model.dto.request_dto.AccountChangePassword;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.request_dto.RegistrationLoginAccount;
import com.innowise.secret_santa.model.dto.response_dto.AccountAuthenticationResponse;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.model.postgres.Account;
import com.innowise.secret_santa.model.postgres.Role;
import com.innowise.secret_santa.repository.AccountRepository;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import com.innowise.secret_santa.service.message_services.SystemMessageService;
import com.innowise.secret_santa.service.page_services.PageService;
import com.innowise.secret_santa.service.password_service.PasswordService;
import com.innowise.secret_santa.service.role_service.RoleService;
import com.innowise.secret_santa.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AccountServiceImpl implements AccountService,
        AccountProfileService,
        AccountEncodingService,
        AccountRegistrationService,
        AccountRoleService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final AccountMapper accountMapper;
    private final PageService<AccountDto> pageService;
    private final LoggerService<Long> logger;
    private final SystemMessageService messageService;
    private final PasswordService passwordService;


    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              RoleService roleService,
                              AccountMapper accountMapper,
                              PageService<AccountDto> pageService,
                              LoggerService<Long> logger,
                              SystemMessageService messageService,
                              PasswordService passwordService) {
        this.accountRepository = accountRepository;
        this.roleService = roleService;
        this.accountMapper = accountMapper;
        this.pageService = pageService;
        this.logger = logger;
        this.messageService = messageService;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public void createdAccount(RegistrationLoginAccount account) {

        checkEmail(account.getEmail());
        Account present = Optional.of(account)
                .map(accountMapper::toAccount)
                .map(this::encodePasswordInAccount)
                .map(this::addRoleForAccount)
                .map(this::addDateCreatedForAccount)
                .map(accountRepository::save)
                .orElseThrow(() -> new SaveDataException("failed to created Account"));

        messageService.messageService(TypeMessage.CREATE, present.getId(), present.getEmail());
        logger.loggerInfo("Account by email {} successful registration", present.getEmail());
    }

    private void checkEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new IncorrectDataException("Email " + email + " already use");
        }
    }

    private Account encodePasswordInAccount(Account account) {
        account.setPassword(passwordService.encodePassword(account.getPassword()));
        return account;
    }

    private Account addRoleForAccount(Account account) {
        account.setRole(List.of(roleService.giveRoleByRoleName(RoleEnum.ROLE_USER)));
        return account;
    }

    private Account addDateCreatedForAccount(Account account) {
        account.setDateCreated(CalendarUtils.getFormatDate(LocalDateTime.now()));
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountAuthenticationResponse getAccountAuthByEmail(String email) {
        return Optional.ofNullable(email)
                .map(accountRepository::findAccountByEmail)
                .map(accountMapper::toAccountAuthenticationResponse)
                .orElseThrow(() -> new IncorrectDataException("Email by name '" + email + "' incorrect"));
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {

        Account account = Optional.of(id)
                .map(this::getAccountById)
                .orElseThrow(() -> new NoDataFoundException("Account by id '" + id + "' not found"));

        accountRepository.delete(account);
        logger.loggerInfo("Account by id {} delete", account.getId());
        messageService.messageService(TypeMessage.DELETE, id, account.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountDtoById(Long id) {

        return Optional.ofNullable(id)
                .map(this::getAccountById)
                .map(accountMapper::toAccountDto)
                .orElseThrow(() -> new MapperException("Error mapping from Account to AccountDto"));
    }

    @Override
    public Account getAccountById(Long id) {

        return Optional.ofNullable(id)
                .flatMap(accountRepository::findById)
                .orElseThrow(() -> new NoDataFoundException("Account by id '" + id + "' not found"));
    }

    @Override
    @Transactional
    public AccountDto changePasswordAccount(Long id, AccountChangePassword account) {

        Account accountById = Optional.ofNullable(id)
                .map(this::getAccountById)
                .orElseThrow(() -> new NoDataFoundException("Not found data for change password account"));

        passwordService.comparePasswords(account.getOldPassword(), accountById.getPassword());
        accountById.setPassword(account.getNewPassword());
        accountRepository.save(encodePasswordInAccount(accountById));
        logger.loggerInfo("Account {} changed password", accountById.getEmail());
        messageService.messageService(TypeMessage.CHANGE_PASSWORD, accountById.getId(), accountById.getEmail());

        return accountMapper.toAccountDto(accountById);
    }

    @Override
    @Transactional(readOnly = true)
    public PagesDtoResponse<Object> getAllAccounts(PagesDto pages) {
        Page<AccountDto> listAccount = accountRepository.findAll(pageService.getPage(pages))
                .map(accountMapper::toAccountDto);
        if (listAccount.isEmpty()) {
            throw new NoDataFoundException("Accounts not found");
        }
        return pageService.getPagesDtoResponse(pages, listAccount.getContent());
    }

    @Override
    public void addOrDeleteRoleToAccount(Long id, RoleEnum role, SettingRolesEnum flag) {

        Account acc = Optional.ofNullable(id)
                .map(this::getAccountById)
                .map(account -> addRoleOrDeleteRoleForAccount(account, role, flag))
                .map(accountRepository::save)
                .orElseThrow(() -> new NoDataFoundException("Filed to change role for account by id: " + id));

        logger.loggerInfo("Account by id {0} set role - {1}", acc.getId(), role.getRole());

    }

    private Account addRoleOrDeleteRoleForAccount(Account account, RoleEnum roleEnum, SettingRolesEnum flag) {

        List<Role> role = account.getRole();

        if (flag.equals(SettingRolesEnum.ADD)) {
            addNewRoleToAccount(role, roleEnum);
        }
        if (flag.equals(SettingRolesEnum.DELETE)) {
            deleteOldRoleFromAccount(role, roleEnum);
        }
        account.setRole(role);
        return account;
    }

    private void addNewRoleToAccount(List<Role> roles, RoleEnum roleEnum) {
        if (roles.stream().noneMatch(role -> role.getRoleName().equals(roleEnum))) {
            roles.add(roleService.giveRoleByRoleName(roleEnum));
        }
    }

    private void deleteOldRoleFromAccount(List<Role> roles, RoleEnum roleEnum) {
        roles.removeIf(role -> role.getRoleName().equals(roleEnum));
    }
}