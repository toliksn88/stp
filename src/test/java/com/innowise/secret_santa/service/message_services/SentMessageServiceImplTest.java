package com.innowise.secret_santa.service.message_services;

import com.innowise.secret_santa.mapper.SentMessageMapper;
import com.innowise.secret_santa.service.logger_services.LoggerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_SENT_MESSAGE;
import static com.innowise.secret_santa.constants.TestConstants.NAME_COLLECTION;
import static com.innowise.secret_santa.constants.TestConstants.SENT_MESSAGE;
import static com.innowise.secret_santa.constants.TestConstants.SENT_MESSAGE_DTO;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SentMessageServiceImplTest {

    @Mock
    private SentMessageMapper sentMessageMapper;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private LoggerService<Long> logger;
    @InjectMocks
    private SentMessageServiceImpl sentMessageService;

    @Test
    void should_Save_SentMessage() {
        given(sentMessageMapper.toSentMessage(SENT_MESSAGE_DTO)).willReturn(SENT_MESSAGE);
        given(mongoTemplate.save(SENT_MESSAGE, NAME_COLLECTION)).willReturn(SENT_MESSAGE);
        doNothing().when(logger).loggerInfo(LOGGER_MESSAGE_SENT_MESSAGE, ID);

        sentMessageService.saveSentMessage(SENT_MESSAGE_DTO);

        then(sentMessageMapper).should(times(1)).toSentMessage(SENT_MESSAGE_DTO);
        then(mongoTemplate).should(times(1)).save(SENT_MESSAGE, NAME_COLLECTION);
        verify(logger, times(1)).loggerInfo(LOGGER_MESSAGE_SENT_MESSAGE, ID);

    }


}