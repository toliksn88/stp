package com.innowise.secret_santa.service.role_service;

import com.innowise.secret_santa.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.innowise.secret_santa.constants.TestConstants.ROLE_USER;
import static com.innowise.secret_santa.constants.TestConstants.ROLE_ENUM_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void should_Get_Role_By_RoleEna() {
        given(roleRepository.findRoleByRoleName(ROLE_ENUM_USER)).willReturn(ROLE_USER);

        assertEquals(ROLE_USER, roleService.giveRoleByRoleName(ROLE_ENUM_USER));

        then(roleRepository).should(times(1)).findRoleByRoleName(ROLE_ENUM_USER);
    }

}