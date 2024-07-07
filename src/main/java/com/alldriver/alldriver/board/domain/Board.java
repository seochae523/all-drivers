package com.alldriver.alldriver.board.domain;


import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import com.alldriver.alldriver.user.domain.User;

import java.time.LocalDateTime;
import java.util.*;

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

    @Column(name="recruit_type", columnDefinition = "varchar", length = 10, nullable = false)
    private String recruitType;

    @Column(name="company_location", columnDefinition = "varchar", length = 100, nullable = false)
    private String companyLocation;

    @CreationTimestamp
    @Column(name="created_at", columnDefinition = "timestamp", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="start_at", columnDefinition = "date", nullable = false)
    private Date startAt;

    @Column(name="end_at", columnDefinition = "date", nullable = false)
    private Date endAt;

    @ColumnDefault("false")
    @Column(name="deleted", nullable = false)
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY ,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<BoardImage> boardImages = new HashSet<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<CarBoard> carBoards = new HashSet<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<JobBoard> jobBoards = new HashSet<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<LocationBoard> locationBoards = new HashSet<>();



    public void setDeleted(Boolean deleted){
        this.deleted = deleted;
    }

    public void addLocationBoard(LocationBoard locationBoard) {
        this.locationBoards.add(locationBoard);
        locationBoard.setBoard(this);
    }
    public void addCarBoard(CarBoard carBoard){
        this.carBoards.add(carBoard);
        carBoard.setBoard(this);
    }

    public void addJobBoard(JobBoard jobBoard){
        this.jobBoards.add(jobBoard);
        jobBoard.setBoard(this);
    }

    public void updateBoard(BoardUpdateRequestDto boardUpdateRequestDto){
        this.content = boardUpdateRequestDto.getContent();
        this.title = boardUpdateRequestDto.getTitle();
        this.payType = boardUpdateRequestDto.getPayType();
        this.payment = boardUpdateRequestDto.getPayment();
        this.recruitType = boardUpdateRequestDto.getRecruitType();
        this.companyLocation = boardUpdateRequestDto.getCompanyLocation();
        this.startAt = boardUpdateRequestDto.getStartAt();
        this.endAt = boardUpdateRequestDto.getEndAt();
    }
}
