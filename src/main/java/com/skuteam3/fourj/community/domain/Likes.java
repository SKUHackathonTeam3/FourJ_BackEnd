package com.skuteam3.fourj.community.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="group_post_id")
    private GroupPost groupPost;

    @ManyToOne
    @JoinColumn(name="user_info_id")
    private UserInfo userInfo;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
