package net.engineeringdigest.journalApp.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
@Getter
@Setter
public class UserEntry {

    @Id
    private ObjectId userId;
    private String email;
    private String phoneNumber;
    private String password;
    private List<ObjectId> friends;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
