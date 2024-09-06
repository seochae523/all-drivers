package com.alldriver.alldriver.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="sms_session")
public class SmsSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="auth_code", columnDefinition = "varchar", length = 6, nullable = false)
    private String authCode;

    @Column(name="phone_number", columnDefinition = "varchar", length = 11,nullable = false)
    private String phoneNumber;

    @CreationTimestamp
    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;
    public void resetAuthCode(String authCode, LocalDateTime createdAt) {
        this.authCode = authCode;
        this.createdAt = createdAt;
    }
}
