package net.engineeringdigest.journalApp.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    // @DBRef
    private List<ObjectId> chats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
