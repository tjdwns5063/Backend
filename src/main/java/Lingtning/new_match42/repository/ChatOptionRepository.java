package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.ChatOption;
import Lingtning.new_match42.entity.MatchRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatOptionRepository extends JpaRepository<ChatOption, Long> {
    Optional<ChatOption> findTopByCapacity(Integer capacity);
}
