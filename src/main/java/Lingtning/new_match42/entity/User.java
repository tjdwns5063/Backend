package Lingtning.new_match42.entity;

import com.fasterxml.jackson.databind.util.ClassUtil;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.core.CollectionFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "intra", "email", "role"})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String intra;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column
    private String interest1;
    @Column
    private String interest2;
    @Column
    private String interest3;
    @Column
    private String interest4;
    @Column
    private String interest5;
    @Column(nullable = false)
    private Integer interestCount;

    @Column
    private String blockUser1;
    @Column
    private String blockUser2;
    @Column
    private String blockUser3;
    @Column
    private String blockUser4;
    @Column
    private String blockUser5;
    @Column(nullable = false)
    private Integer blockCount;

    @Builder
    public User(Long id, String email, String intra, Role role, String interest1, String interest2, String interest3, String interest4, String interest5, Integer interestCount, String blockUser1, String blockUser2, String blockUser3, String blockUser4, String blockUser5, Integer blockCount) {
        this.id = id;
        this.email = email;
        this.intra = intra;
        this.role = role;
        this.interest1 = interest1;
        this.interest2 = interest2;
        this.interest3 = interest3;
        this.interest4 = interest4;
        this.interest5 = interest5;
        this.interestCount = interestCount;
        this.blockUser1 = blockUser1;
        this.blockUser2 = blockUser2;
        this.blockUser3 = blockUser3;
        this.blockUser4 = blockUser4;
        this.blockUser5 = blockUser5;
        this.blockCount = blockCount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getKey()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.intra;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}