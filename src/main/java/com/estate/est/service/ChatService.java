package com.estate.est.service;

import com.estate.est.dto.MessageDto;
import com.estate.est.entities.Chat;
import com.estate.est.entities.Message;

import java.util.List;

public interface ChatService {
    Chat createChat(Long user1Id, Long user2Id)throws Exception;
    MessageDto sendMessage(Long chatId, String sender_token, Long receiver_id, MessageDto msg)throws Exception;
    String removeChat(Long chatId)throws Exception;
    List<Chat> getAllUserChats(Long userId, String token)throws Exception;
    Chat findById(Long chatId)throws Exception;
    List<Message> getAllMessagesBtChat(Long chatId, String token)throws Exception;
}
