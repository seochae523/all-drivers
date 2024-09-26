package com.alldriver.alldriver.user.domain;


import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.user.dto.request.SocialLoginSignUpRequestDto;
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
import java.util.*;

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

    @Column(name="password", columnDefinition = "varchar")
    private String password;

    @Column(name="name", columnDefinition = "varchar", length = 20, nullable = false)
    private String name;

    @Column(name="phone_number", columnDefinition = "varchar", length = 11, nullable = false)
    private String phoneNumber;

    @Column(name="deleted", nullable = false)
    @ColumnDefault(value = "false")
    private Boolean deleted;

    @Column(name = "refresh_token", columnDefinition = "text")
    private String refreshToken;

    @Column(name="created_at", columnDefinition = "timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="role", columnDefinition = "varchar", length = 100, nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<CompanyInformation> companyInformation = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<UserCarInformation> userCarInformation = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private FcmToken fcmToken;



    public void setRole(Role role){
        if(this.role == null) {
            this.role = role.getValue();
        }
        else{
            this.role += ",";
            this.role += role.getValue();
        }
    }


    public void removeJobSeeker(){
        if(role.contains("ROLE_JOB_SEEKER,")){
            role = role.replace("ROLE_JOB_SEEKER,", "");
        }
        else if(role.contains(",ROLE_JOB_SEEKER")){
            role = role.replace(",ROLE_JOB_SEEKER", "");
        }
    }

    public void grantUser() {
        role = role.replace("TEMP_", "");
    }
    public void addUserCarInformation(UserCarInformation userCarInformation){
        this.userCarInformation.add(userCarInformation);
        userCarInformation.setUser(this);
    }
    public void addCompanyInformation(CompanyInformation companyInformation){
        this.companyInformation.add(companyInformation);
        companyInformation.setUser(this);
    }

    public void addFcmToken(FcmToken fcmToken){
        this.fcmToken = fcmToken;
        fcmToken.setUser(this);
    }
    public void updateUserInfo(UserUpdateRequestDto updateRequestDto){
        this.userId = updateRequestDto.getUserId();

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
