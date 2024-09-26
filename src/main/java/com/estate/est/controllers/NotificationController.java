package com.estate.est.controllers;

import com.estate.est.dto.ApiResponse;
import com.estate.est.dto.NotificationDto;
import com.estate.est.entities.Notification;
import com.estate.est.service.implementations.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationServiceImpl notificationService;
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Notification>> getAllUserNotificationsHandler(@PathVariable("userId")Long userId)throws Exception{
        return new ResponseEntity<>(notificationService.getAllUserNotifications(userId), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{notificationId}/{userId}")
    public ResponseEntity<String> deleteNotificationHandler(@RequestHeader("Authorization")String jwt, @PathVariable("notificationId")Long notificationId,@PathVariable("userId")Long userId)throws Exception{
        return new ResponseEntity<>(notificationService.deleteNotitcation(jwt, userId, notificationId),HttpStatus.OK);
    }
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponse> clearAllUserNotificationsHandler(@PathVariable("userId")Long userId)throws Exception{
        return new ResponseEntity<>(new ApiResponse(notificationService.clearAllNotifications(userId),"true"),HttpStatus.OK);
    }
}
