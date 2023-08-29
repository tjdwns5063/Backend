package Lingtning.new_match42.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Table(name = "user_connect_interest")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class UserConnectInterest {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_connect_interest_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id")
    private Interest interest;

    @Builder
    public UserConnectInterest(Long id, User user, Interest interest) {
        this.id = id;
        this.user = user;
        this.interest = interest;
    }
}
