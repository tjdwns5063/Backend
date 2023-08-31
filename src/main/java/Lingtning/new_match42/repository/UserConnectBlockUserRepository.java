package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.UserConnectBlockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserConnectBlockUserRepository extends JpaRepository<UserConnectBlockUser, Long> {
    List<UserConnectBlockUser> findByUser_Id(Long userId);
}
