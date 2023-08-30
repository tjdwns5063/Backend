package Lingtning.new_match42.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Table(name = "user_connect_interest")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class UserConnectInterest {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest", nullable = false)
    private Interest interest;

    @Builder
    public UserConnectInterest(Long id, User user, Interest interest) {
        this.id = id;
        this.user = user;
        this.interest = interest;
    }
}
