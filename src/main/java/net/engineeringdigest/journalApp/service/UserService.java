package net.engineeringdigest.journalApp.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.repository.UsersRepository;
// import org.springframework.web.bind.annotation.RequestBody;

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

    public Optional<UserEntry> findByPhoneNumber(String phoneNumber) {
       return  usersRepository.findByPhoneNumber(phoneNumber);
    }

    public void deleteById(ObjectId id) {
        usersRepository.deleteById(id);
    }

    public void addFriendById(String userPhoneNumber, String friendPhoneNumber, ChatEntry chat){
        Optional<UserEntry> currentUserOptional = findByPhoneNumber(userPhoneNumber);
        Optional<UserEntry> friendUserOptional = findByPhoneNumber(friendPhoneNumber);
        if ( !currentUserOptional.isPresent() ||  !friendUserOptional.isPresent()){
            return;
        }
        UserEntry currentUser = currentUserOptional.get();
        UserEntry friendUser = friendUserOptional.get();
        List<ObjectId> userFriendsList =  currentUser.getFriends();
        List<ObjectId> friendFriendsList =  friendUser.getFriends();
        List<ChatEntry> userChats = currentUser.getChats();
        List<ChatEntry> friendChats = friendUser.getChats();
        userFriendsList.add(friendUser.getUserId());
        friendFriendsList.add(currentUser.getUserId());
        userChats.add(chat);
        friendChats.add(chat);
        currentUser.setFriends(userFriendsList);
        currentUser.setChats(userChats);
        friendUser.setChats(friendChats);
        friendUser.setFriends(friendFriendsList);
        saveEntry(currentUser);
        saveEntry(friendUser);
    }
}
