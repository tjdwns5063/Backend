package Lingtning.new_match42.entity;

import Lingtning.new_match42.enums.MatchType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "match_room")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "matchTable")
public class MatchRoom {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer size;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private MatchType matchType;

    @OneToMany(mappedBy = "matchRoom")
    private List<MatchTable> matchTable;
}
