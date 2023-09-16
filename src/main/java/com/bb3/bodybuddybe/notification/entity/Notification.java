package com.bb3.bodybuddybe.notification.entity;

import com.bb3.bodybuddybe.common.listener.TimeStamped;
import com.bb3.bodybuddybe.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Notification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String content;

    @Column(nullable = false)
    private Boolean isRead;

    public void read() {
        this.isRead = true;
    }

}
