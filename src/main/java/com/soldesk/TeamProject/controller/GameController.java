package com.soldesk.TeamProject.controller;

import com.soldesk.TeamProject.game.GameDTO;
import com.soldesk.TeamProject.game.GameRepository;
import com.soldesk.TeamProject.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"nickname", "userId"})
public class GameController {
    private final GameService gameService;
    private  final GameRepository gameRepository;


    // 장르들 리스트화 해서 표현 (중복제거)
    @GetMapping("/recommend")
    public String showGenres(Model model) {
        List<String> genres = gameService.getUniqueGenres();
        model.addAttribute("genres", genres);

        List<String> tags = gameService.getUniquetags();
        model.addAttribute("tags", tags);
        return "recommend";
    }

    @PostMapping("/recommend/search")
    @ResponseBody
    public List<String> searchGames(@RequestParam(value = "genres[]", required = false) List<String> genres,
                                    @RequestParam(value = "tags[]", required = false) List<String> tags) {

        if (genres != null && genres.size() > 0) {
            List<String> genreList = gameService.findByGenre(genres);
            if (tags != null && tags.size() > 0) {
                List<String> tagList = gameService.findByTag(tags);
                List<String> mergedList = new ArrayList<>(genreList);
                mergedList.removeAll(tagList);
                mergedList.addAll(tagList);
                return mergedList;
            } else {
                System.out.println(genreList);
                return genreList;
            }
        } else if (tags != null && tags.size() > 0) {
            List<String> tagList = gameService.findByTag(tags);
            return tagList;
        } else {
            List<String> all_game = gameService.allGame();
            System.out.println(all_game);
            return all_game;
        }
    }

    @GetMapping("/showgame")
    public String showGame(@RequestParam String name, Model model) {
        GameDTO game = gameService.convertToDTO(gameRepository.findGameByname(name));

        // 속성 설정
        model.addAttribute("name", game.getName());
        model.addAttribute("description", game.getDescription());
        model.addAttribute("genres", game.getGenre());
        model.addAttribute("tags", game.getTag());
        model.addAttribute("specMin", game.getMinimumSpecification());
        model.addAttribute("specRec", game.getRecommendedSpecification());
        model.addAttribute("steamLink", game.getSteamLink());
        model.addAttribute("eval", game.getEval());
        return "showgame";
    }


}