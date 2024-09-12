package com.estate.est.service;

import com.estate.est.dto.NotificationDto;
import com.estate.est.entities.Notification;

import java.util.List;

public interface NofificationService {
    NotificationDto createNotification(Long userId, NotificationDto notification)throws Exception;
    String deleteNotitcation(Long userId, Long notificationId)throws Exception;
    List<Notification> getAllUserNotifications(Long userId)throws Exception;
}
