//package com.example.TeamPortfolio.repsitory;
//
//import com.example.TeamPortfolio.domain.BoxOfficeApi;
//import com.example.TeamPortfolio.domain.Customer;
//import com.example.TeamPortfolio.domain.CustomerRole;
//import com.example.TeamPortfolio.repository.BoxOfficeApiRepository;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.stream.IntStream;
//
//@SpringBootTest
//@Log4j2
//public class BoxOfficeApiRepositoryTest {
//
//    @Autowired
//    private BoxOfficeApiRepository boxOfficeApiRepository;
//
//    @Test
//    public void insertMovies() {
//        IntStream.rangeClosed(1, 10).forEach(i -> {
//            BoxOfficeApi boxOfficeApi = BoxOfficeApi.builder()
//                    .movieNm("boxOfficeApi"+i)
//                    .rank(1)
//                    .build();
//
//            //DB에 데이터 저장
//            boxOfficeApiRepository.save(BoxOfficeApi);
//        });
//
//
//    }
//}