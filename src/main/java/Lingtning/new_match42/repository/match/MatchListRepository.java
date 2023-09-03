package Lingtning.new_match42.repository.match;

import Lingtning.new_match42.entity.match.MatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchListRepository extends JpaRepository<MatchList, Long> {
    List<MatchList> findByUser_Id(Long userId);
    List<MatchList> findByMatchRoom_Id(Long matchRoomId);
    void deleteByUser_IdAndMatchRoom_Id(Long userId, Long roomId);
}
