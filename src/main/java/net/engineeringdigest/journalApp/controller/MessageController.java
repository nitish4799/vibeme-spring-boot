package net.engineeringdigest.journalApp.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.ChatEntry;
import net.engineeringdigest.journalApp.entity.MessageEntry;
import net.engineeringdigest.journalApp.entity.UserEntry;
import net.engineeringdigest.journalApp.service.ChatService;
import net.engineeringdigest.journalApp.service.MessageService;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService UserService;
    @Autowired
    private ChatService chatService;

    @GetMapping
    public List<MessageEntry> getAll() {
        return messageService.getAllMesages();
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageEntry message, @RequestParam String friendPhone) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userPhone = auth.getName();
        UserEntry user = UserService.findByPhoneNumber(userPhone);
        UserEntry friend = UserService.findByPhoneNumber(friendPhone);
        Map<ObjectId, ObjectId> friendChatMap = user.getFriendChatMap();
        if (friendChatMap == null || !friendChatMap.containsKey(friend.getUserId())) {
            return ResponseEntity.badRequest().body("Chat not found between users");
        }
        ObjectId chatId = friendChatMap.get(friend.getUserId());
        Optional<ChatEntry> chatOptional = chatService.getChatById(chatId);
        if (!chatOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Chat not Found");
        }
        ChatEntry chat = chatOptional.get();
        List<MessageEntry> oldMsgs = chat.getMessages();
        MessageEntry msg = messageService.saveMessage(message);
        oldMsgs.add(msg);
        chat.setMessages(oldMsgs);
        chatService.saveEntry(chat);
        return ResponseEntity.ok().body(chat.getMessages());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable ObjectId id) {
        try {
            messageService.deleteMessageById(id);
            return ResponseEntity.ok().body("Message deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.ok().body("Message deletion failed: " + e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable ObjectId id, @RequestBody String editMsg){
        try {
            MessageEntry msg = messageService.getMessageById(id);
            msg.setMessage(editMsg);
            MessageEntry newMsg = messageService.saveMessage(msg);
            return ResponseEntity.ok().body(newMsg);
        } catch (Exception e) {
            return ResponseEntity.ok().body("Message Updation was failed: " + e);
        }
    }

}
