package com.paekom.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_accounts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OauthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(length = 128, nullable = false)
    private String providerUserId;

    @Column(length = 255)
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;
}
