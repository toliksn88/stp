package com.innowise.secret_santa.service.role_service;

import com.innowise.secret_santa.model.RoleEnum;
import com.innowise.secret_santa.model.postgres.Role;

public interface RoleService {
    Role giveRoleByRoleName(RoleEnum roleEnum);
}
