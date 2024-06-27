package com.alldriver.alldriver.board.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.user.domain.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Column(columnDefinition = "varchar(50)")
    @NotNull
    private String title;

    @Column(columnDefinition = "varchar(5)")
    @NotNull
    private String payType;

    @NotNull
    private Integer payment;

    @NotNull
    private LocalDateTime duration;

    @NotNull
    private String recruitType;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ColumnDefault("false")
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="BOARD_ID")
    private Set<Image> image;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<CarBoard> carBoards;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<JobBoard> jobBoards;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<PlaceBoard> placeBoards;



    public void setDeleted(Boolean deleted){
        this.deleted = deleted;
    }
}
