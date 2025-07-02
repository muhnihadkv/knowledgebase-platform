package com.Knowledgebase.User.repositories;

import com.Knowledgebase.User.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    //    List<Notification> findByUserUserIdOrderByCreatedAtDesc(int userId);
    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByUserId(@Param("userId") int userId);

}
