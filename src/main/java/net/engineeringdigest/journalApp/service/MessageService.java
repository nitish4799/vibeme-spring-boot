package net.engineeringdigest.journalApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entity.MessageEntry;
import net.engineeringdigest.journalApp.repository.MessagesRepository;
@Component
public class MessageService {
    @Autowired MessagesRepository messagesRepository;

    public List<MessageEntry> getAllMesages() {
        // List<MessageEntry> messages = chat.getMessages();
        // for( MessageEntry message: messages){
        //     messagesRepository.findById(message.getMessageId());
        // }
        return messagesRepository.findAll();
    }

    public MessageEntry saveMessage(MessageEntry entry) {
        return messagesRepository.save(entry);
    }
}
