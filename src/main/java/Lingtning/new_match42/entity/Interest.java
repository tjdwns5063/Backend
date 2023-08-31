package Lingtning.new_match42.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "interest")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString(exclude = "userConnectInterest")
public class Interest {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL)
    private List<UserConnectInterest> userConnectInterest = new ArrayList<>();

    @Builder
    public Interest(Long id, String keyword, List<UserConnectInterest> userConnectInterest) {
        this.id = id;
        this.keyword = keyword;
        this.userConnectInterest = userConnectInterest;
    }
}
