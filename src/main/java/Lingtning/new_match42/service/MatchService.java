package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.ChatRequest;
import Lingtning.new_match42.dto.MatchRoomResponse;
import Lingtning.new_match42.dto.UserMatchInfoResponse;
import Lingtning.new_match42.entity.MatchList;
import Lingtning.new_match42.entity.MatchRoom;
import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MatchType;
import Lingtning.new_match42.repository.MatchRoomRepository;
import Lingtning.new_match42.repository.MatchListRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j(topic = "MatchService")
@Service
@Transactional
public class MatchService {
    private final MatchRoomRepository matchRoomRepository;
    private final MatchListRepository matchListRepository;

    @Autowired
    public MatchService(MatchRoomRepository matchRoomRepository, MatchListRepository matchListRepository) {
        this.matchRoomRepository = matchRoomRepository;
        this.matchListRepository = matchListRepository;
    }

    public UserMatchInfoResponse getMatchInfo(User user) {
        List<MatchList> matchList = matchListRepository.findByUser_Id(user.getId());

        Long mealMatchId = 0L;
        Long subjectMatchId = 0L;
        Long chatMatchId = 0L;

        for (MatchList match : matchList) {
            MatchRoom matchRoom = match.getMatchRoom();

            if (matchRoom.getMatchType() == MatchType.MEAL) {
                mealMatchId = matchRoom.getId();
            } else if (matchRoom.getMatchType() == MatchType.SUBJECT) {
                subjectMatchId = matchRoom.getId();
            } else if (matchRoom.getMatchType() == MatchType.CHAT) {
                chatMatchId = matchRoom.getId();
            }
        }

        return UserMatchInfoResponse.builder()
                .id(user.getId())
                .mealMatchId(mealMatchId)
                .subjectMatchId(subjectMatchId)
                .chatMatchId(chatMatchId)
                .build();
    }

    public boolean isChatMatched(User user) {
        List<MatchList> matchList = matchListRepository.findByUser_Id(user.getId());

        for (MatchList match : matchList) {
            MatchRoom matchRoom = match.getMatchRoom();

            if (matchRoom.getMatchStatus() == MatchStatus.WAITING && matchRoom.getMatchType() == MatchType.CHAT) {
                return true;
            }
        }

        return false;
    }

    public MatchRoom findChatMatch(User user, Integer capacity) {
        MatchRoom matchRooms = matchRoomRepository.findTopByMatchTypeAndMatchStatusAndCapacityOrderByCreatedDate(MatchType.CHAT, MatchStatus.WAITING, capacity).orElse(null);
        return null;
    }

    public MatchRoomResponse startChatMatch(User user, ChatRequest chatRequest) {
        log.info("startChatMatch: {}", chatRequest.getCapacity());
        // 이미 매칭중인 유저인지 확인
        if (isChatMatched(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 매칭중인 유저입니다.");
        }
        // 매칭 방 찾기
        MatchRoom matchRoom = matchRoomRepository.findTopByMatchTypeAndMatchStatusAndCapacityOrderByCreatedDate(
                MatchType.CHAT, MatchStatus.WAITING, chatRequest.getCapacity()).orElse(null);
        if (matchRoom != null) { // 매칭 방을 찾았다면
            log.info("matchRoom: {}", matchRoom.getId());
            matchRoom.setSize(matchRoom.getSize() + 1);
            if (Objects.equals(matchRoom.getSize(), matchRoom.getCapacity())) {
                matchRoom.setMatchStatus(MatchStatus.MATCHED);
            }
        } else { // 찾지 못했다면
            matchRoom = MatchRoom.builder()
                    .size(1)
                    .capacity(chatRequest.getCapacity())
                    .matchType(MatchType.CHAT)
                    .matchStatus(MatchStatus.WAITING)
                    .build();
        }
        try {
            if (matchRoom.getMatchStatus().equals(MatchStatus.WAITING)) {
                matchRoom = matchRoomRepository.save(matchRoom);
            } else if (matchRoom.getMatchStatus().equals(MatchStatus.MATCHED)) {
                matchRoomRepository.deleteById(matchRoom.getId());
                return MatchRoomResponse.builder()
                        .id(matchRoom.getId())
                        .size(matchRoom.getSize())
                        .capacity(matchRoom.getCapacity())
                        .matchType("CHAT")
                        .matchStatus("MATCHED")
                        .build();
            }
        } catch (Exception e) {
            log.error("matchRoomRepository.save(matchRoom) error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "매칭 생성 에러");
        }
        if (matchListRepository.findByMatchRoom_Id(matchRoom.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 매칭중인 유저입니다.");
        }
        MatchList matchList = MatchList.builder()
                .user(user)
                .matchRoom(matchRoom)
                .build();
        try {
            matchListRepository.save(matchList);
        } catch (Exception e) {
            log.error("matchListRepository.save(matchList) error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "매칭 리스트 생성 에러");
        }

        return MatchRoomResponse.builder()
                .id(matchRoom.getId())
                .size(matchRoom.getSize())
                .capacity(matchRoom.getCapacity())
                .matchType("CHAT")
                .matchStatus("WAITING")
                .build();
    }

    public void stopChatMatch(User user) {
        try {
            // 매칭중인 유저가 아니면 오류
            if (!isChatMatched(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "매칭중인 유저가 아닙니다.");
            }
            // 유저를 기준으로 매치 룸을 찾음
            List<MatchList> userMatchLists = matchListRepository.findByUser_Id(user.getId());
            for (MatchList matchList : userMatchLists) {
                MatchRoom matchRoom = matchList.getMatchRoom();
                // 매칭 상태가 'WAITING'이고 에는 해당 유저의 매칭리스트 삭제, 사이즈에 맞춰 매치 룸도 삭제
                if (matchRoom.getMatchStatus() == MatchStatus.WAITING) {
                    if (matchRoom.getSize() == 1) { //사이즈가 1인 경우, matchRoom에서도 삭제
                        matchListRepository.deleteById(matchList.getId());
                        matchRoomRepository.deleteById(matchRoom.getId());
                    } else { //사이즈를 1 줄임
                        matchRoom.setSize(matchRoom.getSize() - 1);
                        matchListRepository.deleteById(matchList.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("stopChatMatch error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "매칭 중단 에러");
        }

    }


    public MatchRoomResponse getMatchRoomInfo(Long id) {
        return null;
    }
}
