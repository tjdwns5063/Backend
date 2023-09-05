package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.response.MatchRoomResponse;
import Lingtning.new_match42.entity.match.MatchList;
import Lingtning.new_match42.entity.user.User;
import Lingtning.new_match42.repository.match.MatchListRepository;
import Lingtning.new_match42.repository.match.MatchRoomRepository;
import Lingtning.new_match42.repository.user.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j(topic = "FIREBASE_SERVICE")
@RequiredArgsConstructor
@Transactional
public class FirebaseService {
    private final MatchListRepository matchListRepository;
    private final MatchRoomRepository matchRoomRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    // user에게 message를 보내는 함수
    public ResponseEntity<?> sendChatMessage(String token, String message) {
        if (token == null) {
            throw new ResponseStatusException(BAD_REQUEST, "알림을 받을 대상에게 FCM 토큰이 없습니다.");
        }
        Notification notification = Notification.builder()
                .setTitle("매치42")
                .setBody(message)
                .build();

        MulticastMessage fcmMessage = MulticastMessage.builder()
                .setNotification(notification)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setContentAvailable(true)
                                .build())
                        .putHeader("apns-push-type", "background")
                        .putHeader("apns-priority", "5")
                        .putHeader("apns-topic", "com.lingtning.match42")
                        .build())
                .addToken(token)
                .build();

        try { // FCM 메시지를 보내는 함수
            firebaseMessaging.sendEachForMulticast(fcmMessage);
        } catch (FirebaseMessagingException e) {
            throw new ResponseStatusException(BAD_REQUEST, "FCM 메시지 전송에 실패했습니다. " + e.getMessage());
        }
        return ResponseEntity.ok("FCM 메시지 전송에 성공했습니다.");
    }

    // user들에게 한번에 message를 보내는 함수
    public ResponseEntity<?> broadCastChatMessage(List<String> tokens, String message, String firebaseId) {
        if (tokens == null || tokens.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "알림을 받을 대상에게 FCM 토큰이 없습니다.");
        }
        Notification notification = Notification.builder()
                .setTitle("매치42")
                .setBody(message)
                .build();

        MulticastMessage fcmMessage = MulticastMessage.builder()
                .putData("firebase-id", firebaseId)
                .setNotification(notification)
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setContentAvailable(true)
                                .build())
                        .putHeader("apns-push-type", "background")
                        .putHeader("apns-priority", "5")
                        .putHeader("apns-topic", "com.lingtning.match42")
                        .build())
                .addAllTokens(tokens)
                .build();

        try { // FCM 메시지를 보내는 함수
            firebaseMessaging.sendEachForMulticast(fcmMessage);
        } catch (FirebaseMessagingException e) {
            throw new ResponseStatusException(BAD_REQUEST, "FCM 메시지 전송에 실패했습니다. " + e.getMessage());
        }
        return ResponseEntity.ok("FCM 메시지 전송에 성공했습니다.");
    }

    public ResponseEntity<?> subscribeToken(User user, String token) {
        user.setFcmToken(token);
        try {
            sendChatMessage(user.getFcmToken(), "FCM 토큰 등록에 성공했습니다.");
            userRepository.save(user);
        } catch (Exception e) {
            user.setFcmToken(null);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "FCM 토큰 등록에 실패했습니다.");
        }
        return ResponseEntity.ok("FCM 토큰 등록에 성공했습니다.");
    }

    public void createRoomInFireBase(Long roomId, String matchType) {
        final Firestore client = FirestoreClient.getFirestore();

        // 데이터베이스에서 필요한 정보를 조회
        List<MatchList> matchLists = matchListRepository.findByMatchRoom_Id(roomId); // 모든 MatchList 레코드 조회


        List<Long> userIds = new ArrayList<>(); // 유저 서버 아이디를 담을 리스트
        List<String> userIntraList = new ArrayList<>(); // 유저 intra를 담을 리스트
        for (MatchList matchList : matchLists) {
            userIds.add(matchList.getUser().getId()); // 유저 아이디를 리스트에 추가
            userIntraList.add(matchList.getUser().getIntra()); // 유저 intra를 리스트에 추가
        }
        // 매칭 방 삭제
        matchRoomRepository.deleteById(roomId);

        // 채팅방 데이터 생성 및 Firestore에 추가
        Map<String, Object> chatRoomData = new HashMap<>();
        chatRoomData.put("lastMsg", new HashMap<String, Object>() {{
            put("date", null);
            put("message", null);
            put("sender", new HashMap<String, Object>() {{
                put("id", 0);
                put("intra", "");
                put("nickname", "");
                put("profile", "");
            }});
        }});

        // userIntraList를 String으로 변환.
        String userIntraAsString = userIntraList.stream()
                .collect(Collectors.joining(", "));
        // matchType에 따라 name 설정 확장성을 위해 3개 분기로 나눔.
        if ("CHAT".equals(matchType)) {
            chatRoomData.put("name", "수다방");
        } else if ("SUBJECT".equals(matchType)) {
            // SUBJECT인 경우, 유저들의 아이디로 설정
            chatRoomData.put("name", userIntraAsString);
        } else if ("MEAL".equals(matchType)) {
            // MEAL인 경우, 유저들의 아이디로 설정
            chatRoomData.put("name", userIntraAsString);
        }

        chatRoomData.put("open", Timestamp.now()); // 현재 타임스탬프 사용
        chatRoomData.put("type", matchType); // match_room 테이블의 match_type 값을 사용

        // users 배열 초기화=
        List<Long> users = new ArrayList<>();
        for (Long userId : userIds) {
            users.add(userId);
        }
        chatRoomData.put("users", users);

        // unread 배열 초기화==
        List<Long> unread = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            unread.add(0L);
        }
        chatRoomData.put("unread", unread);

        // rooms 컬렉션에 데이터 추가
        try {
            // 채팅방 생성
            ApiFuture<DocumentReference> result = client.collection("rooms").add(chatRoomData);
            String resultId = result.get().getId(); // 채팅방 ID
            log.info("Chat Room Created with ID: " + resultId);
            String resultPath = result.get().getPath(); // 채팅방 경로
            log.info("Chat Room Path: " + resultPath);

            // 매칭 알림 보내기
            List<String> tokens = new ArrayList<>();
            userRepository.findAllById(userIds).forEach(user -> {
                if (user.getFcmToken() != null) {
                    tokens.add(user.getFcmToken());
                }
            });
            broadCastChatMessage(tokens, "매칭이 성사되었습니다.", resultId);
            log.info("Chat Room Created with ID: " + result.get().getId());
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }

        log.info("Chat Room Created DONE");
    }


    //테스트로 저장한거 다 찍어보는 로직

    public List<Map<String, Object>> readAllDataFromRoomsCollection() {
        final Firestore client = FirestoreClient.getFirestore();
        List<Map<String, Object>> dataList = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = client.collection("rooms").get();

        try {
            QuerySnapshot snapshot = querySnapshot.get();
            List<QueryDocumentSnapshot> documents = snapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> data = document.getData();
                log.info("Firebase Data: " + data);
                // System.out.println("문서 데이터: " + data);
                dataList.add(data);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return dataList; // firebase 데이터 전체 리턴
    }

    public void createMatchInFireBase(MatchRoomResponse matchRoomResponse) {
        final Firestore client = FirestoreClient.getFirestore();
        String firebaseMatchId = matchRoomResponse.getFirebaseMatchId();

        DocumentReference match;
        try {
            if (firebaseMatchId == null) {
                match = client.collection("match").document();
                matchRoomRepository.findById(matchRoomResponse.getId()).ifPresent(matchRoom -> {
                    matchRoom.setFirebaseMatchId(match.getId());
                    matchRoomResponse.setFirebaseMatchId(match.getId());
                    matchRoomRepository.save(matchRoom);
                });
            } else {
                match = client.collection("match").document(firebaseMatchId);
            }

            // 매칭방 데이터 생성 및 Firestore에 추가
            Map<String, Object> matchData = new HashMap<>();
            matchData.put("id", matchRoomResponse.getId());
            matchData.put("size", matchRoomResponse.getSize());
            matchData.put("capacity", matchRoomResponse.getCapacity());
            matchData.put("matchType", matchRoomResponse.getMatchType());
            matchData.put("matchStatus", matchRoomResponse.getMatchStatus());

            ApiFuture<WriteResult> updateMatch = match.set(matchData);
            log.info("Update time : " + updateMatch.get().getUpdateTime());
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        }

        log.info("Match Created DONE");
    }
}

