package com.skuteam3.fourj.account.domain;

import com.skuteam3.fourj.account.LoginType;
import com.skuteam3.fourj.account.Role;
import com.skuteam3.fourj.oauth2.domain.SocialUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "Invalid email format.")
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "user_password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Role userRole;

    @Column(name = "user_refresh_token")
    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SocialUser> socialUsers = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public User(String name, String email, String password, LoginType userLoginType, Role userRole, UserInfo userInfo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.userInfo = userInfo;
    }

    public String getUserRole() { return userRole.getKey(); }

}
