package Lingtning.new_match42.service;

import Lingtning.new_match42.dto.ReportDto;
import Lingtning.new_match42.dto.response.UserInterestResponse;
import Lingtning.new_match42.dto.response.UserResponse;
import Lingtning.new_match42.entity.user.Interest;
import Lingtning.new_match42.entity.user.Report;
import Lingtning.new_match42.entity.user.User;
import Lingtning.new_match42.entity.user.UserConnectBlockUser;
import Lingtning.new_match42.entity.user.UserConnectInterest;
import Lingtning.new_match42.repository.user.InterestRepository;
import Lingtning.new_match42.repository.user.ReportRepository;
import Lingtning.new_match42.repository.user.UserConnectBlockUserRepository;
import Lingtning.new_match42.repository.user.UserConnectInterestRepository;
import Lingtning.new_match42.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j(topic = "UserService")
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final UserConnectInterestRepository userConnectInterestRepository;
    private final UserConnectBlockUserRepository userConnectBlockUserRepository;
    private final ReportRepository reportRepository;

    public UserService(UserRepository userRepository, InterestRepository interestRepository,
                       UserConnectInterestRepository userConnectInterestRepository,
                       UserConnectBlockUserRepository userConnectBlockUserRepository,
                       ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
        this.userConnectInterestRepository = userConnectInterestRepository;
        this.userConnectBlockUserRepository = userConnectBlockUserRepository;
        this.reportRepository = reportRepository;
    }

    // 로그인 했는지 확인하고 정보 반환
    public User getUser(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            return userRepository.findById(user.getId()).orElseThrow(()
                    -> new ResponseStatusException(NOT_FOUND, "유저를 찾을 수 없습니다!"));
        } catch (Exception e) {
            throw new ResponseStatusException(NOT_FOUND, "로그인 되어 있지 않습니다.");
        }
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(NOT_FOUND, "유저를 찾을 수 없습니다."));
    }

    public UserResponse getUserResponse(User user) {
        User user_ = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "유저를 찾을 수 없습니다."));

        List<String> interestList = new ArrayList<>();
        List<String> blockUserList = new ArrayList<>();

        List<UserConnectInterest> connectInterestList = userConnectInterestRepository.findByUser_Id(user.getId());
        for (UserConnectInterest connectInterest : connectInterestList) {
            interestList.add(connectInterest.getInterest().getKeyword());
        }

        List<UserConnectBlockUser> connectBlockUserList = userConnectBlockUserRepository.findByUser_Id(user.getId());
        for (UserConnectBlockUser connectBlockUser : connectBlockUserList) {
            blockUserList.add(connectBlockUser.getBlockUser().getIntra());
        }
        return UserResponse.builder()
                .id(user_.getId())
                .intra(user_.getIntra())
                .email(user_.getEmail())
                .interests(interestList)
                .blockUsers(blockUserList)
                .blockCount(user_.getBlockCount())
                .role(user_.getRole())
                .reportCount(user_.getReports().size())
                .build();
    }

    public UserResponse getMe(User user) {
        return getUserResponse(user);
    }

    public UserResponse putInterest(User user, List<String> interests) {
        if (interests == null) {
            throw new ResponseStatusException(BAD_REQUEST, "관심사를 설정해 주세요.");
        }

        try {
            List<UserConnectInterest> connectInterest = userConnectInterestRepository.findByUser_Id(user.getId());
            userConnectInterestRepository.deleteAll(connectInterest);
        } catch (Exception e) {
            log.info("delete: " + e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, "관심사를 설정할 수 없습니다.");
        }

        if (interests.size() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "관심사는 최대 5개까지 설정할 수 있습니다.");
        }
        List<UserConnectInterest> connectInterestList = new ArrayList<>();

        try {
            for (String interest : interests) {
                Interest findInterest = interestRepository.findByKeyword(interest).orElse(null);
                if (findInterest == null) {
                    interestRepository.save(Interest.builder()
                            .keyword(interest)
                            .build());
                    findInterest = interestRepository.findByKeyword(interest).orElseThrow(()
                            -> new ResponseStatusException(NOT_FOUND, "관심사를 저장 할 수 없습니다."));
                }
                UserConnectInterest userConnectInterest = UserConnectInterest.builder()
                        .user(user)
                        .interest(findInterest)
                        .build();
                log.info("userConnectInterest: " + userConnectInterest);
                log.info("user: " + user);
                log.info("findInterest: " + findInterest);
                userConnectInterestRepository.save(userConnectInterest);
                connectInterestList.add(userConnectInterest);
                log.info("interest: " + interest);
            }
            user.setUserConnectInterest(connectInterestList);

            userRepository.save(user);
        } catch (Exception e) {
            log.info("save: " + e.getMessage());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "저장 중에 에러 발생");
        }

        return getUserResponse(user);
    }

    public UserResponse deleteInterest(User user, String interest) {
        List<UserConnectInterest> connectInterestList = userConnectInterestRepository.findByUser_Id(user.getId());

        if (interest == null) {
            throw new ResponseStatusException(BAD_REQUEST, "잘못된 요청입니다.");
        }
        try {
            for (UserConnectInterest connectInterest : connectInterestList) {
                if (connectInterest.getInterest().getKeyword().equals(interest)) {
                    userConnectInterestRepository.delete(connectInterest);
                    userRepository.save(user);
                    return getUserResponse(user);
                }
            }
        } catch (Exception e) {
            log.info("delete: " + e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, "잘못된 요청입니다.");
        }

        throw new ResponseStatusException(NOT_FOUND, "해당 관심사가 없습니다.");
    }

    public UserResponse addBlockUser(User user, String blockUser) {
        if (blockUser == null) {
            throw new ResponseStatusException(BAD_REQUEST, "잘못된 요청입니다.");
        } else if (blockUser.equals(user.getIntra())) {
            throw new ResponseStatusException(BAD_REQUEST, "자기 자신을 차단할 수 없습니다.");
        }

        List<UserConnectBlockUser> connectBlockUserList = userConnectBlockUserRepository.findByUser_Id(user.getId());
        for (UserConnectBlockUser connectBlockUser : connectBlockUserList) {
            if (connectBlockUser.getBlockUser().getIntra().equals(blockUser)) {
                throw new ResponseStatusException(BAD_REQUEST, "이미 차단된 유저입니다.");
            }
        }

        if (user.getBlockCount() == 5) {
            throw new ResponseStatusException(BAD_REQUEST, "차단할 수 있는 유저는 최대 5명까지입니다.");
        }

        User findBlockUser = userRepository.findByIntra(blockUser).orElseThrow(()
                -> new ResponseStatusException(NOT_FOUND, "차단할 유저를 찾을 수 없습니다."));

        UserConnectBlockUser connectBlockUser = UserConnectBlockUser
                .builder()
                .user(user)
                .blockUser(findBlockUser)
                .build();
        user.setBlockCount(user.getBlockCount() + 1);
        connectBlockUserList.add(connectBlockUser);
        user.setUserConnectBlockUser(connectBlockUserList);
        try {
            userConnectBlockUserRepository.save(connectBlockUser);
            userRepository.save(user);
        } catch (Exception e) {
            log.info("save: " + e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, "잘못된 요청입니다.");
        }

        return getUserResponse(user);
    }

    public UserResponse deleteBlockUser(User user, String blockUser) {
        List<UserConnectBlockUser> connectBlockUserList = userConnectBlockUserRepository.findByUser_Id(user.getId());

        if (blockUser == null) {
            throw new ResponseStatusException(BAD_REQUEST, "잘못된 요청입니다.");
        } else if (blockUser.equals(user.getIntra())) {
            throw new ResponseStatusException(BAD_REQUEST, "자기 자신을 차단 해제 할 수 없습니다.");
        }

        log.info("blockUser: " + blockUser);
        try {
            for (UserConnectBlockUser connectBlockUser : connectBlockUserList) {
                if (connectBlockUser.getBlockUser().getIntra().equals(blockUser)) {
                    log.info("connectBlockUser: " + connectBlockUser);
                    userConnectBlockUserRepository.deleteById(connectBlockUser.getId());
                    user.setBlockCount(user.getBlockCount() - 1);
                    userRepository.save(user);
                    connectBlockUserList.remove(connectBlockUser);
                    user.setUserConnectBlockUser(connectBlockUserList);
                    return getUserResponse(user);
                }
            }
        } catch (Exception e) {
            log.info("delete: " + e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, "차단을 해제할 수 없습니다.");
        }

        throw new ResponseStatusException(NOT_FOUND, "해당 유저를 차단하고 있지 않습니다.");
    }

    public UserInterestResponse getInterests(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "잘못된 요청입니다.");
        }
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(NOT_FOUND, "유저를 찾을 수 없습니다."));

        List<String> interestList = new ArrayList<>();
        List<UserConnectInterest> connectInterestList = userConnectInterestRepository.findByUser_Id(user.getId());

        for (UserConnectInterest connectInterest : connectInterestList) {
            interestList.add(connectInterest.getInterest().getKeyword());
        }

        return UserInterestResponse.builder()
                .id(user.getId())
                .intra(user.getIntra())
                .interests(interestList)
                .build();
    }

    public List<String> getIntra(List<Long> useridList) {
        List<String> intraList = new ArrayList<>();
        for (Long userid : useridList) {
            User user = userRepository.findById(userid).orElseThrow(()
                    -> new ResponseStatusException(NOT_FOUND, "찾을 수 없는 유저가 있습니다."));
            intraList.add(user.getIntra());
        }
        return intraList;
    }

    public ReportDto addReport(ReportDto reportRequest) {
        log.info("addReport Called");
        User user = getUser(reportRequest.reportedId());

        return reportRepository.save(new Report(user, reportRequest.reasons())).toReportDto();
    }
}
