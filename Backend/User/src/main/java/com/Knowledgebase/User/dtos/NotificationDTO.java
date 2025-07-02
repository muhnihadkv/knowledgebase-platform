package com.Knowledgebase.User.dtos;

import com.Knowledgebase.User.entities.Notification;

import java.time.Instant;

public record NotificationDTO(
        Long id,
        String message,
        boolean isRead,
        Instant createdAt,
        int userId
) {
    public static NotificationDTO of(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getUser().getUserId()
        );
    }
}
