package com.Knowledgebase.User.services;

import com.Knowledgebase.User.dtos.NotificationDTO;
import com.Knowledgebase.User.entities.Notification;
import com.Knowledgebase.User.repositories.NotificationRepository;
import com.Knowledgebase.User.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private DocumentService documentService;

    public List<NotificationDTO> getNotifications(String authHeader) {
        int userId = documentService.getUserIdFromToken(authHeader);
        System.out.println("Extracted User ID: " + userId);
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification not:notifications){
            System.out.println(not.getMessage());
        }
        return notifications.stream()
                .map(NotificationDTO::of)
                .toList();
    }

    public void markAsRead(Long notificationId, String authHeader) {
        int userId = documentService.getUserIdFromToken(authHeader);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("Notification not found"));

        if (notification.getUser().getUserId() != userId) {
            throw new RuntimeException("Unauthorized");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}

