package Lingtning.new_match42.repository.match;

import Lingtning.new_match42.entity.match.SubjectOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectOptionRepository extends JpaRepository<SubjectOption, Long> {
    Optional<SubjectOption> findTopByCapacityAndProject(Integer capacity, String project);
}
