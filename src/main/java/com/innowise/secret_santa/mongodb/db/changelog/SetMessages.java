package com.innowise.secret_santa.mongodb.db.changelog;

import com.innowise.secret_santa.model.TypeMessage;
import com.innowise.secret_santa.model.mongo.SystemMessage;
import com.mongodb.client.MongoCollection;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@ChangeUnit(id = "2", order = "2", author = "Mikita", transactional = false)
public class SetMessages {

    private final MongoTemplate mongoTemplate;
    private static final String TEXT_MESSAGE = "textMessage";
    private static final String TYPE_MESSAGE = "typeMessage";

    public SetMessages(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Execution
    public void setMessage() {
        MongoCollection<Document> messages = mongoTemplate.getCollection("system_messages");
        Document created = new Document()
                .append(TEXT_MESSAGE, TypeMessage.CREATE.getTextMessage())
                .append(TYPE_MESSAGE, TypeMessage.CREATE.name());
        Document changed = new Document()
                .append(TEXT_MESSAGE, TypeMessage.CHANGE_PASSWORD.getTextMessage())
                .append(TYPE_MESSAGE, TypeMessage.CHANGE_PASSWORD.name());
        Document delete = new Document()
                .append(TEXT_MESSAGE, TypeMessage.DELETE.getTextMessage())
                .append(TYPE_MESSAGE, TypeMessage.DELETE.name());
        Document distribution = new Document()
                .append(TEXT_MESSAGE, TypeMessage.DISTRIBUTION.getTextMessage())
                .append(TYPE_MESSAGE, TypeMessage.DISTRIBUTION.name());
        messages.insertMany(List.of(created, changed, delete, distribution));
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.dropCollection(SystemMessage.class);
    }
}