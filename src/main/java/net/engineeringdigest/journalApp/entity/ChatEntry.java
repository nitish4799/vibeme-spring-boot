package net.engineeringdigest.journalApp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "chats")
@Getter
@Setter
public class ChatEntry {
    @Id
    private ObjectId chatId;
    private LocalDateTime createdAt;
    @DBRef
    private List<MessageEntry> messages = new ArrayList<>();
}
