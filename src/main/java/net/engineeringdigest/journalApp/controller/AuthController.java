package net.engineeringdigest.journalApp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.entity.UserLoginEntry;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/users") // mapping on the whole class
public class AuthController {

    @Autowired
    private UserService UserService;

    @GetMapping()
    public List<UserEntry> getAllUsers() {
        return UserService.getAllEntries();
    }

    @PostMapping("/signup")
    public String createUser(@RequestBody UserEntry entry) {
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        UserService.saveEntry(entry);
        return "User added";
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
        System.out.println(emailMatches);
        System.out.println(phoneNumberMatches);
        System.out.println(passwordMatches);
        System.out.println(currentUser.getPassword());
        System.out.println(user.getPassword());

        if ((emailMatches || phoneNumberMatches) && passwordMatches) {
            return ResponseEntity
                    .ok()
                    .body(currentUser);
        } else {
            return ResponseEntity
                    .status(401)  // Unauthorized
                    .body("Authentication failed: Invalid email/phone or password");
        }
    }


    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable ObjectId id) {
        UserService.delteById(id);
        return "Entry deleted";
    }

    @PutMapping("/update")
    public String updateUserById(@RequestBody UserEntry entry){
        entry.setUpdatedAt(LocalDateTime.now());
        UserService.saveEntry(entry);
        return "User updated";
    }

    @PutMapping("/addFriend")
    public String addFriendById(@RequestParam ObjectId userId, @RequestParam ObjectId friendId) {
        UserService.addFriendById(userId, friendId);
        return "Friend added successfully";
    }

}
