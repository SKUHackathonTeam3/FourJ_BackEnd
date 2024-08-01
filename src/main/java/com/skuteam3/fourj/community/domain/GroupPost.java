package com.skuteam3.fourj.community.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class GroupPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_post_id")
    private Long id;

    private String title;
    private String contents;
    private String hashtag;

    @CreationTimestamp
    private LocalDateTime PostDate;

    @ManyToOne
    @JoinColumn(name="User_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy="groupPost")
    private List<Likes> likes = new ArrayList<>();

}
