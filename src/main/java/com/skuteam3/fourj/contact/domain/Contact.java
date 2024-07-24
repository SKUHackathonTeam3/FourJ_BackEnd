package com.skuteam3.fourj.contact.domain;

import com.skuteam3.fourj.account.domain.UserInfo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Contact_id")
    private Long id;

    private String name;
    private String number;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private UserInfo userInfo;

    @Column(name = "isMain")
    private Boolean isMain;

}
