package com.example.TeamPortfolio.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.example.TeamPortfolio.domain.BoxOfficeApi;
import com.example.TeamPortfolio.repository.BoxOfficeApiRepository;
import jakarta.annotation.PostConstruct;
import org.apache.http.client.methods.HttpHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class BoxOfficeApiController {
    Integer runtime= 0;
    @Autowired
    private BoxOfficeApiRepository boxOfficeApiRepository;

    private Map<String, Object> fetchMovieDetails(String tmdbApiKey, String query) {
        Map<String, Object> details = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        // TMDB API에서 제공하는 검색 기능을 사용
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("api_key", tmdbApiKey)
                .queryParam("query", query)
                .build();

        // API 호출
        ResponseEntity<Map> tmdbResponse = restTemplate.exchange(uri.toString(), HttpMethod.GET, null, Map.class);
        LinkedHashMap map = (LinkedHashMap) tmdbResponse.getBody();
        ArrayList<Map> searchList = (ArrayList<Map>) map.get("results");

        System.out.println("검색 결과 갯수: " + searchList.size());

        if (!searchList.isEmpty()) {
            Map<String, Object> movie = searchList.get(0);
            int movieId = (Integer) movie.get("id");
            details.put("id", movieId);

            if (movie.containsKey("poster_path") && movie.get("poster_path") != null) {
                String image = "https://image.tmdb.org/t/p/w300" + movie.get("poster_path").toString();
                details.put("image", image);
            }

            // 영화 상세 정보 가져오기
            UriComponents detailsUri = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/movie/" + movieId)
                    .queryParam("api_key", tmdbApiKey)
                    .queryParam("language", "ko-KR")
                    .build();

            ResponseEntity<Map> detailsResponse = restTemplate.exchange(detailsUri.toString(), HttpMethod.GET, null, Map.class);
            LinkedHashMap detailsMap = (LinkedHashMap) detailsResponse.getBody();

            // 개요, 개봉일, 러닝 타임, 영화대표코드 가져오기
            details.put("overview", detailsMap.get("overview"));
            details.put("releaseDate", detailsMap.get("release_date"));
            details.put("runtime", detailsMap.get("runtime"));
            details.put("id", detailsMap.get("id"));

      }

        return details;
    }
    @PostConstruct
    @GetMapping("/movie")
    public String getAPI(){

        HashMap<String, Object> result = new HashMap<String, Object>();
        String jsonInString = "";

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            LocalDate today = LocalDate.now().minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formatedToday = today.format(formatter);
            String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("key", "173bd47f65a2784f079f097460c4166a")
                    .queryParam("targetDt", formatedToday)
                    .build();


            //이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            LinkedHashMap lm = (LinkedHashMap) resultMap.getBody().get("boxOfficeResult");
            ArrayList<Map> dboxoffList = (ArrayList<Map>) lm.get("dailyBoxOfficeList");
            LinkedHashMap mnList = new LinkedHashMap<>();

            for (Map obj : dboxoffList) {
                Map<String, Object> movieDetails = fetchMovieDetails("ab70535f4cdbc4b05970314e8811121f", obj.get("movieNm").toString());

                LinkedHashMap movieInfo = new LinkedHashMap<>();
                movieInfo.put("movieNm", obj.get("movieNm"));
                movieInfo.put("image", movieDetails.get("image"));
                movieInfo.put("overview", movieDetails.get("overview"));
                movieInfo.put("releaseDate", movieDetails.get("releaseDate"));
                movieInfo.put("runtime", movieDetails.get("runtime"));
                movieInfo.put("id", movieDetails.get("id"));

                mnList.put(obj.get("rank"), movieInfo);

                // 기존 LinkedHashMap 객체를 JSON 문자열로 변환한 후 다시 JSON 객체로 변환하는 과정을 제거
                ObjectMapper mapper = new ObjectMapper();
                jsonInString = mapper.writeValueAsString(mnList);
            }
try{
//db에 저장하는 코드
    for (Map obj : dboxoffList) {
        LinkedHashMap movieInfo = (LinkedHashMap) mnList.get(obj.get("rank"));
        String movieNm = (String) movieInfo.get("movieNm");
        String image = (String) movieInfo.get("image");
        //Integer: int는 null값 허용이 되지 않기 때문에 사용한다.
        Integer runtime = (Integer) movieInfo.get("runtime");
        String releaseDate = (String) movieInfo.get("releaseDate");
        String overview = (String) movieInfo.get("overview");
        Integer id = (Integer) movieInfo.get("id");

        Long rank = Long.parseLong(obj.get("rank").toString());
        BoxOfficeApi boxOfficeApi = boxOfficeApiRepository.findByRank(rank).orElse(new BoxOfficeApi());

        boxOfficeApi.setRank(Long.parseLong(obj.get("rank").toString()));

        if(movieNm != null)
            boxOfficeApi.setMovieNm(movieNm);

        if (id != null)
            boxOfficeApi.setId(id);

        if (image != null)
            boxOfficeApi.setImage(image);

        if(runtime != null)
            boxOfficeApi.setRuntime(runtime);

        if(overview != null)
            boxOfficeApi.setOverview(overview);

        if(releaseDate != null)
            boxOfficeApi.setReleaseDate(releaseDate);

        BoxOfficeApi savedBoxOfficeApi = boxOfficeApiRepository.save(boxOfficeApi);
        System.out.println("저장 후 객체: " + savedBoxOfficeApi);
    }
}catch (Exception e){
    e.printStackTrace();
}
            //db에 저장한 후에는 다시 꺼내주는 코드를 적어야 출력이 됌
            List<BoxOfficeApi> boxOfficeList = boxOfficeApiRepository.findAll();
            for (BoxOfficeApi boxOfficeApi : boxOfficeList) {
                System.out.println(boxOfficeApi.toString());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            System.out.println(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body", "excpetion오류");
            System.out.println(e.toString());
        }
        return jsonInString;
    }

    @RequestMapping(value="/reservation/start")
    public String showMovieInfo(Model model) {
        List<BoxOfficeApi> boxOfficeList =boxOfficeApiRepository.findAll();
        model.addAttribute("boxOfficeList", boxOfficeList);
        return "reservation/start";
    }
    @RequestMapping(value="/post/detail.do")
    public String reviewMovieInfo(Model model) {
        List<BoxOfficeApi> boxOfficeList =boxOfficeApiRepository.findAll();
        model.addAttribute("boxOfficeList", boxOfficeList);
        return "post/detail";
    }

}

