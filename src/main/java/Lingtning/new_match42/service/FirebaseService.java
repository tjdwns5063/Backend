package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.ChatRoomDto;
import Lingtning.new_match42.dto.ChatCreateDto;
import Lingtning.new_match42.dto.MatchDto;
import Lingtning.new_match42.entity.user.User;
import Lingtning.new_match42.repository.user.UserRepository;
import com.google.firebase.messaging.*;
import com.google.firebase.messaging.AndroidConfig.Priority;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j(topic = "FIREBASE_SERVICE")
@RequiredArgsConstructor
@Transactional
public class FirebaseService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    // user에게 message를 보내는 함수
    public ResponseEntity<?> sendChatMessage(ChatRoomDto dto, String message, User user) {
        if (user.getFcmToken() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "알림을 받을 대상에게 FCM 토큰이 없습니다.");
        }

        Notification notification = Notification.builder()
                .setTitle(dto.name())
                .setBody(message)
                .build();

        List<String> tokens = dto.userIds().stream().filter((Long id) -> !id.equals(user.getId()))
                .map((Long id) -> userRepository.findById(id).orElseThrow().getFcmToken()).toList();

        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(notification)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setPriority(Priority.HIGH)
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setContentAvailable(true)
                                .build())
                        .putHeader("apns-push-matchType", "background")
                        .putHeader("apns-priority", "5")
                        .putHeader("apns-topic", "com.lingtning.match42")
                        .build())
                .addAllTokens(tokens)
                .build();

        int cnt = 0;
        while (cnt < 10) {
            log.info("FCM 전송 시도" + cnt + "회");

            try {
                firebaseMessaging.sendEachForMulticast(multicastMessage);
                log.info("FCM 전송 성공!");

                return ResponseEntity.ok("FCM 메시지 전송에 성공했습니다.");
            } catch (FirebaseMessagingException ignored) {
            }
            ++cnt;
        }

        log.info("FCM 전송 실패!");
        throw new ResponseStatusException(BAD_REQUEST, "FCM 메시지 전송에 실패했습니다. ");
    }

    public ResponseEntity<?> subscribeToken(User user, String token) {
        user.setFcmToken(token);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            user.setFcmToken(null);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "FCM 토큰 등록에 실패했습니다.");
        }
        return ResponseEntity.ok("FCM 토큰 등록에 성공했습니다.");
    }

    public ResponseEntity<?> sendChatCreateMessage(ChatCreateDto dto) {
        dto.ids().forEach((id) -> {
            String token = userRepository.findById(id).orElseThrow().getFcmToken();
            if (token == null) {
                throw new ResponseStatusException(BAD_REQUEST, "알림을 받을 대상에게 FCM 토큰이 없습니다.");
            }
        });

        Notification notification = Notification.builder()
                .setTitle(dto.matchType() + " " + dto.capacity() + "인")
                .setBody("대화가 생성됐습니다")
                .build();

        List<String> tokens = dto.ids().stream()
                .map((Long id) -> userRepository.findById(id).orElseThrow().getFcmToken()).toList();

        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(notification)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setPriority(Priority.HIGH)
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setContentAvailable(true)
                                .build())
                        .putHeader("apns-push-matchType", "background")
                        .putHeader("apns-priority", "5")
                        .putHeader("apns-topic", "com.lingtning.match42")
                        .build())
                .addAllTokens(tokens)
                .build();

        int cnt = 0;
        while (cnt < 10) {
            log.info("FCM 전송 시도" + cnt + "회");

            try {
                firebaseMessaging.sendEachForMulticast(multicastMessage);
                log.info("FCM 전송 성공!");

                return ResponseEntity.ok("FCM 메시지 전송에 성공했습니다.");
            } catch (FirebaseMessagingException ignored) {
            }
            ++cnt;
        }

        log.info("FCM 전송 실패!");
        throw new ResponseStatusException(BAD_REQUEST, "FCM 메시지 전송에 실패했습니다. ");
    }

    public ResponseEntity<?> sendMatchMessage(MatchDto dto) {
        dto.ids().forEach((id) -> {
            String token = userRepository.findById(id).orElseThrow().getFcmToken();
            if (token == null) {
                throw new ResponseStatusException(BAD_REQUEST, "알림을 받을 대상에게 FCM 토큰이 없습니다.");
            }
        });

        List<String> intras = dto.ids().stream().map((id) ->
                userRepository.findById(id).orElseThrow().getIntra()
        ).toList();

        Notification notification = Notification.builder()
                .setTitle("Match42")
                .setBody(intras.toString() + " 님이 매칭되었습니다")
                .build();

        List<String> tokens = dto.ids().stream()
                .map((Long id) -> userRepository.findById(id).orElseThrow().getFcmToken()).toList();

        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(notification)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setPriority(Priority.HIGH)
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setContentAvailable(true)
                                .build())
                        .putHeader("apns-push-matchType", "background")
                        .putHeader("apns-priority", "5")
                        .putHeader("apns-topic", "com.lingtning.match42")
                        .build())
                .addAllTokens(tokens)
                .build();

        int cnt = 0;
        while (cnt < 10) {
            log.info("FCM 전송 시도" + cnt + "회");

            try {
                firebaseMessaging.sendEachForMulticast(multicastMessage);
                log.info("FCM 전송 성공!");

                return ResponseEntity.ok("FCM 메시지 전송에 성공했습니다.");
            } catch (FirebaseMessagingException ignored) {
            }
            ++cnt;
        }

        log.info("FCM 전송 실패!");
        throw new ResponseStatusException(BAD_REQUEST, "FCM 메시지 전송에 실패했습니다. ");
    }
}

