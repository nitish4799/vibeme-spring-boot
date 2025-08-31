package net.engineeringdigest.journalApp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Document(collection="users")
@Getter
@Setter
public class UserEntry {
    @Id
    private ObjectId userId;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique=true)
    @NonNull
    private String phoneNumber;
    @NonNull
    private String password;
    private List<ObjectId> friends;
    private Map<ObjectId, ObjectId> friendChatMap = new HashMap<>();
    @DBRef
    private List<ChatEntry> chats = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> roles = new ArrayList<>();
}
