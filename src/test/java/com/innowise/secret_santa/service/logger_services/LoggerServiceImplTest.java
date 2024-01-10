package com.innowise.secret_santa.service.logger_services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.LOGGER_MESSAGE_CREATED_PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoggerServiceImplTest {

    @Mock
    private Logger logger;
    @InjectMocks
    private LoggerServiceImpl<Long> loggerService;

    @Test
    void should_Logger_All_Info_Return_Object() {
        doNothing().when(logger).info(LOGGER_MESSAGE_CREATED_PLAYER, ID);

        assertEquals(ID, loggerService.logger(LOGGER_MESSAGE_CREATED_PLAYER, ID));

        verify(logger, times(1)).info(LOGGER_MESSAGE_CREATED_PLAYER, ID);
    }
}