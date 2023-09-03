package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.request.ChatRequest;
import Lingtning.new_match42.dto.request.MatchRequest;
import Lingtning.new_match42.dto.request.MealRequest;
import Lingtning.new_match42.dto.request.SubjectRequest;
import Lingtning.new_match42.dto.response.MatchRoomResponse;
import Lingtning.new_match42.dto.response.UserMatchInfoResponse;
import Lingtning.new_match42.entity.match.*;
import Lingtning.new_match42.entity.user.User;
import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MatchType;
import Lingtning.new_match42.repository.match.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j(topic = "MatchService")
@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {
    private final MatchRoomRepository matchRoomRepository;
    private final MatchListRepository matchListRepository;
    private final ChatOptionRepository chatOptionRepository;
    private final SubjectOptionRepository subjectOptionRepository;
    private final MealOptionRepository mealOptionRepository;

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

    public MatchRoom isMatched(User user, MatchType matchType) {
        List<MatchList> matchList = matchListRepository.findByUser_Id(user.getId());

        for (MatchList match : matchList) {
            MatchRoom matchRoom = match.getMatchRoom();

            if (matchRoom.getMatchStatus() == MatchStatus.WAITING && matchRoom.getMatchType() == matchType) {
                return matchRoom;
            }
        }

        return null;
    }

    private MatchRoom saveNewMatchRoom(MatchType matchType) {
        return matchRoomRepository.save(MatchRoom.builder()
                .size(1)
                .matchType(matchType)
                .matchStatus(MatchStatus.WAITING)
                .build());
    }

    MatchRoom chooseMatchRoom(MatchType matchType, MatchRequest matchRequest) {
        if (matchType == MatchType.CHAT) {
            ChatRequest chatRequest = (ChatRequest) matchRequest;
            ChatOption chatOption = chatOptionRepository.findTopByCapacity(chatRequest.getCapacity()).orElse(null);

            // 찾는 매칭 방이 없다면
            if (chatOption == null) {
                MatchRoom matchRoom = saveNewMatchRoom(matchType);
                chatOptionRepository.save(ChatOption.builder()
                        .matchRoom(matchRoom)
                        .capacity(chatRequest.getCapacity())
                        .build());

                return matchRoom;
            }
            // 찾는 매칭 방이 있다면
            MatchRoom matchRoom = chatOption.getMatchRoom();
            matchRoom.setSize(matchRoom.getSize() + 1);

            if (Objects.equals(matchRoom.getSize(), chatOption.getCapacity())) {
                matchRoom.setMatchStatus(MatchStatus.MATCHED);
            }

            return matchRoom;

        } else if (matchType == MatchType.SUBJECT) {
            SubjectRequest subjectRequest = (SubjectRequest) matchRequest;
            SubjectOption subjectOption = subjectOptionRepository.findTopByCapacityAndProject(subjectRequest.getCapacity(), subjectRequest.getProject()).orElse(null);

            // 찾는 매칭 방이 없다면
            if (subjectOption == null) {
                MatchRoom matchRoom = saveNewMatchRoom(matchType);
                subjectOptionRepository.save(SubjectOption.builder()
                        .matchRoom(matchRoom)
                        .capacity(subjectRequest.getCapacity())
                        .project(subjectRequest.getProject())
                        .build());

                return matchRoom;
            }
            // 찾는 매칭 방이 있다면
            MatchRoom matchRoom = subjectOption.getMatchRoom();
            matchRoom.setSize(matchRoom.getSize() + 1);

            if (Objects.equals(matchRoom.getSize(), subjectOption.getCapacity())) {
                matchRoom.setMatchStatus(MatchStatus.MATCHED);
            }

            return matchRoom;

        } else if (matchType == MatchType.MEAL) {
            MealRequest mealRequest = (MealRequest) matchRequest;
            MealOption mealOption = mealOptionRepository.findTopByCapacityAndMenu(mealRequest.getCapacity(), mealRequest.getMenu()).orElse(null);

            // 찾는 매칭 방이 없다면
            if (mealOption == null) {
                MatchRoom matchRoom = saveNewMatchRoom(matchType);
                mealOptionRepository.save(MealOption.builder()
                        .matchRoom(matchRoom)
                        .capacity(mealRequest.getCapacity())
                        .menu(mealRequest.getMenu())
                        .build());

                return matchRoom;
            }
            // 찾는 매칭 방이 있다면
            MatchRoom matchRoom = mealOption.getMatchRoom();
            matchRoom.setSize(matchRoom.getSize() + 1);

            if (Objects.equals(matchRoom.getSize(), mealOption.getCapacity())) {
                matchRoom.setMatchStatus(MatchStatus.MATCHED);
            }

            return matchRoom;

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "매칭 타입이 잘못되었습니다.");
        }
    }

    public MatchRoomResponse startMatch(User user, MatchRequest matchRequest, MatchType matchType) {
        log.info("startChatMatch: {}", matchRequest.getCapacity());
        // 이미 매칭중인 유저인지 확인
        if (isMatched(user, matchType) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 매칭중인 유저입니다.");
        }
        // 매칭 방 찾기
        MatchRoom matchRoom = null;
        try {
            // 매칭 방이 없으면 새로 만들고, 있으면 사이즈를 늘리고 매칭 상태를 바꿈
            matchRoom = chooseMatchRoom(matchType, matchRequest);
        } catch (Exception e) {
            log.error("chooseMatchRoom error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "매칭 방 찾기 에러");
        }

        try {
            // 매칭 중 이라면
            if (matchRoom.getMatchStatus().equals(MatchStatus.WAITING)) {
                matchRoom = matchRoomRepository.save(matchRoom);
                log.info("matchRoom: {}", matchRoom);
            // 매칭 완료 되었다면 매칭 리스트 추가 없이 바로 리턴
            } else if (matchRoom.getMatchStatus().equals(MatchStatus.MATCHED)) {
                return MatchRoomResponse.builder()
                        .id(matchRoom.getId())
                        .size(matchRoom.getSize())
                        .capacity(matchRequest.getCapacity())
                        .matchType(matchType.getKey())
                        .matchStatus("MATCHED")
                        .build();
            }
        } catch (Exception e) {
            log.error("matchRoomRepository.save(matchRoom) error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "매칭 룸 생성 에러");
        }

        try {
            // 매칭 리스트 추가
            matchListRepository.save(MatchList.builder()
                    .user(user)
                    .matchRoom(matchRoom)
                    .build());
        } catch (Exception e) {
            log.error("matchListRepository.save(matchList) error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "매칭 리스트 생성 에러");
        }

        return MatchRoomResponse.builder()
                .id(matchRoom.getId())
                .size(matchRoom.getSize())
                .capacity(matchRequest.getCapacity())
                .matchType(matchType.getKey())
                .matchStatus("WAITING")
                .build();
    }

    public void stopMatch(User user, MatchType matchType) {
        try {
            // 매칭중인 유저가 아니면 오류
            MatchRoom matchRoom = isMatched(user, matchType);
            if (matchRoom == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "매칭중인 유저가 아닙니다.");
            }

            matchListRepository.deleteByUser_IdAndMatchRoom_Id(user.getId(), matchRoom.getId());
            matchRoom.setSize(matchRoom.getSize() - 1);

            if (matchRoom.getSize() == 0) {
                matchRoomRepository.deleteById(matchRoom.getId());
            } else {
                matchRoomRepository.save(matchRoom);
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
