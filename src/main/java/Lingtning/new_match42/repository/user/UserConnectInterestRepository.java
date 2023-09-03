package Lingtning.new_match42.repository.user;

import Lingtning.new_match42.entity.user.UserConnectInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConnectInterestRepository extends JpaRepository<UserConnectInterest, Long> {
    List<UserConnectInterest> findByUser_Id(Long userId);
}
