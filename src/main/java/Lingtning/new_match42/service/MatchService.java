package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.MatchRoomResponse;
import Lingtning.new_match42.dto.UserMatchInfoResponse;
import Lingtning.new_match42.entity.MatchList;
import Lingtning.new_match42.entity.MatchRoom;
import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.enums.MatchType;
import Lingtning.new_match42.repository.MatchRoomRepository;
import Lingtning.new_match42.repository.MatchListRepository;
import Lingtning.new_match42.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    private final UserRepository userRepository;
    private final MatchRoomRepository matchRoomRepository;
    private final MatchListRepository matchListRepository;

    private final UserService userService;

    @Autowired
    public MatchService(UserRepository userRepository, MatchRoomRepository matchRoomRepository, MatchListRepository matchListRepository, UserService userService) {
        this.userRepository = userRepository;
        this.matchRoomRepository = matchRoomRepository;
        this.matchListRepository = matchListRepository;
        this.userService = userService;
    }

    public UserMatchInfoResponse getMatchInfo(Authentication authentication) {
        User user = userService.getUser(authentication);
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

    public MatchRoomResponse startChatMatch(Authentication authentication) {
        return null;
    }

    public void stopChatMatch(Authentication authentication) {
    }
}
