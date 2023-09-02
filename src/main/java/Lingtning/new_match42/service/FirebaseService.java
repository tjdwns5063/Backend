package Lingtning.new_match42.service;

import Lingtning.new_match42.entity.MatchList;
import Lingtning.new_match42.entity.MatchRoom;
import Lingtning.new_match42.repository.MatchListRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class FirebaseService {

    @Autowired
    private MatchListRepository matchListRepository;

    public List<Map<String, Object>> createChatRoomInFireBase(Long roomId) {
        final Firestore client = FirestoreClient.getFirestore();
        List<Map<String, Object>> chatRoomDataList = new ArrayList<>();

        // 데이터베이스에서 필요한 정보를 조회
        List<MatchList> matchLists = matchListRepository.findByMatchRoom_Id(roomId); // 모든 MatchList 레코드 조회

        for (MatchList matchList : matchLists) { // 매개변수로 match_list의 id를 가져온다면 필요없을듯
            List<Integer> userIds = new ArrayList<>();
            userIds.add(Math.toIntExact(matchList.getUser().getId()));
        }

            // MatchRoom 정보 조회  매개변수로 match_list의 id를 가져온다면 필요없을듯
//            MatchRoom matchRoom = matchLists.get
            if (matchRoom == null) {
                continue;
            }

        // match_room_id를 기준으로 그룹화
        Map<Long, List<MatchList>> matchListsByMatchRoomId = matchLists.stream()
                .collect(Collectors.groupingBy(matchList -> matchList.getMatchRoom().getId()));

            // 채팅방 데이터 생성 및 Firestore에 추가
            Map<String, Object> chatRoomData = new HashMap<>();
            chatRoomData.put("lastMsg", new HashMap<String, Object>() {{
                put("data", null);
                put("message", null);
                put("sender", new HashMap<String, Object>() {{
                    put("id", null);
                    put("intra", "");
                    put("nickname", "");
                    put("profile", "");
                }});
            }});
            chatRoomData.put("name", "test");
            chatRoomData.put("open", Timestamp.now()); // 현재 타임스탬프 사용
            chatRoomData.put("type", matchRoom.getMatchType().toString()); // match_room 테이블의 match_type 값을 사용
            // unread 필드 초기화
            Map<String, Integer> unread = new HashMap<>();
            for (Integer userId : userIds) {
                unread.put(String.valueOf(userId), 0);
            }
            chatRoomData.put("unread", unread);

            // users 필드 초기화
            Map<String, Integer> users = new HashMap<>();
            for (Integer userId : userIds) {
                users.put(String.valueOf(userId), userId);
            }
            chatRoomData.put("users", users);

            // rooms 컬렉션에 데이터 추가
            ApiFuture<DocumentReference> result = client.collection("rooms").add(chatRoomData);
            try {
                DocumentReference documentReference = result.get();
                log.info("Chat Room Created with ID: " + documentReference.getId());
                chatRoomDataList.add(chatRoomData);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        log.info("Chat Room Created DONE");
        return chatRoomDataList;
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
}

