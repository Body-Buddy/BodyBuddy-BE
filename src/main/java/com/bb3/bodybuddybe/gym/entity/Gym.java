package com.bb3.bodybuddybe.gym.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "gym")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "kakao_place_id", nullable = false, unique = true)
    private String kakaoPlaceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL)
    private List<UserGym> members = new ArrayList<>();

    public Gym(String kakaoPlaceId, String name, String roadAddress) {
        this.kakaoPlaceId = kakaoPlaceId;
        this.name = name;
        this.roadAddress = roadAddress;
    }
}