package com.innowise.secret_santa.service.message_services;

import com.innowise.secret_santa.model.dto.request_dto.SentMessageDto;

public interface SentMessagesService {
    void saveSentMessage(SentMessageDto sentMessageDto);
}
