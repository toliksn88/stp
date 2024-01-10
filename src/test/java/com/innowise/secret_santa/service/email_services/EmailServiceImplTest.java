package com.innowise.secret_santa.service.email_services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static com.innowise.secret_santa.constants.TestConstants.EMAIL;
import static com.innowise.secret_santa.constants.TestConstants.MAIL_MESSAGE;
import static com.innowise.secret_santa.constants.TestConstants.TEXT_MESSAGE;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender sender;
    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void should_Send_Email() {
        doNothing().when(sender).send(MAIL_MESSAGE);

        emailService.sendMail(EMAIL, EMAIL, TEXT_MESSAGE);

        verify(sender, times(1)).send(MAIL_MESSAGE);
    }
}