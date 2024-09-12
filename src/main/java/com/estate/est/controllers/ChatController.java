package com.estate.est.controllers;

import com.estate.est.dto.MessageDto;
import com.estate.est.entities.Chat;
import com.estate.est.entities.Message;
import com.estate.est.service.implementations.ChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    @Autowired
    private ChatServiceImpl chatService;
    @GetMapping("/{chatId}")
    public ResponseEntity<List<Message>> getAllChatMessagesHandler(@RequestHeader("Authorization")String jwt, @PathVariable("chatId")Long chatId)throws Exception{
        return new ResponseEntity<>(chatService.getAllMessagesBtChat(chatId,jwt), HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getAllUserChatsHandler(@RequestHeader("Authorization")String jwt, @PathVariable("userId")Long userId)throws Exception{
        return new ResponseEntity<>(chatService.getAllUserChats(userId,jwt),HttpStatus.OK);
    }
    @PostMapping("/create/{user1Id}/{user2Id}")
    public ResponseEntity<Chat> createChatHandler(@PathVariable("user1Id") Long user1Id,@PathVariable("user2Id") Long user2Id)throws Exception{
        return new ResponseEntity<>(chatService.createChat(user1Id,user2Id), HttpStatus.CREATED);
    }
    @PostMapping("/{chatId}/{receiverId}")
    public ResponseEntity<MessageDto> sendMessageHandler(
            @RequestHeader("Authorization")String jwt,
            @PathVariable("chatId")Long chatId,
            @PathVariable("receiverId")Long receiverId,
            @RequestBody MessageDto msg
    )throws Exception{
        return new ResponseEntity<>(chatService.sendMessage(chatId,jwt,receiverId,msg),HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<String> removeChatHandler(@PathVariable("chatId")Long chatId)throws Exception{
        return new ResponseEntity<>(chatService.removeChat(chatId),HttpStatus.ACCEPTED);
    }
}
