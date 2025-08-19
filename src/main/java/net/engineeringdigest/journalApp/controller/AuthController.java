package net.engineeringdigest.journalApp.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.entity.UserLoginEntry;
import net.engineeringdigest.journalApp.service.ChatService;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/users") // mapping on the whole class
public class AuthController {

    @Autowired
    private UserService UserService;
    @Autowired
    private ChatService chatService;

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
        entry.setChats(new ArrayList<>());
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
            ChatEntry chatEntry = new ChatEntry();
            chatEntry.setCreatedAt(LocalDateTime.now());
            ChatEntry chat = chatService.saveEntry(chatEntry);
            UserService.addFriendById(userPhoneNumber, friendPhoneNumber, chat);
            return ResponseEntity.ok().body("Friend added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Add Friend request failed with exception:" + e.getMessage());
        }
    }

}
