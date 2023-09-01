package Lingtning.new_match42.service;

import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    // user에게 message를 보내는 함수
    public void sendChatMessage(User user, String message) {
        String token = user.getFcmToken();
        if (token == null) {
            throw new ResponseStatusException(BAD_REQUEST, "FCM 토큰이 없습니다.");
        }
        Notification notification = Notification.builder()
                .setTitle("매치42")
                .setBody(message)
                .build();

        Message fcmMessage = Message.builder()
                .setNotification(notification)
                .setToken(token)
                .build();

        try {
            firebaseMessaging.send(fcmMessage);
        } catch (FirebaseMessagingException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "FCM 메시지 전송에 실패했습니다.");
        }
        throw new ResponseStatusException(OK, "FCM 메시지 전송에 성공했습니다.");
    }

    public void subscribeToken(User user, String token) {
        if (user.getFcmToken() != null) {
            throw new ResponseStatusException(BAD_REQUEST, "이미 등록된 FCM 토큰이 있습니다.");
        }
        user.setFcmToken(token);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "FCM 토큰 등록에 실패했습니다.");
        }
        throw new ResponseStatusException(OK, "FCM 토큰 등록에 성공했습니다.");
    }
}
