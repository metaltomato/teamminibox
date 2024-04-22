package com.example.TeamPortfolio.repository;

import com.example.TeamPortfolio.domain.BoxOfficeApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoxOfficeApiRepository extends JpaRepository<BoxOfficeApi, Long> {
    Optional<BoxOfficeApi> findByRank(Long rank);
}
