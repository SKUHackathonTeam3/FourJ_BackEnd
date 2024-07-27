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
public class PrivatePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privatePost_id")
    private Long id;

    private String contents;

    @CreationTimestamp
    private LocalDateTime PostDate;

    @ManyToOne
    @JoinColumn(name="User_id")
    private UserInfo userInfo;

}
