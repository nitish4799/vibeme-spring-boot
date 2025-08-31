package net.engineeringdigest.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import net.engineeringdigest.journalApp.entity.UserEntry;


public interface UsersRepository extends MongoRepository<UserEntry, ObjectId> {
    UserEntry findByPhoneNumber(String phoneNumber);

    UserEntry findByEmail(String email);
}
