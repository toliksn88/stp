package com.innowise.secret_santa.mongodb.db.changelog;

import com.innowise.secret_santa.model.mongo.SentMessage;
import com.innowise.secret_santa.model.mongo.SystemMessage;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "1", order = "1", author = "Mikita", transactional = false)
public class CreateCollection {

    private final MongoTemplate mongoTemplate;

    public CreateCollection(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Execution
    public void createCollections() {

        mongoTemplate.createCollection(SystemMessage.class);
        mongoTemplate.createCollection(SentMessage.class);
    }


    @RollbackExecution
    public void rollback() {
        mongoTemplate.dropCollection(SystemMessage.class);
        mongoTemplate.dropCollection(SentMessage.class);

    }
}