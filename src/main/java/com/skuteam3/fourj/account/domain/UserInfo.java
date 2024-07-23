package com.skuteam3.fourj.account.domain;

import com.skuteam3.fourj.abti.domain.Abti;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id")
    private Long id;

    @Column(name = "user_info_name")
    private String name;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @Builder
    public UserInfo(String name, User user) {
        this.name = name;
        this.user = user;
    }

    // Feature 추가할 때 아래 추가하기!!!

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "abti", referencedColumnName = "id")
    private Abti abti;


}
