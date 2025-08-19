package net.engineeringdigest.journalApp.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import net.engineeringdigest.journalApp.entity.UserEntry;


public interface UsersRepository extends MongoRepository<UserEntry, ObjectId> {
    Optional<UserEntry> findByPhoneNumber(String phoneNumber);
}
