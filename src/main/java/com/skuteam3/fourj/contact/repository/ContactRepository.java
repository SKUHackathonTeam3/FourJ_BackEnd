package com.skuteam3.fourj.contact.repository;

import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.contact.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    public List<Contact> findContactByUserInfo(UserInfo userInfo);
    public Optional<Contact> findContactByIdAndIsMain(Long id, boolean isMain);
    public Optional<Contact> findContactByUserInfoAndIsMain(UserInfo userInfo, Boolean isMain);
}
