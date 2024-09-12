package com.estate.est.service.implementations;

import com.estate.est.config.JwtProvider;
import com.estate.est.dto.MessageDto;
import com.estate.est.entities.Chat;
import com.estate.est.entities.Message;
import com.estate.est.entities.User;
import com.estate.est.exceptions.ChatException;
import com.estate.est.repositories.ChatRepository;
import com.estate.est.repositories.MessageRepository;
import com.estate.est.repositories.UserRepository;
import com.estate.est.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Chat createChat(Long user1Id, Long user2Id) throws Exception{
        Optional<User> user1Exists = userRepository.findById(user1Id);
        Optional<User> user2Exists = userRepository.findById(user2Id);
        if (user1Exists.isEmpty() || user2Exists.isEmpty()) {
            throw new Exception("One or both users not found");
        }
        Optional<Chat> existingChat = Optional.ofNullable(chatRepository.findByUser1AndUser2(user1Exists.get(), user2Exists.get()));
        if(existingChat.isPresent()){
            return existingChat.get();
        }
        Chat newChat = new Chat();
        newChat.setUser1(user1Exists.get());
        newChat.setUser2(user2Exists.get());
        return chatRepository.save(newChat);
    }

    @Override
    public MessageDto sendMessage(Long chatId, String senderToken, Long receiverId, MessageDto msg) throws Exception {
        // Retrieve chat from the database
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isEmpty()) {
            throw new ChatException("Chat not found with id " + chatId);
        }
        Chat chat = optionalChat.get();

        // Retrieve sender from token
        User sender = userRepository.getByEmail(jwtProvider.getEmailFromToken(senderToken));
        if (sender == null) {
            throw new ChatException("Sender not found");
        }

        // Retrieve receiver from database
        Optional<User> optionalReceiver = userRepository.findById(receiverId);
        if (optionalReceiver.isEmpty()) {
            throw new ChatException("Receiver not found");
        }
        User receiver = optionalReceiver.get();

        // Validate that sender and receiver are part of the chat
        if ((chat.getUser1() != sender && chat.getUser2() != sender) ||
                (chat.getUser1() != receiver && chat.getUser2() != receiver)) {
            throw new ChatException("Sender or receiver do not belong to this chat");
        }
        if (sender.equals(receiver)){
            throw new ChatException("You cannot send a message to yourself");
        }
        // Create and save the new message
        Message newMessage = new Message();
        newMessage.setMsgSender(sender);
        newMessage.setMsgReceiver(receiver);
        newMessage.setChat(chat);
        newMessage.setMessage(msg.getMessage());
        messageRepository.save(newMessage);

        return new MessageDto(newMessage.getMessage());
    }


    @Override
    public String removeChat(Long chatId) throws Exception{
        Optional<Chat> chatExists = chatRepository.findById(chatId);
        if(chatExists.isEmpty()){
            throw new ChatException("chat doesn't exists");
        }
        chatRepository.deleteById(chatId);
        return "chat with id " +chatId+" deleted successfully";
    }

    @Override
    public List<Chat> getAllUserChats(Long userId, String token) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatException("User not found"));

        // Return all chats for the user, regardless of whether they are user1 or user2
        List<Chat> allChats = new ArrayList<>();
        allChats.addAll(user.getChatsAsUser1());
        allChats.addAll(user.getChatsAsUser2());
        return allChats;
    }

    @Override
    public Chat findById(Long chatId) throws Exception {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()){
            throw new ChatException("chat with id "+chatId+" not found");
        }
        return chat.get();
    }

    @Override
    public List<Message> getAllMessagesBtChat(Long chatId, String token)throws Exception {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found"));

        String tokenEmail = jwtProvider.getEmailFromToken(token);
        if (!isEmailFromTokenValid(tokenEmail, chat)) {
            throw new ChatException("You cannot see any other user's chats");
        }

        return chat.getMessages();
    }

    private boolean isEmailFromTokenValid(String tokenEmail, Chat chat) {
        return tokenEmail.equalsIgnoreCase(chat.getUser1().getEmail()) ||
                tokenEmail.equalsIgnoreCase(chat.getUser2().getEmail());
    }

}
