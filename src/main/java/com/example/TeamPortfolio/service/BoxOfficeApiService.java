package com.example.TeamPortfolio.service;

import com.example.TeamPortfolio.domain.BoxOfficeApi;
import com.example.TeamPortfolio.repository.BoxOfficeApiRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Getter
@Setter
public class BoxOfficeApiService {
    @Autowired
    private BoxOfficeApiRepository boxOfficeApiRepository;

    public BoxOfficeApi save(BoxOfficeApi boxOfficeApi) {
        return boxOfficeApiRepository.save(boxOfficeApi);
    }

    public BoxOfficeApi findById(Long rank) {
        Optional<BoxOfficeApi> optionalBoxOfficeApi = boxOfficeApiRepository.findById(rank);
        return optionalBoxOfficeApi.orElse(null);

    }
}