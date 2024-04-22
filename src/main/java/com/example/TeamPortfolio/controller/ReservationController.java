package com.example.TeamPortfolio.controller;

import com.example.TeamPortfolio.domain.Customer;
import com.example.TeamPortfolio.domain.Reservation;
import com.example.TeamPortfolio.repository.ReservationRepository;
import com.example.TeamPortfolio.service.BoxOfficeApiService;
import com.example.TeamPortfolio.service.CustomerService;
import com.example.TeamPortfolio.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/reservation")
@Log4j2
public class ReservationController {
    private final ReservationService reservationService;
    private final BoxOfficeApiService boxOfficeApiService;

    private final CustomerService customerService;

    @Autowired
    private ReservationRepository reservationRepository;

    // 의존성 주입을 받습니다.
    public ReservationController(ReservationService reservationService, BoxOfficeApiService boxOfficeApiService, CustomerService customerService) {
        this.reservationService = reservationService;
        this.boxOfficeApiService = boxOfficeApiService;
        this.customerService = customerService;
    }


    @GetMapping("/start")
    public String startGET(
            @RequestParam("rank") Long rank,
            @RequestParam("movieNm") String movieNm,
            @RequestParam("image") String image,
            @RequestParam("overview") String overview,
            @RequestParam("runtime") String runtime,
            @RequestParam("releaseDate") String releaseDate,
            @RequestParam("reservation_num") String reservation_num,
            Model model, Customer customer, HttpServletRequest request) {

        List<String> reservedSeats = reservationRepository.findReservedSeats(movieNm);

        // 로그인 된 사용자 이름 가져오기
        Object principal = request.getUserPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : null;
        model.addAttribute("username", username);

        // 모델 객체를 사용해 영화 관련 정보를 저장합니다.
        model.addAttribute("rank", rank);
        model.addAttribute("movieNm", movieNm);
        model.addAttribute("image", image);
        model.addAttribute("overview", overview);
        model.addAttribute("runtime", runtime);
        model.addAttribute("releaseDate", releaseDate);
        model.addAttribute("reservedSeats", reservedSeats);
        return "reservation/start";
    }

    @GetMapping(value = "/isAuthenticated")
    @ResponseBody
    public boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

    @PostMapping("/start")
    public String startPOST(HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttrs) {
//        String cid = httpServletRequest.getParameter("cid");
        Long rank = Long.valueOf(httpServletRequest.getParameter("rank"));
        String reservation_day = (httpServletRequest.getParameter("reservation_day") != null) ? httpServletRequest.getParameter("reservation_day") : "";
        String reservation_num = (httpServletRequest.getParameter("reservation_num") != null) ? httpServletRequest.getParameter("reservation_num") : "";
        String movieNm = httpServletRequest.getParameter("movieNm");
        String image = httpServletRequest.getParameter("image");
        String overview = httpServletRequest.getParameter("overview");
        String runtime = httpServletRequest.getParameter("runtime");
        String releaseDate = httpServletRequest.getParameter("releaseDate");
        List<String> reservedSeats = reservationRepository.findReservedSeats(reservation_num);

        model.addAttribute("rank", rank);
        model.addAttribute("movieNm", movieNm);
        model.addAttribute("image", image);
        model.addAttribute("overview", overview);
        model.addAttribute("runtime", runtime);
        model.addAttribute("releaseDate", releaseDate);
        model.addAttribute("reservedSeats", reservedSeats);

        // 리다이렉트 URL에 쿼리 파라미터로 값을 전달하기 위해 RedirectAttributes에 추가합니다.
//        redirectAttrs.addAttribute("cid", cid);
        redirectAttrs.addAttribute("rank", rank);
        redirectAttrs.addAttribute("reservation_day", reservation_day);
        redirectAttrs.addAttribute("reservation_num", reservation_num);
        redirectAttrs.addAttribute("movieNm", movieNm);
        redirectAttrs.addAttribute("image", image);
        redirectAttrs.addAttribute("overview", overview);
        redirectAttrs.addAttribute("runtime", runtime);
        redirectAttrs.addAttribute("releaseDate", releaseDate);

        return "redirect:/reservation/start";
    }

    @GetMapping("/check")
    public String checkGET(
//            @RequestParam("cid") String cid,
            @RequestParam("rank") Long rank,
            @RequestParam("reservation_day") String reservation_day,
            @RequestParam("reservation_num") String reservation_num,
            @RequestParam("movieNm") String movieNm,
            @RequestParam("image") String image,
            Model model) {

//        model.addAttribute("cid", cid);
        model.addAttribute("rank", rank);
        model.addAttribute("reservation_day", reservation_day);
        model.addAttribute("reservation_num", reservation_num);
        model.addAttribute("movieNm", movieNm);
        model.addAttribute("image", image);

        return "reservation/check";
    }

    @PostMapping("/check")
    public String checkPOST(
//            @RequestParam("cid") String cid,
            @RequestParam("rank") Long rank,
            @RequestParam("movieNm") String movieNm,
            @RequestParam("image") String image,
            @RequestParam("reservation_day") String reservation_day,
            @RequestParam("reservation_num") String reservation_num, Model model) {
//        model.addAttribute("cid", cid);
        model.addAttribute("rank", rank);
        model.addAttribute("movieNm", movieNm);
        model.addAttribute("image", image);
        model.addAttribute("reservation_day", reservation_day);
        model.addAttribute("reservation_num", reservation_num);

        return "reservation/check";
    }

    @GetMapping("/done")
    public String doneGET(
            @RequestParam("username") String username,
            @RequestParam("rank") Long rank,
            @RequestParam("movieNm") String movieNm,
            @RequestParam("image") String image,
            @RequestParam("reservation_day") String reservation_day,
            @RequestParam("reservation_num") String reservation_num) {

        saveEntities(username, rank, movieNm, image, reservation_day, reservation_num);

        // 저장이 완료되면 check 페이지로 이동합니다.
        return "reservation/done";
    }

    @PostMapping("/done")
    public String donePOST(
            @RequestParam("username") String username,
            @RequestParam("rank") Long rank,
            @RequestParam("movieNm") String movieNm,
            @RequestParam("image") String image,
            @RequestParam("reservation_day") String reservation_day,
            @RequestParam("reservation_num") String reservation_num) {

        saveEntities(username, rank, movieNm, image, reservation_day, reservation_num);

        return "reservation/done";
    }

    void saveEntities(String username, Long rank, String movieNm, String image, String reservation_day, String
            reservation_num) {

        Reservation reservation = new Reservation();
        reservation.setUsername(username);
        reservation.setReservation_day(reservation_day);
        reservation.setReservation_num(reservation_num);

        reservation.setRank(rank);
        reservation.setMovieNm(movieNm);
        reservation.setImage(image);

        Reservation savedReservation = reservationService.save(reservation);
    }

}