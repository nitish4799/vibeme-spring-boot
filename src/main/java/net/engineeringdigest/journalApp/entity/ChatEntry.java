package net.engineeringdigest.journalApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "chats")
@Getter
@Setter
public class ChatEntry {
    @Id
    private ObjectId chatId;
    private LocalDateTime createdAt;
    @DBRef
    private List<ObjectId> messages;
}
