package com.alldriver.alldriver.board.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import com.alldriver.alldriver.user.domain.User;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name="title", columnDefinition = "varchar", length = 50, nullable = false)
    private String title;

    @Column(name="pay_type", columnDefinition = "varchar", length = 5, nullable = false)
    private String payType;

    @Column(name="payment", columnDefinition = "integer", nullable = false)
    private Integer payment;

    @Column(name="end_at", columnDefinition = "timestamp", nullable = false)
    private LocalDateTime endAt;

    @Column(name="recruit_type", columnDefinition = "varchar", length = 10, nullable = false)
    private String recruitType;

    @Column(name="company_location", columnDefinition = "varchar", length = 100, nullable = false)
    private String companyLocation;

    @CreationTimestamp
    @Column(name="created_at", columnDefinition = "timestamp", nullable = false)
    private LocalDateTime createdAt;

    @ColumnDefault("false")
    @Column(name="deleted", nullable = false)
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<BoardImage> boardImages;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<CarBoard> carBoards;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<JobBoard> jobBoards;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<LocationBoard> locationBoards;

    public void setDeleted(Boolean deleted){
        this.deleted = deleted;
    }
}
