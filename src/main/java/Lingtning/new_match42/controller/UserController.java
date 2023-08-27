package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.UserResponseDto;
import Lingtning.new_match42.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회 API", description = "내 정보 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "내 정보 조회 완료")
    })
    public UserResponseDto getMe(Authentication authentication) {
        return userService.getMe(authentication);
    }

    @PostMapping("/interest")
    @Operation(summary = "관심사 추가 API", description = "관심사 추가 API", responses = {
            @ApiResponse(responseCode = "200", description = "관심사 추가 완료")
    })
    public UserResponseDto addInterest(Authentication authentication, @RequestParam String interest) {
        return userService.addInterest(authentication, interest);
    }

    @DeleteMapping("/interest")
    @Operation(summary = "관심사 삭제 API", description = "관심사 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "관심사 삭제 완료")
    })
    public UserResponseDto deleteInterest(Authentication authentication, @RequestParam String interest) {
        return userService.deleteInterest(authentication, interest);
    }

    @PostMapping("/block")
    @Operation(summary = "차단 유저 추가 API", description = "차단 유저 추가 API", responses = {
            @ApiResponse(responseCode = "200", description = "차단 유저 추가 완료")
    })
    public UserResponseDto addBlockUser(Authentication authentication, @RequestParam String blockUser) {
        return userService.addBlockUser(authentication, blockUser);
    }

    @DeleteMapping("/block")
    @Operation(summary = "차단 유저 삭제 API", description = "차단 유저 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "차단 유저 삭제 완료")
    })
    public UserResponseDto deleteBlockUser(Authentication authentication, @RequestParam String blockUser) {
        return userService.deleteBlockUser(authentication, blockUser);
    }
}
