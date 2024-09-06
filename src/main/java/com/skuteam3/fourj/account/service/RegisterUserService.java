package com.skuteam3.fourj.account.service;

import com.skuteam3.fourj.account.LoginType;
import com.skuteam3.fourj.account.Role;
import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.dto.RegisterUserDto;
import com.skuteam3.fourj.account.repository.UserInfoRepository;
import com.skuteam3.fourj.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisterUserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    @Transactional
    public Long save(RegisterUserDto dto) {

        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        User user = null;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (user.getPassword() != null) {
                throw new IllegalArgumentException("Email already exists.");
            }

            else {
                user.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
                user.setName(dto.getName());

                return userRepository.save(user).getUserId();
            }
        }

        UserInfo userInfo = userInfoRepository.save(UserInfo.builder()
                .name(dto.getName())
                .build());

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bcryptPasswordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .userLoginType(LoginType.FOURJ)
                .userRole(Role.USER)
                .userInfo(userInfo)
                .build()).getUserId();
    }
}
