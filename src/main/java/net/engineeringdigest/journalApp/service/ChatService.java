package net.engineeringdigest.journalApp.service;

import org.springframework.beans.factory.annotation.Autowired;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.repository.ChatsRepository;
import org.springframework.stereotype.Component;

@Component
public class ChatService {

    @Autowired
    private ChatsRepository chatsRepository;

    public ChatEntry saveEntry(ChatEntry entry){
        return chatsRepository.save(entry);
    }
}
