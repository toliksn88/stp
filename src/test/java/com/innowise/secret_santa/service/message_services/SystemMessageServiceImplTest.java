package com.innowise.secret_santa.service.message_services;

import com.innowise.secret_santa.service.email_services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.innowise.secret_santa.constants.TestConstants.EMAIL;
import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.NAME_COLLECTION_SYSTEM_MESSAGES;
import static com.innowise.secret_santa.constants.TestConstants.QUERY;
import static com.innowise.secret_santa.constants.TestConstants.SENT_MESSAGE_DTO;
import static com.innowise.secret_santa.constants.TestConstants.SYSTEM_MESSAGE;
import static com.innowise.secret_santa.constants.TestConstants.SYSTEM_MESSAGE_CLASS;
import static com.innowise.secret_santa.constants.TestConstants.TEXT_MESSAGE;
import static com.innowise.secret_santa.constants.TestConstants.TYPE_MESSAGE_CREATE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SystemMessageServiceImplTest {

    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private SentMessagesService sentMessagesService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private SystemMessageServiceImpl systemMessageService;

    @Test
    void should_Save_Or_Get_Message_In_MongoDB(){
        given(mongoTemplate.findOne(QUERY, SYSTEM_MESSAGE_CLASS, NAME_COLLECTION_SYSTEM_MESSAGES)).willReturn(SYSTEM_MESSAGE);
        doNothing().when(emailService).sendMail(EMAIL,EMAIL,TEXT_MESSAGE);
        doNothing().when(sentMessagesService).saveSentMessage(SENT_MESSAGE_DTO);

        systemMessageService.messageService(TYPE_MESSAGE_CREATE, ID, EMAIL);

        then(mongoTemplate).should(times(1)).findOne(QUERY, SYSTEM_MESSAGE_CLASS, NAME_COLLECTION_SYSTEM_MESSAGES);
        verify(emailService, times(1)).sendMail(EMAIL,EMAIL,TEXT_MESSAGE);
        verify(sentMessagesService, times(1)).saveSentMessage(SENT_MESSAGE_DTO);

    }
    @Test
    void should_Save_Or_Get_Message_In_MongoDB_With_Two_Emails(){
        given(mongoTemplate.findOne(QUERY, SYSTEM_MESSAGE_CLASS, NAME_COLLECTION_SYSTEM_MESSAGES)).willReturn(SYSTEM_MESSAGE);
        doNothing().when(emailService).sendMail(EMAIL,EMAIL,TEXT_MESSAGE);
        doNothing().when(sentMessagesService).saveSentMessage(SENT_MESSAGE_DTO);

        systemMessageService.messageService(TYPE_MESSAGE_CREATE, ID, EMAIL, EMAIL);

        then(mongoTemplate).should(times(1)).findOne(QUERY, SYSTEM_MESSAGE_CLASS, NAME_COLLECTION_SYSTEM_MESSAGES);
        verify(emailService, times(1)).sendMail(EMAIL,EMAIL,TEXT_MESSAGE);
        verify(sentMessagesService, times(1)).saveSentMessage(SENT_MESSAGE_DTO);

    }

}