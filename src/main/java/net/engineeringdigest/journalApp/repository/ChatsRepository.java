package net.engineeringdigest.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import net.engineeringdigest.journalApp.entity.ChatEntry;

public interface ChatsRepository extends MongoRepository<ChatEntry, ObjectId> {
}
