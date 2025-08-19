package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatsRepository extends MongoRepository<ChatEntry, ObjectId> {
}
