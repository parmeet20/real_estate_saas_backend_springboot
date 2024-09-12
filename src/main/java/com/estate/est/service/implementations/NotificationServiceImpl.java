package com.estate.est.service.implementations;

import com.estate.est.dto.NotificationDto;
import com.estate.est.entities.Notification;
import com.estate.est.entities.User;
import com.estate.est.repositories.NotificationRepository;
import com.estate.est.repositories.UserRepository;
import com.estate.est.service.NofificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NofificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public NotificationDto createNotification(Long userId, NotificationDto notification) throws Exception {
        Notification notificationExists = notificationRepository.findByNotification(notification.getNotification());
        Optional<User> userExists = userRepository.findById(userId);
        if (userExists.isEmpty()){
            throw new Exception("user not found to send notification");
        }
        if(notificationExists == null){
            throw new Exception("notification already exists");
        }
        Notification newNotification = new Notification();
        newNotification.setIsRead(false);
        newNotification.setUser(userExists.get());
        newNotification.setNotification(notification.getNotification());
        notificationRepository.save(newNotification);
        userExists.get().getNotifications().add(newNotification);
        userRepository.save(userExists.get());
        return new NotificationDto(newNotification.getUser(),newNotification.getNotification(),newNotification.getIsRead());
    }

    @Override
    public String deleteNotitcation(Long userId, Long notificationId) throws Exception {
        Optional<Notification> notificationExists = notificationRepository.findById(notificationId);
        Optional<User> userExists = userRepository.findById(userId);
        if (userExists.isEmpty()){
            throw new Exception("user not found to send notification");
        }
        if(notificationExists.isEmpty()){
            throw new Exception("notification not found with id " + notificationId);
        }
        userExists.get().getNotifications().remove(notificationExists.get());
        notificationRepository.deleteById(notificationId);
        return "notification with id " + notificationId + " deleted successfully";
    }

    @Override
    public List<Notification> getAllUserNotifications(Long userId) throws Exception {
        Optional<User> userExists = userRepository.findById(userId);
        if (userExists.isEmpty()){
            throw new Exception("user not found");
        }
        return notificationRepository.getAllUserNotificationsByUserId(userId);
    }
}
