package Lingtning.new_match42.entity.user;

import static jakarta.persistence.GenerationType.IDENTITY;

import Lingtning.new_match42.dto.ReportDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reasons", nullable = false,  columnDefinition = "TEXT")
    private String reasons;

    public Report(User user, List<String> reasons) {
        this.user = user;
        this.reasons = reasons.toString();
    }

    public ReportDto toReportDto() {
        return new ReportDto(user.getId(), transformReasons());
    }

    private List<String> transformReasons() {
        return Arrays.stream(reasons.substring(1, reasons.length() - 1).split(",")).map(String::trim).toList();
    }
}
