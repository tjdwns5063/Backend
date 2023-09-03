package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.request.ChatRequest;
import Lingtning.new_match42.dto.request.MealRequest;
import Lingtning.new_match42.dto.request.SubjectRequest;
import Lingtning.new_match42.dto.response.MatchRoomResponse;
import Lingtning.new_match42.dto.response.UserMatchInfoResponse;
import Lingtning.new_match42.entity.user.User;
import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MatchType;
import Lingtning.new_match42.service.FirebaseService;
import Lingtning.new_match42.service.MatchService;
import Lingtning.new_match42.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
@Tag(name = "Match", description = "매칭 관련 API")
public class MatchController {
    private final UserService userService;
    private final MatchService matchService;
    private final FirebaseService firebaseService;

    @GetMapping("/me")
    @Operation(summary = "매칭 정보 반환 API", description = "현재 매칭 정보를 반환하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "매칭 정보 반환 완료")
    })
    public UserMatchInfoResponse getMatchInfo(Authentication authentication) {
        User user = userService.getUser(authentication);
        return matchService.getMatchInfo(user);
    }

    @GetMapping("/room/{id}")
    @Operation(summary = "매칭 방 정보 반환 API", description = "매칭 방 정보를 반환하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "매칭 방 정보 반환 완료")
    })
    public MatchRoomResponse getMatchRoomInfo(@PathVariable Long id) {
        return matchService.getMatchRoomInfo(id);
    }


    @PostMapping("/chat/start")
    @Operation(summary = "채팅 매칭 시작 API", description = "채팅 매칭을 시작하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "채팅 매칭 시작 완료")
    })
    public MatchRoomResponse startChatMatch(Authentication authentication, @RequestBody ChatRequest matchRequest) {
        User user = userService.getUser(authentication);
        MatchRoomResponse matchRoomResponse = matchService.startMatch(user, matchRequest, MatchType.CHAT);
        if (matchRoomResponse.getMatchStatus().equals(MatchStatus.MATCHED.getKey())) {
            firebaseService.createRoomInFireBase(matchRoomResponse.getId(), matchRoomResponse.getMatchType());
        }
        return matchRoomResponse;
    }

    @PostMapping("/chat/stop")
    @Operation(summary = "채팅 매칭 종료 API", description = "채팅 매칭을 종료하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "채팅 매칭 종료 완료")
    })
    public void stopChatMatch(Authentication authentication) {
        User user = userService.getUser(authentication);
        matchService.stopMatch(user, MatchType.CHAT);
    }

    @PostMapping("/subject/start")
    @Operation(summary = "과제 매칭 시작 API", description = "과제 매칭을 시작하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "과제 매칭 시작 완료")
    })
    public MatchRoomResponse startSubjectMatch(Authentication authentication, @RequestBody SubjectRequest subjectRequest) {
        User user = userService.getUser(authentication);
        MatchRoomResponse matchRoomResponse = matchService.startMatch(user, subjectRequest, MatchType.SUBJECT);
        if (matchRoomResponse.getMatchStatus().equals(MatchStatus.MATCHED.getKey())) {
            firebaseService.createRoomInFireBase(matchRoomResponse.getId(), matchRoomResponse.getMatchType());
        }
        return matchRoomResponse;
    }

    @PostMapping("/subject/stop")
    @Operation(summary = "과제 매칭 종료 API", description = "과제 매칭을 종료하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "과제 매칭 종료 완료")
    })
    public void stopSubjectMatch(Authentication authentication) {
        User user = userService.getUser(authentication);
        matchService.stopMatch(user, MatchType.SUBJECT);
    }

    @PostMapping("/meal/start")
    @Operation(summary = "식사 매칭 시작 API", description = "식사 매칭을 시작하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "식사 매칭 시작 완료")
    })
    public MatchRoomResponse startMealMatch(Authentication authentication, @RequestBody MealRequest mealRequest) {
        User user = userService.getUser(authentication);
        MatchRoomResponse matchRoomResponse = matchService.startMatch(user, mealRequest, MatchType.MEAL);
        if (matchRoomResponse.getMatchStatus().equals(MatchStatus.MATCHED.getKey())) {
            firebaseService.createRoomInFireBase(matchRoomResponse.getId(), matchRoomResponse.getMatchType());
        }
        return matchRoomResponse;
    }

    @PostMapping("/meal/stop")
    @Operation(summary = "식사 매칭 종료 API", description = "식사 매칭을 종료하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "식사 매칭 종료 완료")
    })
    public void stopMealMatch(Authentication authentication) {
        User user = userService.getUser(authentication);
        matchService.stopMatch(user, MatchType.MEAL);
    }
}

