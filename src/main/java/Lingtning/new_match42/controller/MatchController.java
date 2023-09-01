package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.ChatRequest;
import Lingtning.new_match42.dto.MatchRoomResponse;
import Lingtning.new_match42.dto.UserMatchInfoResponse;
import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.service.MatchService;
import Lingtning.new_match42.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/match")
@Tag(name = "Match", description = "매칭 관련 API")
public class MatchController {
    private final UserService userService;
    private final MatchService matchService;

    @Autowired
    public MatchController(UserService userService, MatchService matchService) {
        this.userService = userService;
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
        return matchService.startChatMatch(user, chatRequest);
    }

    @PostMapping("/chat/stop")
    @Operation(summary = "채팅 매칭 종료 API", description = "채팅 매칭을 종료하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "채팅 매칭 종료 완료")
    })
    public void stopChatMatch(Authentication authentication) {
        User user = userService.getUser(authentication);
        matchService.stopChatMatch(user);
    }

    /* /chat/start 로직에 반영하여 일단 여기는 주석 처리 */
//    @PostMapping("/chat/waiting")
//    @Operation(summary = "대기 중인 채팅 매칭 확인 API", description = "대기 중인 채팅 매칭이 있는지 확인하는 API", responses = {
//            @ApiResponse(responseCode = "200", description = "대기 중인 채팅 매칭 시작 완료")
//    })
//    public void waitingChatMatch(Authentication authentication, ChatRequest chatRequest) {
//        System.out.println(chatRequest.getCapacity());
//        User user = userService.getUser(authentication);
//        matchService.waitingMatchRoom(user, chatRequest);
//    }
}
