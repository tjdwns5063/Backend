package Lingtning.new_match42.entity.match;


import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "meal_option")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MealOption {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_room_id", nullable = false)
    MatchRoom matchRoom;

    @Column(nullable = false)
    Integer capacity;

    @Column(nullable = false)
    String menu;

    @Builder
    public MealOption(MatchRoom matchRoom, Integer capacity, String menu) {
        this.matchRoom = matchRoom;
        this.capacity = capacity;
        this.menu = menu;
    }
}
