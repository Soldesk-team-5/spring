package com.soldesk.TeamProject.controller;


import com.soldesk.TeamProject.game.GameDTO;
import com.soldesk.TeamProject.game.GameService;
import com.soldesk.TeamProject.recommendSystem.RsService;
import com.soldesk.TeamProject.user.UserEntity;
import com.soldesk.TeamProject.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"nickname", "userId"})
public class RsController {
    private final UserService userService;
    private final GameService gameService;
    private final RsService rsService;

    @GetMapping("/gameflix")
    public String recommend(@SessionAttribute(name = "userId", required = false) String userId, Model model){
        UserEntity signinUser = userService.getLoginUserByUserId(userId);

//        List<String> games = new ArrayList<>();
//        List<Long> gameIds = signinUser.getSelectedGame();
//        for (Long gameId : gameIds) {
//            GameDTO gameDTO = gameService.findByGameId(gameId);
//            games.add(gameDTO.getName());
//        };
//
//        // 장바구니 안에 있는 장르들과 태그들 다 들고오기 [장르, 태그]
//        List<String> basketGamesTags = gameService.getGameGenresTags(games);
//
//        // 장바구니 안에 있는 게임들의 장르들과 태그들의 중복정도 체크 <태그 : 횟수>
//        Map<String, Integer> dupCheck = gameService.listDupCheck(basketGamesTags);
//
//        // 장바구니 제외한 게임들의 <게임명, [장르, 태그]>
//        Map<String, List<String>> gamesWithoutBasket = gameService.getGameGenresTagsWithoutBasket(games);
//
//        basketGamesTags.stream().distinct().collect(Collectors.toList());
//
//        List<String> sortedList = rsService.calculateSimilarities(rsService.convertBasketTagToVectors(basketGamesTags), rsService.convertGameTagsToVectors(gamesWithoutBasket));
//
//        // 장바구니 내부에서 카운트가 높은 태그 순으로 5개 가져오기
//        List<String> fiveTags = gameService.getFiveTags(dupCheck);
//
//        List<List<String>> fiveTagsGameList = gameService.getFiveTagsGameList(fiveTags, sortedList);
        List<String> fiveTags = signinUser.getRecommendedGameTags();
        List<List<Long>> fiveTagsGameList = signinUser.getRecommendedGame();

        List<List<String>> finalFiveTagsGameList = new ArrayList<>();
        for(List<Long> GameList : fiveTagsGameList ) {
            List<String> temp = new ArrayList<>();
            for (Long game : GameList){
                GameDTO gameDTO = gameService.findByGameId(game);
                temp.add(gameDTO.getName());
            }
            finalFiveTagsGameList.add(temp);
        }

        model.addAttribute("firstTag", fiveTags.get(0));
        model.addAttribute("firstTagGames", finalFiveTagsGameList.get(0));

        if(finalFiveTagsGameList.get(1).size() > 0){
            model.addAttribute("secondTag", fiveTags.get(1));
            model.addAttribute("secondTagGames", finalFiveTagsGameList.get(1));
        };

        if(finalFiveTagsGameList.get(2).size() > 0) {
            model.addAttribute("thirdTag", fiveTags.get(2));
            model.addAttribute("thirdTagGames", finalFiveTagsGameList.get(2));
        };

        if(finalFiveTagsGameList.get(3).size() > 0) {
            model.addAttribute("fourthTag", fiveTags.get(3));
            model.addAttribute("fourthTagGames", finalFiveTagsGameList.get(3));
        };
        if(finalFiveTagsGameList.get(4).size() > 0) {
            model.addAttribute("fifthTag", fiveTags.get(4));
            model.addAttribute("fifthTagGames", finalFiveTagsGameList.get(4));
        };

        return "gameflix";
    }
}
