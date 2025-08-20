package net.engineeringdigest.journalApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entity.MessageEntry;
import net.engineeringdigest.journalApp.repository.MessagesRepository;
@Component
public class MessageService {
    @Autowired MessagesRepository messagesRepository;

    // public List<MessageEntry> getAllMesages(ChatEntry chat) {
    //     List<MessageEntry> messages = chat.getMessages();
    //     for( MessageEntry message: messages){
    //         messagesRepository.findById(message.getMessageId());
    //     }
    //     return messagesRepository.findAll();
    // }

    public void saveMessage(MessageEntry entry) {
        messagesRepository.save(entry);
    }
}
