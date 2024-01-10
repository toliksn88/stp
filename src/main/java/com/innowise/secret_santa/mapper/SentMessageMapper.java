package com.innowise.secret_santa.mapper;

import com.innowise.secret_santa.model.dto.request_dto.SentMessageDto;
import com.innowise.secret_santa.model.mongo.SentMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SentMessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeMessage", ignore = true)
    SentMessage toSentMessage(SentMessageDto sentMessageDto);
}
