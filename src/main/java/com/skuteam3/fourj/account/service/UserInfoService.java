package com.skuteam3.fourj.account.service;

import com.skuteam3.fourj.abti.repository.AbtiRepository;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.dto.FcmClientKeyRequestDto;
import com.skuteam3.fourj.account.dto.UpdateDrinkAmountDto;
import com.skuteam3.fourj.account.dto.UpdateNameDto;
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

    public void updateUserInfoName(String userEmail, UpdateNameDto updateNameDto) {

        UserInfo userInfo = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
                ).getUserInfo();

        if (updateNameDto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name required");
        }

        userInfo.setName(updateNameDto.getName());
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
}
