package Lingtning.new_match42.repository.match;

import Lingtning.new_match42.entity.match.SubjectOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectOptionRepository extends JpaRepository<SubjectOption, Long> {
    Optional<SubjectOption> findTopByCapacityAndProject(Integer capacity, String project);

    List<SubjectOption> findByCapacityAndProject(Integer capacity, String project);

    Optional<SubjectOption> findByMatchRoom_Id(Long matchRoomId);
}
