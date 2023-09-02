package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.UserConnectInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserConnectInterestRepository extends JpaRepository<UserConnectInterest, Long> {
    List<UserConnectInterest> findByUser_Id(Long userId);
}
