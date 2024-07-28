package com.skuteam3.fourj.oauth2.domain;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.oauth2.SocialType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
public class SocialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_user_id", updatable = false)
    private Long userId;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "Invalid email format.")
    @Column(name = "social_user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "social_user_provider_id", nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_user_login_type", nullable = false)
    private SocialType socialType;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public SocialUser(String email, SocialType socialType, String providerId, User user) {
        this.email = email;
        this.socialType = socialType;
        this.providerId = providerId;
        this.user = user;
    }

    public String getSocialType() {
        return socialType.name();
    }
}
