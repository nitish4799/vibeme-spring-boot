package net.engineeringdigest.journalApp.service;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.repository.ChatsRepository;

@Component
public class ChatService {

    @Autowired
    private ChatsRepository chatsRepository;

    public ChatEntry saveEntry(ChatEntry entry){
        return chatsRepository.save(entry);
    }

    public Optional<ChatEntry> getChatById(ObjectId chatId){
        return  chatsRepository.findById(chatId);
    }
}
