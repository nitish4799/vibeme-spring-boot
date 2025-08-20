package net.engineeringdigest.journalApp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/chats")
public class ChatController {
    // @Autowired
    // private ChatService chatService;
    @Autowired
    private  UserService userService;
    // @Autowired MessageService messageService;

    @GetMapping("/{phoneNumber}")
    public List<ChatEntry> getChatsByPhoneNumber(@PathVariable String phoneNumber) {
        Optional<UserEntry> userOptional = userService.findByPhoneNumber(phoneNumber);
        if (!userOptional.isPresent()) return new ArrayList<>();
        UserEntry user = userOptional.get();
        List<ChatEntry> chats = user.getChats();
        return chats;
    }

    // public List<MessageEntry> getMessagesByChatId(ObjectId chatId) {

    // }
}
