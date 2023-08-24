package com.bb3.bodybuddybe.gym.entity;

import com.bb3.bodybuddybe.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_gym")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    public UserGym(User user, Gym gym) {
        this.user = user;
        this.gym = gym;
    }
}