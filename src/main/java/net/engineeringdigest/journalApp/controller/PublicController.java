package net.engineeringdigest.journalApp.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/status")
    public ResponseEntity<String> fun() {
        return ResponseEntity
                .ok()
                .body("Vibe me is properly functional.");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserEntry entry) {
        UserEntry phoneAlreadyExist = userService.findByPhoneNumber(entry.getPhoneNumber());
        UserEntry emailAlreadyExist = userService.findByEmail(entry.getEmail());
        if (phoneAlreadyExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Phone Number already registered.");
        }
        if (emailAlreadyExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already registered.");
        }
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setFriends(new ArrayList<>());
        UserEntry savedUser = userService.saveNewEntry(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
