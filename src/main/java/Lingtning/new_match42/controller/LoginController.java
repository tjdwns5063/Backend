package Lingtning.new_match42.controller;

import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "LoginController")
@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "OAuth", description = "42 OAuth 인증 API")
public class LoginController {
    @GetMapping("/")
    @Operation(summary = "Login API", description = "42 로그인 후 JWT 반환", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 처리 완료")
    })
    public String login(@RequestParam("token") String token) {
        if (token == null)
            return "로그인 실패";
        return "로그인 되었습니다.";
    }
}
