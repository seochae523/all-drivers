package com.alldriver.alldriver.community.domain;

import com.alldriver.alldriver.board.domain.SubLocation;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "community_location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityLocation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id")
    @Setter
    private Community community;

    @ManyToOne
    @JoinColumn(name= "location_id")
    private SubLocation subLocation;
}
