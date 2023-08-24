package com.bb3.bodybuddybe.timestamped;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


    @Getter
    @MappedSuperclass
    @EntityListeners(AuditingEntityListener.class)
    public abstract class Timestamped {

        @CreatedDate
        @Column(updatable = false, name = "create_time")
        @Temporal(TemporalType.TIMESTAMP)
        private LocalDateTime createdAt;

        @LastModifiedDate
        @Column(name = "modify_time")
        @Temporal(TemporalType.TIMESTAMP)
        private LocalDateTime modifiedAt;
    }

