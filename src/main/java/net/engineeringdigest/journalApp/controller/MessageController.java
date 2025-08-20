package net.engineeringdigest.journalApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.MessageEntry;
import net.engineeringdigest.journalApp.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<MessageEntry> getAll() {
        return messageService.getAllMesages();
    }

//    @PostMapping("/send")
//    public void sendMessage(MessageEntry message) {
//        messageService.saveMessage(message);
//    }

    // public void getMessagesByUsername(ChatService chat){

    // }
}
