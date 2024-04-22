package com.example.TeamPortfolio.domain.post;

import com.example.TeamPortfolio.common.dto.MessageDto;
import com.example.TeamPortfolio.common.dto.SearchDto;
import com.example.TeamPortfolio.common.file.FileUtils;
import com.example.TeamPortfolio.common.paging.PagingResponse;
import com.example.TeamPortfolio.domain.BoxOfficeApi;
import com.example.TeamPortfolio.domain.comment.CommentService;
import com.example.TeamPortfolio.domain.file.FileRequest;
import com.example.TeamPortfolio.domain.file.FileResponse;
import com.example.TeamPortfolio.domain.file.FileService;
import com.example.TeamPortfolio.repository.BoxOfficeApiRepository;
import com.example.TeamPortfolio.service.BoxOfficeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final BoxOfficeApiRepository boxOfficeApiRepository;
    private final BoxOfficeApiService boxOfficeApiService;
    private final CommentService commentService;

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }


    // 쿼리 스트링 파라미터를 Map에 담아 반환
    private Map<String, Object> queryParamsToMap(final SearchDto queryParams) {
        Map<String, Object> data = new HashMap<>();
        data.put("page", queryParams.getPage());
        data.put("recordSize", queryParams.getRecordSize());
        data.put("pageSize", queryParams.getPageSize());
        data.put("keyword", queryParams.getKeyword());
        data.put("searchType", queryParams.getSearchType());
        return data;
    }


    // 게시글 작성 페이지
    @GetMapping("/post/write.do")
    public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model) {
        if (id != null) {
            PostResponse post = postService.findPostById(id);
            model.addAttribute("post", post);
        }
        return "post/write";
    }


    // 게시글 리스트 페이지
    @GetMapping("/post/list.do")
    public String openPostList(@ModelAttribute("params") final SearchDto params, Model model) {
        PagingResponse<PostResponse> response = postService.findAllPost(params);
        model.addAttribute("response", response);
        return "post/list";
    }

//    @GetMapping("/post/detail/{rank}")
//    public String openDetailList(@ModelAttribute("params") final SearchDto params, Model model,
//                                 @PathVariable("rank") Long rank) {
//
//        PagingResponse<PostResponse> response = postService.findAllPost(params);
//        List<BoxOfficeApi> movies = boxOfficeApiRepository.findAll();
//        BoxOfficeApi movies2 = boxOfficeApiService.findById(rank);
//        model.addAttribute("movies", movies);
//        model.addAttribute("response", response);
//        model.addAttribute("movies2", movies2);
//        return "post/detail";
//    }


    // 게시글 상세 페이지
    @GetMapping("/post/view.do")
    public String openPostView(@RequestParam final Long id, Model model) throws Exception {
        PostResponse post = postService.findPostById(id);
        model.addAttribute("post", post);
        postService.cntPlus(id);
        return "post/view";
    }


    // 신규 게시글 생성
    @PostMapping("/post/save.do")
    public String savePost(final PostRequest params, Model model) {
        Long id = postService.savePost(params);
        List<FileRequest> files = fileUtils.uploadFiles(params.getFiles());
        fileService.saveFiles(id, files);
        MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/post/list.do", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }


    // 기존 게시글 수정
    @PostMapping("/post/update.do")
    public String updatePost(final PostRequest params, final SearchDto queryParams, Model model) {

        // 1. 게시글 정보 수정
        postService.updatePost(params);

        // 2. 파일 업로드 (to disk)
        List<FileRequest> uploadFiles = fileUtils.uploadFiles(params.getFiles());

        // 3. 파일 정보 저장 (to database)
        fileService.saveFiles(params.getId(), uploadFiles);

        // 4. 삭제할 파일 정보 조회 (from database)
        List<FileResponse> deleteFiles = fileService.findAllFileByIds(params.getRemoveFileIds());

        // 5. 파일 삭제 (from disk)
        fileUtils.deleteFiles(deleteFiles);

        // 6. 파일 삭제 (from database)
        fileService.deleteAllFileByIds(params.getRemoveFileIds());

        MessageDto message = new MessageDto("게시글 수정이 완료되었습니다.", "/post/list.do", RequestMethod.GET, queryParamsToMap(queryParams));
        return showMessageAndRedirect(message, model);
    }


    // 게시글 삭제
    @PostMapping("/post/delete.do")
    public String deletePost(@RequestParam final Long id, final SearchDto queryParams, Model model) {
        postService.deletePost(id);
        MessageDto message = new MessageDto("게시글 삭제가 완료되었습니다.", "/post/list.do", RequestMethod.GET, queryParamsToMap(queryParams));
        return showMessageAndRedirect(message, model);
    }
    @GetMapping("/post/detail/{rank}")
    public String getMovieInfoByRank(@PathVariable("rank") Long rank, Model model) {
        Optional<BoxOfficeApi> optionalBoxOfficeApi = boxOfficeApiRepository.findByRank(rank);
        if (optionalBoxOfficeApi.isPresent()) {
            model.addAttribute("movieData", optionalBoxOfficeApi.get());
            return "post/detail";
        } else {
            return "error"; // error.html 파일이 존재한다고 가정합니다.
        }
    }

}
