package Lingtning.new_match42.controller;

import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "LoginController")
@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "OAuth", description = "42 OAuth 인증 API")
public class LoginController {
    @GetMapping("/success")
    @Operation(summary = "JWT 반환 경로", description = "42 로그인 성공 후 querty string 으로 JWT 반환 되는 경로", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 처리 완료")
    })
    public String loginSuccess(@RequestParam(required = false, value = "token") String token) {
        if (token == null)
            return "로그인 실패";
        return "로그인 되었습니다.";
    }

    @PostMapping("")
    @Operation(summary = "로그인 API", description = "42 로그인 시도 API", responses = {
            @ApiResponse(responseCode = "200", description = "이미 로그인 되어있음.")
    })
    public String login(ServletRequest request) {
        log.info("로그인 시도: " + request.getRequestId());
        return "이미 로그인 되어 있습니다.";
    }
}
