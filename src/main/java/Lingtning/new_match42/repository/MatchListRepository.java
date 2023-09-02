package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.MatchList;
import Lingtning.new_match42.entity.MatchRoom;
import Lingtning.new_match42.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchListRepository extends JpaRepository<MatchList, Long> {
    int countByMatchRoom(MatchRoom matchRoom);

    List<MatchList> findByUser_Id(Long userId);

    List<MatchList> findByMatchRoom_Id(Long matchRoomId);

    Optional<MatchList> findByMatchRoom_IdAndUser_Id(Long roomId, Long userId);
}
