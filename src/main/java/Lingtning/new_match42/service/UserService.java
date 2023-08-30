package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.UserInterestResponse;
import Lingtning.new_match42.dto.UserResponse;
import Lingtning.new_match42.entity.Interest;
import Lingtning.new_match42.entity.User;
import Lingtning.new_match42.entity.UserConnectBlockUser;
import Lingtning.new_match42.entity.UserConnectInterest;
import Lingtning.new_match42.repository.InterestRepository;
import Lingtning.new_match42.repository.UserConnectBlockUserRepository;
import Lingtning.new_match42.repository.UserConnectInterestRepository;
import Lingtning.new_match42.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j(topic = "UserService")
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final UserConnectInterestRepository userConnectInterestRepository;
    private final UserConnectBlockUserRepository userConnectBlockUserRepository;

    public UserService(UserRepository userRepository, InterestRepository interestRepository, UserConnectInterestRepository userConnectInterestRepository, UserConnectBlockUserRepository userConnectBlockUserRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
        this.userConnectInterestRepository = userConnectInterestRepository;
        this.userConnectBlockUserRepository = userConnectBlockUserRepository;
    }

    // 유저가 존재하는지 확인하고 정보 반환
    public User getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userRepository.findById(user.getId()).orElseThrow(()
                -> new ResponseStatusException(NOT_FOUND, "유저를 찾을 수 없습니다."));
    }

    public UserResponse getUserResponse(User user) {
        List<String> interestList = new ArrayList<>();
        List<String> blockUserList = new ArrayList<>();

        List<UserConnectInterest> connectInterestList = user.getUserConnectInterest();
        for (UserConnectInterest connectInterest : connectInterestList) {
            interestList.add(connectInterest.getInterest().getKeyword());
        }

        List<UserConnectBlockUser> connectBlockUserList = user.getUserConnectBlockUser();
        for (UserConnectBlockUser connectBlockUser : connectBlockUserList) {
            blockUserList.add(connectBlockUser.getBlockUser().getIntra());
        }
        return UserResponse.builder()
                .id(user.getId())
                .intra(user.getIntra())
                .email(user.getEmail())
                .interests(interestList)
                .blockUsers(blockUserList)
                .blockCount(user.getBlockCount())
                .role(user.getRole())
                .build();
    }

    public UserResponse getMe(Authentication authentication) {
        User userMe = getUser(authentication);

        return getUserResponse(userMe);
    }

    public UserResponse putInterest(Authentication authentication, List<String> interests) {
        User userMe = getUser(authentication);
        userConnectInterestRepository.deleteAll(userMe.getUserConnectInterest());
        if (interests.size() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "관심사는 최대 5개까지 설정할 수 있습니다.");
        }
        List<UserConnectInterest> connectInterestList = new ArrayList<>();

        for (String interest : interests) {
            Interest findInterest = interestRepository.findByKeyword(interest).orElse(null);
            if (findInterest == null) {
                findInterest = interestRepository.save(
                    Interest.builder()
                    .keyword(interest)
                    .build());
            }
            UserConnectInterest userConnectInterest = UserConnectInterest.builder()
                .user(userMe)
                .interest(findInterest)
                .build();
            userConnectInterestRepository.save(userConnectInterest);
            connectInterestList.add(userConnectInterest);
            log.info("interest: " + interest);
        }

        userMe.setUserConnectInterest(connectInterestList);

        userRepository.save(userMe);

        return getUserResponse(userMe);
    }

    public UserResponse deleteInterest(Authentication authentication, String interest) {
        User userMe = getUser(authentication);
        List<UserConnectInterest> connectInterestList = userMe.getUserConnectInterest();

        for (UserConnectInterest connectInterest : connectInterestList) {
            if (connectInterest.getInterest().getKeyword().equals(interest)) {
                userConnectInterestRepository.delete(connectInterest);
                userRepository.save(userMe);
                return getUserResponse(userMe);
            }
        }

        throw new ResponseStatusException(NOT_FOUND, "해당 관심사가 없습니다.");
    }

    public UserResponse addBlockUser(Authentication authentication, String blockUser) {
        User userMe = getUser(authentication);
        List<UserConnectBlockUser> connectBlockUserList = userMe.getUserConnectBlockUser();

        for (UserConnectBlockUser connectBlockUser : connectBlockUserList) {
            if (connectBlockUser.getBlockUser().getIntra().equals(blockUser)) {
                throw new ResponseStatusException(BAD_REQUEST, "이미 차단된 유저입니다.");
            }
        }

        if (userMe.getBlockCount() == 5) {
            throw new ResponseStatusException(BAD_REQUEST, "차단할 수 있는 유저는 최대 5명까지입니다.");
        }

        User findBlockUser = userRepository.findByIntra(blockUser).orElseThrow(()
                -> new ResponseStatusException(NOT_FOUND, "차단할 유저를 찾을 수 없습니다."));

        UserConnectBlockUser connectBlockUser = UserConnectBlockUser
            .builder()
            .user(userMe)
            .blockUser(findBlockUser)
            .build();
        userMe.setBlockCount(userMe.getBlockCount() + 1);

        connectBlockUserList.add(connectBlockUser);
        userMe.setUserConnectBlockUser(connectBlockUserList);

        userConnectBlockUserRepository.save(connectBlockUser);
        userRepository.save(userMe);

        return getUserResponse(userMe);
    }

    public UserResponse deleteBlockUser(Authentication authentication, String blockUser) {
        User userMe = getUser(authentication);
        List<UserConnectBlockUser> connectBlockUserList = userMe.getUserConnectBlockUser();

        for (UserConnectBlockUser connectBlockUser : connectBlockUserList) {
            if (connectBlockUser.getBlockUser() == null) {
                continue;
            }
            if (connectBlockUser.getBlockUser().getIntra().equals(blockUser)) {
                userConnectBlockUserRepository.delete(connectBlockUser);
                userMe.setBlockCount(userMe.getBlockCount() - 1);
                connectBlockUserList.remove(connectBlockUser);
                userMe.setUserConnectBlockUser(connectBlockUserList);
                userRepository.save(userMe);
                return getUserResponse(userMe);
            }
        }

        throw new ResponseStatusException(NOT_FOUND, "해당 유저를 차단하고 있지 않습니다.");
    }

    public UserInterestResponse getInterests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(NOT_FOUND, "유저를 찾을 수 없습니다."));

        List<String> interestList = new ArrayList<>();
        List<UserConnectInterest> connectInterestList = user.getUserConnectInterest();

        for (UserConnectInterest connectInterest : connectInterestList) {
            interestList.add(connectInterest.getInterest().getKeyword());
        }

        return UserInterestResponse.builder()
                .id(user.getId())
                .intra(user.getIntra())
                .interests(interestList)
                .build();
    }
}
