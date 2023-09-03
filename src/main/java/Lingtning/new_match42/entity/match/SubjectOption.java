package Lingtning.new_match42.entity.match;


import jakarta.persistence.*;
import lombok.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "subject_option")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class SubjectOption {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_room_id", nullable = false)
    MatchRoom matchRoom;

    @Column(nullable = false)
    Integer capacity;

    @Column(nullable = false)
    String project;

    @Builder
    public SubjectOption(MatchRoom matchRoom, Integer capacity, String project) {
        this.matchRoom = matchRoom;
        this.capacity = capacity;
        this.project = project;
    }
}
