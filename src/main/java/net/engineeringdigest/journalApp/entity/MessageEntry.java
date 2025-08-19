package net.engineeringdigest.journalApp.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Getter
@Setter
public class MessageEntry {
    @Id
    private ObjectId messageId;
    @NonNull
    private ObjectId sender;
    @NonNull
    private String message;
}
