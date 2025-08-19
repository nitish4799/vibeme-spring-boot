package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.MessageEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessagesRepository extends MongoRepository<MessageEntry, ObjectId> {
}
