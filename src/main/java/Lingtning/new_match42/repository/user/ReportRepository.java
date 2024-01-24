package Lingtning.new_match42.repository.user;

import Lingtning.new_match42.entity.user.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
