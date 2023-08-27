package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.UserResponseDto;
import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String getIntra(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getIntra();
    }

    private User getUser(Authentication authentication) {
        String intra = getIntra(authentication);
        return userRepository.findByIntra(intra).orElseThrow(()
                -> new ResponseStatusException(NOT_FOUND, "유저를 찾을 수 없습니다."));
    }

    private UserResponseDto getUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .intra(user.getIntra())
                .email(user.getEmail())
                .blockUser1(user.getBlockUser1())
                .blockUser2(user.getBlockUser2())
                .blockUser3(user.getBlockUser3())
                .blockUser4(user.getBlockUser4())
                .blockUser5(user.getBlockUser5())
                .blockCount(user.getBlockCount())
                .interest1(user.getInterest1())
                .interest2(user.getInterest2())
                .interest3(user.getInterest3())
                .interest4(user.getInterest4())
                .interest5(user.getInterest5())
                .interestCount(user.getInterestCount())
                .role(user.getRole())
                .build();
    }

    public UserResponseDto getMe(Authentication authentication) {
        User user = getUser(authentication);

        return getUserResponseDto(user);
    }

    public UserResponseDto addInterest(Authentication authentication, String interest) {
        User user = getUser(authentication);
        String[] arrayInterest = {user.getInterest1(), user.getInterest2(), user.getInterest3(), user.getInterest4(), user.getInterest5()};

        for (String str : arrayInterest) {
            if (str != null && str.equals(interest)) {
                throw new ResponseStatusException(BAD_REQUEST, "이미 추가된 관심사입니다.");
            }
        }

        if (user.getInterest1() == null) {
            user.setInterest1(interest);
        } else if (user.getInterest2() == null) {
            user.setInterest2(interest);
        } else if (user.getInterest3() == null) {
            user.setInterest3(interest);
        } else if (user.getInterest4() == null) {
            user.setInterest4(interest);
        } else if (user.getInterest5() == null) {
            user.setInterest5(interest);
        } else {
            throw new ResponseStatusException(BAD_REQUEST, "관심사는 최대 5개까지 설정할 수 있습니다.");
        }

        user.setInterestCount(user.getInterestCount() + 1);
        userRepository.save(user);

        return getUserResponseDto(user);
    }

    public UserResponseDto deleteInterest(Authentication authentication, String interest) {
        User user = getUser(authentication);

        if (user.getInterest1() != null && user.getInterest1().equals(interest)) {
            user.setInterest1(null);
        } else if (user.getInterest2() != null && user.getInterest2().equals(interest)) {
            user.setInterest2(null);
        } else if (user.getInterest3() != null && user.getInterest3().equals(interest)) {
            user.setInterest3(null);
        } else if (user.getInterest4() != null && user.getInterest4().equals(interest)) {
            user.setInterest4(null);
        } else if (user.getInterest5() != null && user.getInterest5().equals(interest)) {
            user.setInterest5(null);
        } else {
            throw new ResponseStatusException(NOT_FOUND, "해당 관심사가 없습니다.");
        }

        user.setInterestCount(user.getInterestCount() - 1);
        userRepository.save(user);

        return getUserResponseDto(user);
    }

    public UserResponseDto addBlockUser(Authentication authentication, String blockUser) {
        User user = getUser(authentication);
        String[] arrayBlockUser = {user.getBlockUser1(), user.getBlockUser2(), user.getBlockUser3(), user.getBlockUser4(), user.getBlockUser5()};

        for (String str : arrayBlockUser) {
            if (str != null && str.equals(blockUser)) {
                throw new ResponseStatusException(BAD_REQUEST, "이미 차단된 유저입니다.");
            }
        }

        if (user.getBlockUser1() == null) {
            user.setBlockUser1(blockUser);
        } else if (user.getBlockUser2() == null) {
            user.setBlockUser2(blockUser);
        } else if (user.getBlockUser3() == null) {
            user.setBlockUser3(blockUser);
        } else if (user.getBlockUser4() == null) {
            user.setBlockUser4(blockUser);
        } else if (user.getBlockUser5() == null) {
            user.setBlockUser5(blockUser);
        } else {
            throw new ResponseStatusException(BAD_REQUEST, "차단할 수 있는 유저는 최대 5명까지입니다.");
        }

        user.setBlockCount(user.getBlockCount() + 1);
        userRepository.save(user);

        return getUserResponseDto(user);
    }

    public UserResponseDto deleteBlockUser(Authentication authentication, String blockUser) {
        User user = getUser(authentication);

        if (user.getBlockUser1() != null && user.getBlockUser1().equals(blockUser)) {
            user.setBlockUser1(null);
        } else if (user.getBlockUser2() != null && user.getBlockUser2().equals(blockUser)) {
            user.setBlockUser2(null);
        } else if (user.getBlockUser3() != null && user.getBlockUser3().equals(blockUser)) {
            user.setBlockUser3(null);
        } else if (user.getBlockUser4() != null && user.getBlockUser4().equals(blockUser)) {
            user.setBlockUser4(null);
        } else if (user.getBlockUser5() != null && user.getBlockUser5().equals(blockUser)) {
            user.setBlockUser5(null);
        } else {
            throw new ResponseStatusException(NOT_FOUND, "해당 유저를 차단하고 있지 않습니다.");
        }

        user.setBlockCount(user.getBlockCount() - 1);
        userRepository.save(user);

        return getUserResponseDto(user);
    }
}
