package com.example.TeamPortfolio.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movieApi")
public class BoxOfficeApi {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`rank`")
    private Long rank;
    @Column
    private Integer id;
    @Column(length = 255, nullable = true)
    private String movieNm;
    @Column(nullable = true)
    private String image;
    @Column(length=1000, nullable = true)
    private String overview;
    @Column(nullable = true)
    private Integer runtime;
    @Column(nullable = true)
    private String releaseDate;
}
