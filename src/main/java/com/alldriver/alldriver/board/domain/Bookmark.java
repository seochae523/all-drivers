package com.alldriver.alldriver.board.domain;

import com.alldriver.alldriver.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Entity(name = "bookmark")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name="created_at", columnDefinition = "timestamp", nullable = false)
    private LocalDateTime createdAt;
}
