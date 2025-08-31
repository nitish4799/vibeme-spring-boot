package net.engineeringdigest.journalApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    private  UserService userService;

    @GetMapping
    public List<ChatEntry> getChatsByPhoneNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        UserEntry user = userService.findByPhoneNumber(phoneNumber);
        return user.getChats();
    }
}
