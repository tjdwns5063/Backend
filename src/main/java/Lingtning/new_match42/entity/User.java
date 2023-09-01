package Lingtning.new_match42.entity;

import Lingtning.new_match42.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

// 유저 테이블
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@ToString(of = {"id", "intra", "email", "role", "blockCount"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String intra;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserConnectInterest> userConnectInterest = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserConnectBlockUser> userConnectBlockUser = new ArrayList<>();

    @Column(nullable = false)
    private Long blockCount;

    @Column()
    private String fcmToken;

    @Builder
    public User(Long id, String email, String intra, Role role, List<UserConnectInterest> userConnectInterest, List<UserConnectBlockUser> userConnectBlockUser, Long blockCount, String fcmToken) {
        this.id = id;
        this.email = email;
        this.intra = intra;
        this.role = role;
        this.userConnectInterest = userConnectInterest;
        this.userConnectBlockUser = userConnectBlockUser;
        this.blockCount = blockCount;
        this.fcmToken = fcmToken;
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