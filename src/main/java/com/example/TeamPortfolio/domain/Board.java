package com.example.TeamPortfolio.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Data
public class Board {

    private int no;
    private String title;
    private String writer;
    private LocalDateTime updateTime;

    Board() {
    }

    public Board(int no, String title, String writer) {
        this.no = no;
        this.title = title;
        this.writer = writer;
        this.updateTime = LocalDateTime.now();
    }
}
