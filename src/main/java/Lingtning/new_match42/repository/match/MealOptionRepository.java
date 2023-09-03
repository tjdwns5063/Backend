package Lingtning.new_match42.repository.match;

import Lingtning.new_match42.entity.match.MealOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealOptionRepository extends JpaRepository<MealOption, Long> {
    Optional<MealOption> findTopByCapacityAndMenu(Integer capacity, String menu);

    List<MealOption> findByCapacityAndMenu(Integer capacity, String menu);
}
