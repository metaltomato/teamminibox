package com.example.TeamPortfolio.domain.post;

import com.example.TeamPortfolio.common.dto.SearchDto;
import com.example.TeamPortfolio.common.paging.Pagination;
import com.example.TeamPortfolio.common.paging.PagingResponse;
import com.example.TeamPortfolio.domain.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final CommentService commentService;

    /**
     * 게시글 저장
     * @param params - 게시글 정보
     * @return Generated PK
     */
    @Transactional
    public Long savePost(final PostRequest params) {
        postMapper.save(params);
        return params.getId();
    }

    /**
     * 게시글 상세정보 조회
     * @param id - PK
     * @return 게시글 상세정보
     */
    public PostResponse findPostById(final Long id) {
        return postMapper.findById(id);
    }

    /**
     * 게시글 수정
     * @param params - 게시글 정보
     * @return PK
     */
    @Transactional
    public Long updatePost(final PostRequest params) {
        postMapper.update(params);
        return params.getId();
    }

    /**
     * 게시글 삭제
     * @param id - PK
     * @return PK
     */
    public Long deletePost(final Long id) {
        postMapper.deleteById(id);
        return id;
    }

    /**
     * 게시글 리스트 조회
     * @param params - search conditions
     * @return list & pagination information
     */
    public PagingResponse<PostResponse> findAllPost(final SearchDto params) {

        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
        int count = postMapper.count(params);
        if (count < 1) {
            return new PagingResponse<>(Collections.emptyList(), null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<PostResponse> list = postMapper.findAll(params);
        for (PostResponse post : list) {
            int commentCount = commentService.countCommentsByPostId(post.getId());
            post.setCommentCount(commentCount);
        }
        return new PagingResponse<>(list, pagination);
    }
    // 조회수 올리기
    public int cntPlus(Long id) throws Exception {
        return postMapper.cntPlus(id);
    }
    public List<PostResponse> findAllPostWithCommentCount(final SearchDto params) {
        List<PostResponse> posts = findAllPost(params).getList();
        for (PostResponse post : posts) {
            int commentCount = commentService.countCommentsByPostId(post.getId());
            post.setCommentCount(commentCount);
        }
        return posts;
    }
}
