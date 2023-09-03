package Lingtning.new_match42.repository.user;

import Lingtning.new_match42.entity.user.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByKeyword(String interest);
}
