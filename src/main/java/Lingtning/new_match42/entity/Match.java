package Lingtning.new_match42.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

//@Entity
//@Table(name = "match")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class Match {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long matchNumber;
}
