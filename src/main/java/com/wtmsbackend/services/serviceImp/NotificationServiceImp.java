package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.NotificationRequest;
import com.wtmsbackend.dto.response.NotificationResponse;
import com.wtmsbackend.models.Notification;
import com.wtmsbackend.models.User;
import com.wtmsbackend.repositories.NotificationRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.services.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotificationResponse> getAllNotifications(int page, int size) {
        List<NotificationResponse> notifications = notificationRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponse)
                .getContent();
        return notifications;
    }

    @Override
    public List<NotificationResponse> getUserNotifications(Integer userId, int page, int size) {
        List<NotificationResponse> notifications = notificationRepository.findByUserId(userId, PageRequest.of(page, size))
                .map(this::mapToResponse)
                .getContent();
        return notifications;
    }

    @Override
    public List<NotificationResponse> getUnreadUserNotifications(Integer userId, int page, int size) {
        List<NotificationResponse> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId, PageRequest.of(page, size))
                .map(this::mapToResponse)
                .getContent();
        return notifications;
    }

    @Override
    public NotificationResponse getNotificationById(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
        return mapToResponse(notification);
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .message(request.getMessage())
                .isRead(false)
                .build();

        return mapToResponse(notificationRepository.save(notification));
    }

    @Override
    public NotificationResponse updateNotification(Integer id, NotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setMessage(request.getMessage());

        if (!notification.getUser().getId().equals(request.getUserId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            notification.setUser(user);
        }

        return mapToResponse(notificationRepository.save(notification));
    }

    @Override
    public NotificationResponse markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        return mapToResponse(notificationRepository.save(notification));
    }

    @Override
    public void deleteNotification(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .userName(notification.getUser().getFirstName() + " " + notification.getUser().getLastName())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}