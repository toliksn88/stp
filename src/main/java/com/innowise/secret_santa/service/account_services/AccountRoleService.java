package com.innowise.secret_santa.service.account_services;

import com.innowise.secret_santa.model.RoleEnum;
import com.innowise.secret_santa.model.SettingRolesEnum;

public interface AccountRoleService {

    void addOrDeleteRoleToAccount(Long id, RoleEnum roleEnum, SettingRolesEnum settingRolesEnum);
}
