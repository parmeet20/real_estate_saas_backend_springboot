package com.estate.est.repositories;

import com.estate.est.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Notification findByNotification(String notification);
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
    List<Notification> getAllUserNotificationsByUserId(@Param("userId") Long userId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}

