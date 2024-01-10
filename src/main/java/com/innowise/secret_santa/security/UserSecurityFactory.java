package com.innowise.secret_santa.security;

import com.innowise.secret_santa.model.dto.RoleDto;
import com.innowise.secret_santa.model.dto.response_dto.AccountAuthenticationResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public final class UserSecurityFactory {

    private UserSecurityFactory() {
    }

    public static UserSecurity addUserSecurity(AccountAuthenticationResponse account) {

        return new UserSecurity(account.getId(),
                account.getEmail(),
                account.getPassword(),
                mapToAuthority(account.getRole()));
    }

    private static List<GrantedAuthority> mapToAuthority(List<RoleDto> role) {

        List<GrantedAuthority> listRole = new ArrayList<>();

        for (RoleDto roleDto : role) {
            listRole.add(new SimpleGrantedAuthority(roleDto.getRoleName().getRole()));
        }

        return listRole;
    }
}