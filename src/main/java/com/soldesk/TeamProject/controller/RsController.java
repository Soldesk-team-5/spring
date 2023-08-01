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
        if(userId == null) {
            return "signin";
        }

        UserEntity signinUser = userService.getLoginUserByUserId(userId);

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
