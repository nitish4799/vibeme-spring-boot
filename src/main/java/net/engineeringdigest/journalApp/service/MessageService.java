package net.engineeringdigest.journalApp.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entity.MessageEntry;
import net.engineeringdigest.journalApp.repository.MessagesRepository;

@Component
public class MessageService {

    @Autowired
    MessagesRepository messagesRepository;

    public List<MessageEntry> getAllMesages() {
        return messagesRepository.findAll();
    }

    public MessageEntry saveMessage(MessageEntry entry) {
        return messagesRepository.save(entry);
    }

    public void deleteMessageById(ObjectId id) {
        messagesRepository.deleteById(id);
    }

    public MessageEntry getMessageById(ObjectId id){
        Optional<MessageEntry> messageOptional = messagesRepository.findById(id);
        return messageOptional.orElse(null);
    }
}
