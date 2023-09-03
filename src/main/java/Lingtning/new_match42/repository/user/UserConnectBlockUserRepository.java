package Lingtning.new_match42.repository.user;

import Lingtning.new_match42.entity.user.UserConnectBlockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserConnectBlockUserRepository extends JpaRepository<UserConnectBlockUser, Long> {
    List<UserConnectBlockUser> findByUser_Id(Long userId);

    List<UserConnectBlockUser> findByBlockUser_Id(Long userId);
}
