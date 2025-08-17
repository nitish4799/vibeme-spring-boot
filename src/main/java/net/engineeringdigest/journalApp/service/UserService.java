package net.engineeringdigest.journalApp.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.repository.UsersRepository;

@Component
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public UserEntry saveEntry(UserEntry entry) {
        return usersRepository.save(entry);
    }

    public List<UserEntry> getAllEntries() {
        return usersRepository.findAll();
    }

    public Optional<UserEntry> getById(ObjectId id) {
        return usersRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        usersRepository.deleteById(id);
    }

    public void addFriendById(ObjectId userId, ObjectId friendId){
        UserEntry currentUser = getById(userId).orElse(null);
        UserEntry friendUser = getById(friendId).orElse(null);
        List<ObjectId> friends1 =  currentUser.getFriends();
        List<ObjectId> friends2 =  friendUser.getFriends();
        friends1.add(friendId);
        friends2.add(userId);
        saveEntry(currentUser);
        saveEntry(friendUser);
    }
}
