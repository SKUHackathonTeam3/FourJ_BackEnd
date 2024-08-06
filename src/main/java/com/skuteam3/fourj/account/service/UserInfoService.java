package com.skuteam3.fourj.account.service;

import com.skuteam3.fourj.abti.repository.AbtiRepository;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.dto.*;
import com.skuteam3.fourj.account.repository.UserInfoRepository;
import com.skuteam3.fourj.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final AbtiRepository abtiRepository;

    public UserNameDto getUserInfoName(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                ).getUserInfo();

        return new UserNameDto(userInfo.getName());
    }

    public UserAbtiDto getUserInfoAbti(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                ).getUserInfo();

        if (userInfo.getAbti() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저의 술비티아이가 설정되어있지 않습니다.");
        }

        return new UserAbtiDto(userInfo.getAbti().getTitle());
    }

    public void updateUserInfoName(String userEmail, UserNameDto userNameDto) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                ).getUserInfo();

        if (userNameDto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name required");
        }

        userInfo.setName(userNameDto.getName());
        userInfoRepository.save(userInfo);
    }

    public void updateUserInfoDrinkAmount(String userEmail, UpdateDrinkAmountDto updateDrinkAmountDto) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                ).getUserInfo();

        if (updateDrinkAmountDto.getWeeklyAlcoholAmount() != null) {
            userInfo.setWeeklyAlcoholAmount(updateDrinkAmountDto.getWeeklyAlcoholAmount());
        }
        if (updateDrinkAmountDto.getAverageAlcoholAmount() != null) {
            userInfo.setAverageAlcoholAmount(updateDrinkAmountDto.getAverageAlcoholAmount());
        }

        userInfoRepository.save(userInfo);
    }

    public void setUserInfoFcmKey(String userEmail, FcmClientKeyRequestDto fcmClientKeyRequestDto) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                ).getUserInfo();

        if (fcmClientKeyRequestDto.getClientFcmKey() != null) {

            userInfo.setClientFcmKey(fcmClientKeyRequestDto.getClientFcmKey());
            userInfoRepository.save(userInfo);
        }
    }

    public ExistsFcmClientKeyRequestDto existsUserInfoFcmKey(String userEmail) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                ).getUserInfo();

        return new ExistsFcmClientKeyRequestDto(userInfo.getClientFcmKey() != null);
    }
}
