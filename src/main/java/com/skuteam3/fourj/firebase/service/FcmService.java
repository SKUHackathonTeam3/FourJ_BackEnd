package com.skuteam3.fourj.firebase.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Service
@Slf4j
public class FcmService {

    private final FirebaseApp firebaseApp;
    private final ThreadPoolTaskScheduler taskScheduler;

    public void sendWebPushNotification(String token, String title, String body) {

        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {

            FirebaseMessaging.getInstance(firebaseApp).send(message);
        } catch (Exception e) {

            log.error("FcmService_sendWebPushNotification: " + e.getMessage(), e);
        }
    }


    // 하나의 작업을 생성할 때 마다 스케쥴 작업을 생성하는 것 vs 일정 시간마다 데이터 베이스에서 데이터를 조회하여
    //                                                   알림을 보내는 것
    public void schedulePush(LocalDateTime taskTime, int plusHour, String token, String title, String body) {

        LocalDateTime executionTime = taskTime.plusHours(plusHour);
        Date executeDate = java.sql.Timestamp.valueOf(executionTime);

        taskScheduler.schedule(() -> {
            sendWebPushNotification(token, title, body);
        }, executeDate);
    }

}
