package com.estate.est.repositories;

import com.estate.est.entities.Chat;
import com.estate.est.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Chat findByUser1AndUser2(User user1, User user2);
    List<Chat> findByUser1(User user);
    List<Chat> findByUser2(User user);
}
