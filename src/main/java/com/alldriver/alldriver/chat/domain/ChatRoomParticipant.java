package com.alldriver.alldriver.chat.domain;

import com.alldriver.alldriver.user.domain.User;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="chat_room_participant")
public class ChatRoomParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="chat_room_id")
    @Setter
    private ChatRoom chatRoom;

}
