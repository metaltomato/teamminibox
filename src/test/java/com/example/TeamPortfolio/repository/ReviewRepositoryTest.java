package com.example.TeamPortfolio.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void testInsert(){
        IntStream.range(1,70).forEach(i -> {
            Review review = Review.builder()
                    .rtitle("다시만든제목"+i)
                    .rtext("다시만든내용"+i)
                    .rwriter("다시만든작성자"+i)
                    .rpass("1234")
                    .build();
            Review result = reviewRepository.save(review);
            log.info("번호: " + result);
        });
    }
}