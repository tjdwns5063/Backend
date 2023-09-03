package Lingtning.new_match42.repository.match;

import Lingtning.new_match42.entity.match.ChatOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatOptionRepository extends JpaRepository<ChatOption, Long> {
    Optional<ChatOption> findTopByCapacity(Integer capacity);

    List<ChatOption> findByCapacity(Integer capacity);
}
