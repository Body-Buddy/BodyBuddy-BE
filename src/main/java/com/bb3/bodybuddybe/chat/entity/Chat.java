package com.bb3.bodybuddybe.chat.entity;

import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatType chatType; // 1대1 or 그룹

    @Column
    private String roomname; // 채팅방 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User ownerUser;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true) // 채팅방이 삭제되면 참여인원들 같이 삭제되도록
    private List<UserChat> userChatList;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true) // 채팅방이 삭제되면 메세지들도 같이 삭제 되도록
    private List<Message> messages;

    @Builder
    public Chat(ChatType chatType, String roomName, Gym gym, User ownerUser) {
        this.chatType = chatType;
        this.roomname = roomName;
        this.gym = gym;
        this.ownerUser = ownerUser;
    }

    public void updateChat(ChatType chatType, String roomname) {
        this.chatType = chatType;
        this.roomname = roomname;
    }

}
