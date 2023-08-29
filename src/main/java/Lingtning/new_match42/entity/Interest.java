package Lingtning.new_match42.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "interest")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class Interest {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    @OneToMany(mappedBy = "interest")
    private List<UserConnectInterest> userConnectInterest;

    @Builder
    public Interest(Long id, String keyword, List<UserConnectInterest> userConnectInterest) {
        this.id = id;
        this.keyword = keyword;
        this.userConnectInterest = userConnectInterest;
    }
}
