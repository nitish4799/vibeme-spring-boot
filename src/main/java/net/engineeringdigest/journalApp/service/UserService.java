package net.engineeringdigest.journalApp.service;

import java.util.HashMap;
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
        return usersRepository.findByPhoneNumber(phoneNumber);
    }

    public void deleteById(ObjectId id) {
        usersRepository.deleteById(id);
    }

    public void addFriendById(String userPhoneNumber, String friendPhoneNumber, ChatEntry chat) {
        Optional<UserEntry> currentUserOptional = findByPhoneNumber(userPhoneNumber);
        Optional<UserEntry> friendUserOptional = findByPhoneNumber(friendPhoneNumber);
        if (!currentUserOptional.isPresent() || !friendUserOptional.isPresent()) {
            return;
        }
        UserEntry currentUser = currentUserOptional.get();
        UserEntry friendUser = friendUserOptional.get();
        List<ObjectId> userFriendsList = currentUser.getFriends();
        List<ObjectId> friendFriendsList = friendUser.getFriends();
        List<ChatEntry> userChats = currentUser.getChats();
        List<ChatEntry> friendChats = friendUser.getChats();
        if (!userFriendsList.contains(friendUser.getUserId())) {
            userFriendsList.add(friendUser.getUserId());
        }
        if (!friendFriendsList.contains(currentUser.getUserId())) {
            friendFriendsList.add(currentUser.getUserId());
        }
        if (!userChats.contains(chat)) {
            userChats.add(chat);
        }
        if (!friendChats.contains(chat)) {
            friendChats.add(chat);
        }
        currentUser.setFriends(userFriendsList);
        friendUser.setFriends(friendFriendsList);
        currentUser.setChats(userChats);
        friendUser.setChats(friendChats);
        if (currentUser.getFriendChatMap() == null) {
            currentUser.setFriendChatMap(new HashMap<>());
        }
        if (friendUser.getFriendChatMap() == null) {
            friendUser.setFriendChatMap(new HashMap<>());
        }
        // Put the mappings friendId -> chatId
        currentUser.getFriendChatMap().put(friendUser.getUserId(), chat.getChatId());
        friendUser.getFriendChatMap().put(currentUser.getUserId(), chat.getChatId());
        saveEntry(currentUser);
        saveEntry(friendUser);
    }
}
