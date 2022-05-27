package com.letscode.moviesBattle.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
public class Movie {

    private Long id;
    private String title;
    private int voteCount;
    private BigDecimal voteAverage;
    private String imagePath;

}
