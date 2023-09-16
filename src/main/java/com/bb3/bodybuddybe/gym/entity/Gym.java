package com.bb3.bodybuddybe.gym.entity;

import com.bb3.bodybuddybe.common.listener.TimeStamped;
import com.bb3.bodybuddybe.gym.dto.GymRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "gym")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gym extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String kakaoPlaceId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String roadAddress;

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL)
    private List<UserGym> members = new ArrayList<>();

    public Gym(GymRequestDto requestDto) {
        this.kakaoPlaceId = requestDto.getId();
        this.name = requestDto.getName();
        this.roadAddress = requestDto.getRoadAddress();
    }

    public Gym(String kakaoPlaceId, String name, String roadAddress) {
        this.kakaoPlaceId = kakaoPlaceId;
        this.name = name;
        this.roadAddress = roadAddress;
    }
}