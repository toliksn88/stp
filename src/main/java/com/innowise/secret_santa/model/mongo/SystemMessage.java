package com.innowise.secret_santa.model.mongo;

import com.innowise.secret_santa.model.TypeMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "system_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SystemMessage implements Serializable {

    @Id
    private String id;

    @Field
    private String textMessage;

    @Field
    private TypeMessage typeMessage;


}
