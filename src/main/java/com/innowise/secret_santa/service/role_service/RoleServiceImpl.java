package com.innowise.secret_santa.service.role_service;

import com.innowise.secret_santa.exception.NoDataFoundException;
import com.innowise.secret_santa.model.RoleEnum;
import com.innowise.secret_santa.model.postgres.Role;
import com.innowise.secret_santa.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role giveRoleByRoleName(RoleEnum roleEnum) {
        return Optional.ofNullable(roleEnum)
                .map(roleRepository::findRoleByRoleName)
                .orElseThrow(() -> new NoDataFoundException("Role: " + roleEnum + " not found"));
    }
}
