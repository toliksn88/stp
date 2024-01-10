package com.innowise.secret_santa.service.message_services;

import com.innowise.secret_santa.exception.UnknownMessageException;
import com.innowise.secret_santa.model.TypeMessage;
import com.innowise.secret_santa.model.dto.request_dto.SentMessageDto;
import com.innowise.secret_santa.model.mongo.SystemMessage;
import com.innowise.secret_santa.service.email_services.EmailService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SystemMessageServiceImpl implements SystemMessageService {

    private final MongoTemplate mongoTemplate;
    private final SentMessagesService sentMessagesService;
    private final EmailService emailService;
    private static final String KEY = "typeMessage";
    private static final String NAME_COLLECTION = "system_messages";

    public SystemMessageServiceImpl(MongoTemplate mongoTemplate,
                                    SentMessagesService sentMessagesService,
                                    EmailService emailService) {
        this.mongoTemplate = mongoTemplate;
        this.sentMessagesService = sentMessagesService;
        this.emailService = emailService;
    }

    @Override
    public void messageService(TypeMessage typeMessage, Long accountId, String... emails) {

        String systemMessageForEmail;
        if (emails.length == 1) {
            systemMessageForEmail = String.format(getMessage(typeMessage), emails[0]);
        } else {
            systemMessageForEmail = String.format(getMessage(typeMessage), emails[0], emails[1]);
        }
        emailService.sendMail(emails[0], emails[0], systemMessageForEmail);
        saveSentMessage(systemMessageForEmail, accountId);
    }

    private String getMessage(TypeMessage typeMessage) {

        return Optional
                .ofNullable(mongoTemplate.findOne
                        (
                                getQuery(typeMessage.name()),
                                SystemMessage.class,
                                NAME_COLLECTION
                        )
                ).map(SystemMessage::getTextMessage)
                .orElseThrow(() -> new UnknownMessageException("This message is unknown"));
    }

    private Query getQuery(String typeMessage) {

        return new Query((Criteria.where(KEY).is(typeMessage)));
    }


    private void saveSentMessage(String systemMessage, Long id) {
        sentMessagesService.saveSentMessage(SentMessageDto
                .builder()
                .textMessage(systemMessage)
                .account(id)
                .build());
    }
}