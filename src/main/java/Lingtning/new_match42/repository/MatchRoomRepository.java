package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.MatchRoom;
import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRoomRepository extends JpaRepository<MatchRoom, Long> {
    MatchRoom findByMatchStatusAndMatchType(MatchStatus matchStatus, MatchType matchType);
}
