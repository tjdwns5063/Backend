package Lingtning.new_match42.entity.match;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "chat_option")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatOption {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_room_id", nullable = false)
    private MatchRoom matchRoom;

    @Column(nullable = false)
    private Integer capacity;

    @Builder
    public ChatOption(MatchRoom matchRoom, Integer capacity) {
        this.matchRoom = matchRoom;
        this.capacity = capacity;
    }
}
