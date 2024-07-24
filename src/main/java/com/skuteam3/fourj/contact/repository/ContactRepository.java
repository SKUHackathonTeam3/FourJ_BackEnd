package com.skuteam3.fourj.contact.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.contact.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    public List<Contact> findContactByUserInfo(UserInfo userInfo);

}
