package com.soldesk.TeamProject.controller;

import com.soldesk.TeamProject.game.GameDTO;
import com.soldesk.TeamProject.game.GameRepository;
import com.soldesk.TeamProject.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        List<String> finalTags = new ArrayList<>();
        for (String tag : tags) {
            if(!genres.contains(tag)){
                finalTags.add(tag);
            }
        }
        model.addAttribute("tags", finalTags);
        return "recommend";
    }

    // 게임들 내보내는 코드
    @PostMapping("/recommend/search")
    @ResponseBody
    public List<String> searchGames(@RequestParam(value = "genres[]", required = false) List<String> genres,
                                    @RequestParam(value = "tags[]", required = false) List<String> tags) {

//        if (genres != null && genres.size() > 0) {
//            List<String> genreList = gameService.findByGenre(genres);
//            if (tags != null && tags.size() > 0) {
//                List<String> tagList = gameService.findByTag(tags);
//                List<String> mergedList = new ArrayList<>(genreList);
//                mergedList.removeAll(tagList);
//                mergedList.addAll(tagList);
//                return mergedList;
//            } else {
//                return genreList;
//            }
//        } else if (tags != null && tags.size() > 0) {
//            List<String> tagList = gameService.findByTag(tags);
//            return tagList;
//        } else {
//            List<String> all_game = gameService.allGame();
//            return all_game;
//        }

        List<GameDTO> allGames = gameService.getAllGames(); // 모든 게임 DTO를 가져옴
        List<String> matchingGameNames = new ArrayList<>();

        for (GameDTO game : allGames) {
            if (gameService.isGenresMatched(game, genres) && gameService.isTagsMatched(game, tags)) {
                matchingGameNames.add(game.getName()); // 모든 장르와 태그들이 충족하는 경우에만 게임명을 리스트에 추가
            }
        }

        return matchingGameNames;
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