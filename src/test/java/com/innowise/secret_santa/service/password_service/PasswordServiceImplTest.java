package com.innowise.secret_santa.service.password_service;

import com.innowise.secret_santa.exception.IncorrectDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.innowise.secret_santa.constants.TestConstants.ENCODE_PASSWORD;
import static com.innowise.secret_santa.constants.TestConstants.PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PasswordServiceImplTest {

    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private PasswordServiceImpl passwordService;

    @Test
    void should_Compare_Passwords(){
        given(encoder.matches(ENCODE_PASSWORD, ENCODE_PASSWORD)).willReturn(true);

        passwordService.comparePasswords(ENCODE_PASSWORD, ENCODE_PASSWORD);

        then(encoder).should(times(1)).matches(ENCODE_PASSWORD, ENCODE_PASSWORD);
    }
    @Test
    void should_Throw_IncorrectDataException(){
        IncorrectDataException ex = assertThrows(IncorrectDataException.class
                , () -> passwordService.comparePasswords(PASSWORD, ENCODE_PASSWORD)
                , "Expected comparePasswords to throw, but it didn't");

        assertTrue(ex.getMessage().contains("Password incorrect, please write it again"));
    }
    @Test
    void should_Encode_Password(){
        given(encoder.encode(ENCODE_PASSWORD)).willReturn(ENCODE_PASSWORD);

        assertEquals(ENCODE_PASSWORD,passwordService.encodePassword(ENCODE_PASSWORD));

        then(encoder).should(times(1)).encode(ENCODE_PASSWORD);
    }


}