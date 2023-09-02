package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.MatchList;
import Lingtning.new_match42.entity.MatchRoom;
import Lingtning.new_match42.enums.MatchStatus;
import Lingtning.new_match42.enums.MatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRoomRepository extends JpaRepository<MatchRoom, Long> {
}
