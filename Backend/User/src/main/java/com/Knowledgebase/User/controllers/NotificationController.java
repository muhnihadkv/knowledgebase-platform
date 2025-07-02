package com.Knowledgebase.User.controllers;

import com.Knowledgebase.User.dtos.NotificationDTO;
import com.Knowledgebase.User.entities.Notification;
import com.Knowledgebase.User.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getByUser")
    public List<NotificationDTO> getNotifications(@RequestHeader("Authorization") String authHeader) {
        return notificationService.getNotifications(authHeader);
    }

    @PostMapping("/markAsRead/{id}")
    public void markAsRead(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        notificationService.markAsRead(id, authHeader);
    }
}
