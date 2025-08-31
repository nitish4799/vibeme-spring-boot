package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class HealthCheck {

    @Autowired
    private UserService userService;

    @GetMapping("/check")
    public ResponseEntity<String> fun() {
        return ResponseEntity
                .ok()
                .body("RunningSmoothly");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserEntry entry) {
        UserEntry userAlreadyExist = userService.findByPhoneNumber(entry.getPhoneNumber());
        if (userAlreadyExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists");
        }
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setFriends(new ArrayList<>());
        UserEntry savedUser = userService.saveNewEntry(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
