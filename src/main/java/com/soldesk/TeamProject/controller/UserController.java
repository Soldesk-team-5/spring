package com.soldesk.TeamProject.controller;

import com.soldesk.TeamProject.game.GameDTO;
import com.soldesk.TeamProject.game.GameService;
import com.soldesk.TeamProject.recommendSystem.EventClass;
import com.soldesk.TeamProject.recommendSystem.RsService;
import com.soldesk.TeamProject.user.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"nickname", "userId"})
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final GameService gameService;
    private final RsService rsService;

    @Autowired
    ApplicationEventPublisher publisher;

    // 회원가입 페이지로
    @GetMapping("/signup")
    public String signupPage(Model model){
        return "signup";
    }

    //회원가입 신청
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupRequest signupRequest, BindingResult bindingResult, Model model) {

        // 아이디 중복체크
        if(userService.checkUserIdDuplicate(signupRequest.getUserId())){
            bindingResult.addError(new FieldError("signUpRequest", "userId", "해당 아이디는 이미 존재합니다"));
            model.addAttribute("valid_userId","해당 아이디는 이미 존재합니다");
        }
        // 닉네임 중복체크
        if(userService.checkNicknameDuplicate(signupRequest.getNickname())) {
            bindingResult.addError(new FieldError("signUpRequest", "nickname", "해당 닉네임은 이미 존재합니다"));
            model.addAttribute("valid_nickname","해당 닉네임은 이미 존재합니다");
        }
        // password와 passwordCheck가 같은지 체크
        if(!signupRequest.getPassword().equals(signupRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("signUpRequest", "passwordCheck", "바밀번호가 일치하지 않습니다"));
            model.addAttribute("valid_passwordCheck","바밀번호가 일치하지 않습니다");
        }

        if(bindingResult.hasErrors()) {
            /* 회원가입 페이지로 리턴 */
            return "signup";
        }

        userService.signup(signupRequest);

        // 정상적으로 회원가입되면 index 페이지로 redirect
        return "redirect:/";
    }

    // 로그인 페이지로
    @GetMapping("/signin")
    public String signinPage(Model model){
        return "signin";
    }
    
    // 로그인 기능
    @PostMapping("/signin")
    public String signin(@ModelAttribute SigninRequest signinRequest, HttpServletRequest httpServletRequest, Model model) {

        UserEntity user = userService.signin(signinRequest);

        // 로그인 아이디가 틀린 경우 에러처리
        if(user == null) {
            model.addAttribute("noId", "존재하지않는 아이디 입니다");
            return "signin";
        }

        if(!user.getPassword().equals(signinRequest.getPassword())) {
            model.addAttribute("noPassword", "틀린 비밀번호 입니다");
            return "signin";
        }

        // 로그인 성공 => 세션 생성
        // 세션을 생성하기 전에 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);  // Session이 없으면 생성

        // rsservice를 위한 session 전달
        rsService.signinUser = user.getUserId();

        // 세션에 nickname, userId를 넣어줌
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("nickname", user.getNickname());
        return "redirect:/";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, SessionStatus ss) {
        HttpSession session = request.getSession();
        session.removeAttribute("userId");
        session.removeAttribute("nickname");
        ss.setComplete();
        return "redirect:/";
    }

    // 유저 정보 페이지
    @GetMapping("/userInfo")
    public String userInfo(@SessionAttribute(name = "userId", required = false) String userId, Model model) {

        UserEntity signinUser = userService.getLoginUserByUserId(userId);
        model.addAttribute("userId", signinUser.getUserId());
        model.addAttribute("nickname", signinUser.getNickname());
        return "userInfo";
    }

    // 유저 정보 수정
    @PostMapping("/updateInfo")
    public String updateInfo(@SessionAttribute(name = "userId", required = false) String userId, @RequestParam(name = "changedNickname") String changedNickname,
                            @RequestParam(name ="changedPassword") String changedPassword, Model model) {

        // 세션에서 "userId" 속성이 존재하지 않을 경우, 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/signin";
        }

        // 닉네임 중복체크
        if(userService.checkNicknameDuplicate(changedNickname)) {
            model.addAttribute("valid_nickname","해당 닉네임은 이미 존재합니다");
            return "userInfo";
        }

        UserEntity signinUser = userService.getLoginUserByUserId(userId);
        signinUser.setNickname(changedNickname);
        signinUser.setPassword(changedPassword);
        userRepository.save(signinUser);

        // 정상적으로 수정되면 유저정보 페이지로 redirect
        return "userInfo";
    }
    
    // 유저 삭제
    @PostMapping("/deleteInfo")
    public String deleteuser(@SessionAttribute(name = "userId", required = false) String userId, @RequestParam(name = "checkPassword") String checkPassword,
                             Model model, SessionStatus ss) {

        // 세션에서 "userId" 속성이 존재하지 않을 경우, 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/signin";
        }
        
        UserEntity signinUser = userService.getLoginUserByUserId(userId);
        if (signinUser.getPassword().equals(checkPassword)){
            userRepository.delete(signinUser);    
        } else {
            model.addAttribute("notEqual", "비밀번호가 틀립니다. 올바른 비밀번호를 입력하세요");
            return "userInfo";
        }
        
        // 세션 삭제
        ss.setComplete();

        // 정상적으로 삭제되면 메인으로
        return "index";
    }

    // 장바구니에 게임 담기
    @GetMapping("/toBasket")
    @ResponseBody
    public String toBasket(@RequestParam("name") String gameName, @SessionAttribute(name = "userId", required = false) String userId) {


        UserEntity signinUser = userService.getLoginUserByUserId(userId);
        List<Long> gameList = signinUser.getSelectedGame();
        GameDTO gameDTO = gameService.findByName(gameName);
        Long gameId = gameDTO.getId();


        if(gameList == null){
            gameList = new ArrayList<>();
            gameList.add(gameId);
            // 이벤트를 전달
            signinUser.setSelectedGame(gameList);
            userRepository.save(signinUser);
            publisher.publishEvent(new EventClass(gameList, userId));
            return "장바구니에 게임을 담았습니다 (" +  gameList.size() + " / 5)";
        }

        if(gameList.contains(gameId)){

            return "이미 장바구니에 있는 게임 입니다";
        }

        if (gameList.size() < 5){
            gameList.add(gameId);
            // 이벤트를 전달
            signinUser.setSelectedGame(gameList);
            userRepository.save(signinUser);
            publisher.publishEvent(new EventClass(gameList, userId));
            return "장바구니에 게임을 담았습니다 (" +  gameList.size() + " / 5)";
        } else {
            return "장바구니가 가득차있습니다!!";
        }
    }

    // Basket으로 가기
    @GetMapping("/checkBasket")
    public String checkBasket(@SessionAttribute(name = "userId", required = false) String userId, Model model){
        UserEntity signinUser = userService.getLoginUserByUserId(userId);
        List<Long> gameIds = signinUser.getSelectedGame();
        List<Map<String, String>> gameList = new ArrayList<>();

        for (Long gameId : gameIds) {
            Map<String, String> temp = new HashMap<>();
            GameDTO gameDTO = gameService.findByGameId(gameId);
            if(gameDTO == null){
                break;
            } else {
                    temp.put("tempName", gameDTO.getName().replace(" ", "@"));
                    temp.put("name", gameDTO.getName());
                    gameList.add(temp);
                };
            };

        model.addAttribute("gamelist", gameList);
        return "basket";
    }

    // 장바구니 삭제
    @GetMapping("/basketDelete")
    @ResponseBody
    public String deleteBasket(@SessionAttribute(name = "userId", required = false) String userId, @RequestParam(value = "items[]", required = false) List<String> games){
        UserEntity signinUser = userService.getLoginUserByUserId(userId);
        List<Long> userBasketGames = signinUser.getSelectedGame();

        List<Long> temp = new ArrayList<>();

        // 게임즈의 게임명으로 게임 아이디 찾고 리스트에 담기
        for (String game : games){
            game = game.replace("@", " ");
            GameDTO gameDTO = gameService.findByName(game);
            temp.add(gameDTO.getId());
        }

        List<Long> newBasketGames = new ArrayList<>(userBasketGames);
        newBasketGames.removeAll(temp);

        signinUser.setSelectedGame(newBasketGames);
        userRepository.save(signinUser);
        
        // 이벤트를 전달
        publisher.publishEvent(new EventClass(newBasketGames, userId));
        return "success";
    }
}
