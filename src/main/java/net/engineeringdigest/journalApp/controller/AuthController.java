package net.engineeringdigest.journalApp.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.entity.MessageEntry;
import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.entity.UserLoginEntry;
import net.engineeringdigest.journalApp.service.ChatService;
import net.engineeringdigest.journalApp.service.MessageService;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/users") // mapping on the whole class
public class AuthController {

    @Autowired
    private UserService UserService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;

    @GetMapping()
    public ResponseEntity<List<UserEntry>> getAllUsers() {
        List<UserEntry> users = UserService.getAllEntries();
        if (!users.isEmpty()) {
            return ResponseEntity.ok().body(users);
        }
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserEntry entry) {
        Optional<UserEntry> userAlreadyExist = UserService.findByPhoneNumber(entry.getPhoneNumber());
        if (userAlreadyExist.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists");
        }
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setFriends(new ArrayList<>());
        UserEntry savedUser = UserService.saveEntry(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable ObjectId id, @RequestBody UserLoginEntry user) {
        UserEntry currentUser = UserService.getById(id).orElse(null);
        if (currentUser == null || user == null) {
            return ResponseEntity
                    .badRequest()
                    .body("User or input data is missing");
        }
        boolean emailMatches = user.getEmail() != null && currentUser.getEmail().equals(user.getEmail());
        boolean phoneNumberMatches = user.getPhoneNumber() != null && currentUser.getPhoneNumber().equals(user.getPhoneNumber());
        boolean passwordMatches = currentUser.getPassword().equals(user.getPassword());
        if ((emailMatches || phoneNumberMatches) && passwordMatches) {
            return ResponseEntity
                    .ok()
                    .body(currentUser);
        } else {
            return ResponseEntity
                    .status(401)
                    .body("Authentication failed: Invalid email/phone or password");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable ObjectId id) {
        try {
            UserService.deleteById(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User deletion failed with exception: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserById(@RequestBody UserEntry entry) {
        Optional<UserEntry> userOptional = UserService.findByPhoneNumber(entry.getPhoneNumber());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        UserEntry user = userOptional.get();
        user.setUpdatedAt(LocalDateTime.now());
        if (entry.getEmail() != null) {
            user.setEmail(entry.getEmail());
        }
        if (!entry.getPassword().isEmpty()) {
            user.setPassword(entry.getPassword());
        }
        try {
            UserEntry updatedUser = UserService.saveEntry(user);
            return ResponseEntity.ok().body(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User update failed with error: " + e.getMessage());
        }
    }

    @PutMapping("/addFriend")
    public ResponseEntity<?> addFriendById(@RequestParam String userPhoneNumber, @RequestParam String friendPhoneNumber) {
        try {
        
            UserService.addFriendById(userPhoneNumber, friendPhoneNumber);
            return ResponseEntity.ok().body("Friend added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Add Friend request failed with exception:" + e.getMessage());
        }
    }

// need to be transactional ??
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageEntry message, @RequestParam String userPhone, @RequestParam String friendPhone) {
        Optional<UserEntry> userOptional = UserService.findByPhoneNumber(userPhone);
        Optional<UserEntry> friendOptional = UserService.findByPhoneNumber(friendPhone);
        if (!userOptional.isPresent() || !friendOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User or friend not found");
        }
        UserEntry user = userOptional.get();
        UserEntry friend = friendOptional.get();
        Map<ObjectId, ObjectId> friendChatMap = user.getFriendChatMap();
        if (friendChatMap == null || !friendChatMap.containsKey(friend.getUserId())) {
            return ResponseEntity.badRequest().body("Chat not found between users");
        }
        ObjectId chatId = friendChatMap.get(friend.getUserId());
        Optional<ChatEntry> chatOptional = chatService.getChatById(chatId);
        if ( !chatOptional.isPresent()){
            return ResponseEntity.badRequest().body("Chat not Found");
        }
        ChatEntry chat = chatOptional.get();
        List<MessageEntry> oldMsgs = chat.getMessages();
        MessageEntry msg = messageService.saveMessage(message);
        oldMsgs.add(msg);
        chat.setMessages(oldMsgs);
        chatService.saveEntry(chat);
        return ResponseEntity.ok().body(chat.getMessages());
    }

}
