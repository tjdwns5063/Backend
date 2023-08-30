package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.MatchRoom;
import Lingtning.new_match42.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRoomRepository extends JpaRepository<MatchRoom, Long> {
}
