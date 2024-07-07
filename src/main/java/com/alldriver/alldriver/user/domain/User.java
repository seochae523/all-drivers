package com.alldriver.alldriver.user.domain;


import com.alldriver.alldriver.common.emun.Role;
import com.alldriver.alldriver.user.dto.request.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "varchar", length = 30, nullable = false)
    private String userId;

    @Column(name="password", columnDefinition = "varchar", nullable = false)
    private String password;

    @Column(name="name", columnDefinition = "varchar", length = 20, nullable = false)
    private String name;

    @Column(name="phone_number", columnDefinition = "varchar", length = 11, nullable = false)
    private String phoneNumber;

    @Column(name="deleted", nullable = false)
    @ColumnDefault(value = "false")
    private Boolean deleted;

    @Column(name="nickname", columnDefinition = "varchar", length = 20, nullable = false)
    private String nickname;
    @Column(name = "refresh_token", columnDefinition = "text")
    private String refreshToken;

    @Column(name="created_at", columnDefinition = "timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="role", columnDefinition = "varchar", length = 30, nullable = false)
    private String role;

    @OneToMany(mappedBy = "user")
    private Set<License> license;

    @OneToMany(mappedBy = "user")
    private Set<UserCar> userCar;


    public void setRole(Role role){
        if(this.role == null) {
            this.role = role.getValue();
        }
        else{
            this.role += ",";
            this.role += role.getValue();
        }
    }

    public void updateUserInfo(UserUpdateRequestDto updateRequestDto){
        this.userId = updateRequestDto.getUserId();
        this.nickname = updateRequestDto.getNickname();
    }

    public void setDeleted(Boolean deleted){
        this.deleted = deleted;
    }
    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public User hashPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
        return this;
    }
    public void updatePassword(String password){
        this.password = password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : this.role.split(",")) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return userId;
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
        return true;
    }
}
