package com.alldriver.alldriver.chat.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="title", columnDefinition = "varchar", length = 50, nullable = false)
    private String title;

    @Column(name="created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;


    @ManyToOne
    @JoinColumn(name = "participant_id")
    private User participant;


    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;

    public void setDeleted(Boolean deleted){
        this.deleted = deleted;
    }
}
