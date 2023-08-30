package Lingtning.new_match42.repository;

import Lingtning.new_match42.entity.MatchList;
import Lingtning.new_match42.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchListRepository extends JpaRepository<MatchList, Long> {
    public List<MatchList> findByUser_Id(Long userId);
}
