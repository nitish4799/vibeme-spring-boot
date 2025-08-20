package net.engineeringdigest.journalApp.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Document(collection = "messages")
@Getter
@Setter
public class MessageEntry {
    @Id
    private ObjectId messageId;
    // @NonNull
    // private ObjectId sender;
    @NonNull
    private String message;
}
