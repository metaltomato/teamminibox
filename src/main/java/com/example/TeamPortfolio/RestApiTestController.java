package com.example.TeamPortfolio;

import com.example.TeamPortfolio.common.dto.SearchDto;
import com.example.TeamPortfolio.common.paging.PagingResponse;
import com.example.TeamPortfolio.domain.post.PostResponse;
import com.example.TeamPortfolio.domain.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiTestController {

    private final PostService postService;

    @GetMapping("/posts")
    public PagingResponse<PostResponse> findAllPost() {
        return postService.findAllPost(new SearchDto());
    }

}
