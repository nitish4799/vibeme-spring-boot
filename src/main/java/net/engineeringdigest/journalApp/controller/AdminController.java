package net.engineeringdigest.journalApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntry> users = userService.getAllEntries();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createAdmin(@RequestBody UserEntry entry){
        UserEntry user = userService.saveAdminEntry(entry);
        return ResponseEntity.ok().body(user);
    }

}
