package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity(name = "job")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", columnDefinition = "varchar", length = 10, nullable = false)
    private String category;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private Set<JobBoard> jobBoards;

}
