package net.engineeringdigest.journalApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.repository.UsersRepository;
// import org.springframework.web.bind.annotation.RequestBody;

@Component
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ChatService chatService;

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntry saveEntry(UserEntry entry) {
        return usersRepository.save(entry);
    }

    public UserEntry saveNewEntry(UserEntry entry) {
        entry.setPassword(passwordEncoder.encode(entry.getPassword()));
        entry.setRoles(Arrays.asList("User"));
        return usersRepository.save(entry);
    }

    public List<UserEntry> getAllEntries() {
        return usersRepository.findAll();
    }

    public Optional<UserEntry> getById(ObjectId id) {
        return usersRepository.findById(id);
    }

    public UserEntry findByPhoneNumber(String phoneNumber) {
        return usersRepository.findByPhoneNumber(phoneNumber);
    }

    public UserEntry findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public void deleteById(ObjectId id) {
        usersRepository.deleteById(id);
    }

    @Transactional
    public String addFriendById(String userPhoneNumber, String friendPhoneNumber) {
        // Step 1: Get users
        UserEntry currentUser = findByPhoneNumber(userPhoneNumber);
        UserEntry friendUser = findByPhoneNumber(friendPhoneNumber);
        
        // Step 2: Initialize collections if null
        if (currentUser.getFriends() == null) {
            currentUser.setFriends(new ArrayList<>());
        }
        if (friendUser.getFriends() == null) {
            friendUser.setFriends(new ArrayList<>());
        }
        if (currentUser.getChats() == null) {
            currentUser.setChats(new ArrayList<>());
        }
        if (friendUser.getChats() == null) {
            friendUser.setChats(new ArrayList<>());
        }

        // Step 3: Add Friends if not already
        if ( currentUser.getFriends().contains(friendUser.getUserId())){
            return "Already Friends";
        }
        currentUser.getFriends().add(friendUser.getUserId());
        friendUser.getFriends().add(currentUser.getUserId());

        // Step 1: Create and save chat FIRST
        ChatEntry chatEntry = new ChatEntry();
        chatEntry.setCreatedAt(LocalDateTime.now());
        ChatEntry savedChat = chatService.saveEntry(chatEntry);


        // Step 5: Add the SAVED chat to both users (key point!)
        boolean chatExistsInCurrentUser = currentUser.getChats().stream()
                .anyMatch(chat -> chat.getChatId().equals(savedChat.getChatId()));
        boolean chatExistsInFriendUser = friendUser.getChats().stream()
                .anyMatch(chat -> chat.getChatId().equals(savedChat.getChatId()));

        if (!chatExistsInCurrentUser) {
            currentUser.getChats().add(savedChat);
        }
        if (!chatExistsInFriendUser) {
            friendUser.getChats().add(savedChat);
        }

        // Step 6: Initialize and update friend-chat mappings
        if (currentUser.getFriendChatMap() == null) {
            currentUser.setFriendChatMap(new HashMap<>());
        }
        if (friendUser.getFriendChatMap() == null) {
            friendUser.setFriendChatMap(new HashMap<>());
        }

        currentUser.getFriendChatMap().put(friendUser.getUserId(), savedChat.getChatId());
        friendUser.getFriendChatMap().put(currentUser.getUserId(), savedChat.getChatId());

        // Step 7: Save users (this will create the DBRef links)
        saveEntry(currentUser);
        saveEntry(friendUser);
        return  "Friends added successfully.";
    }
}
