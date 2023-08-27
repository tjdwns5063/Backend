package Lingtning.new_match42.controller;

import Lingtning.new_match42.dto.UserResponseDto;
import Lingtning.new_match42.service.UserService;
import io.jsonwebtoken.Claims;
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
    public UserResponseDto getMe(Authentication authentication) {
        return userService.getMe(authentication);
    }

    @PostMapping("/interest")
    public UserResponseDto addInterest(Authentication authentication, @RequestParam String interest) {
        return userService.addInterest(authentication, interest);
    }

    @DeleteMapping("/interest")
    public UserResponseDto deleteInterest(Authentication authentication, @RequestParam String interest) {
        return userService.deleteInterest(authentication, interest);
    }

    @PostMapping("/block")
    public UserResponseDto addBlockUser(Authentication authentication, @RequestParam String blockUser) {
        return userService.addBlockUser(authentication, blockUser);
    }

    @DeleteMapping("/block")
    public UserResponseDto deleteBlockUser(Authentication authentication, @RequestParam String blockUser) {
        return userService.deleteBlockUser(authentication, blockUser);
    }
}
