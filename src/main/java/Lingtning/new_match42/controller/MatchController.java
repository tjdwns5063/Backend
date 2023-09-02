package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.ChatRequest;
import Lingtning.new_match42.dto.MatchRoomResponse;
import Lingtning.new_match42.dto.UserMatchInfoResponse;
import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MatchType;
import Lingtning.new_match42.service.FirebaseService;
import Lingtning.new_match42.service.MatchService;
import Lingtning.new_match42.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/match")
@Tag(name = "Match", description = "매칭 관련 API")
public class MatchController {
    private final UserService userService;
    private final MatchService matchService;
    private FirebaseService firebaseService;

    @Autowired
    public MatchController(UserService userService, MatchService matchService, FirebaseService firebaseService) {
        this.userService = userService;
        this.firebaseService = firebaseService; // FirebaseService 주입
        this.matchService = matchService;

    }


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
    public MatchRoomResponse startChatMatch(Authentication authentication, @RequestBody ChatRequest chatRequest) {
        User user = userService.getUser(authentication);
        MatchRoomResponse matchRoomResponse = matchService.startChatMatch(user, chatRequest);
        if (matchRoomResponse.getMatchStatus().equals(MatchStatus.MATCHED.getKey())) {
            List<Map<String, Object>> firebaseDataList = firebaseService.createChatRoomInFireBase(matchRoomResponse.getId());
        }
        return matchRoomResponse;
    }

    @PostMapping("/chat/stop")
    @Operation(summary = "채팅 매칭 종료 API", description = "채팅 매칭을 종료하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "채팅 매칭 종료 완료")
    })
    public void stopChatMatch(Authentication authentication) {
        User user = userService.getUser(authentication);
        matchService.stopChatMatch(user);
    }

    @GetMapping("/chat/start/firebaseinit")
    @Operation(summary = "채팅시작 firebase 채팅방 생성", description = "처음으로 만든 API", responses = {
            @ApiResponse(responseCode = "200", description = "야호! 성공!!!")
    })
    public List<Map<String, Object>> helloFirebase() {
        // FirebaseService를 통한 작업 수행`
        List<Map<String, Object>> firebaseDataList = firebaseService.createChatRoomInFireBase();
        return firebaseDataList; // 모든 Firebase 데이터를 반환
    }
}

