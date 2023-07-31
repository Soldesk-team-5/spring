package com.soldesk.TeamProject.recommendSystem;

import com.soldesk.TeamProject.game.GameDTO;
import com.soldesk.TeamProject.game.GameService;
import com.soldesk.TeamProject.user.UserEntity;
import com.soldesk.TeamProject.user.UserRepository;
import com.soldesk.TeamProject.user.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventHandler {

    private UserService userService;
    private GameService gameService;

    private RsService rsService;

    private UserRepository userRepository;

    @Autowired
    public EventHandler(UserService userService, GameService gameService, RsService rsService, UserRepository userRepository) {
        this.userService = userService;
        this.gameService = gameService;
        this.rsService = rsService;
        this.userRepository = userRepository;
    }


    @Async
    @EventListener
    public void calculateRecomendedGame(EventClass event){

        List<Long> changedBasket = event.getSelectedGames();

        String signinUser = event.getSigninUser();

        UserEntity signinUserEntity = userService.getLoginUserByUserId(signinUser);

        List<String> games = new ArrayList<>();
        List<Long> gameIds = signinUserEntity.getSelectedGame();
        for (Long gameId : gameIds) {
            GameDTO gameDTO = gameService.findByGameId(gameId);
            games.add(gameDTO.getName());
        };

        // 장바구니 안에 있는 장르들과 태그들 다 들고오기 [장르, 태그]
        List<String> basketGamesTags = gameService.getGameGenresTags(games);

        // 장바구니 안에 있는 게임들의 장르들과 태그들의 중복정도 체크 <태그 : 횟수>
        Map<String, Integer> dupCheck = gameService.listDupCheck(basketGamesTags);

        // 장바구니 제외한 게임들의 <게임명, [장르, 태그]>
        Map<String, List<String>> gamesWithoutBasket = gameService.getGameGenresTagsWithoutBasket(games);

        basketGamesTags.stream().distinct().collect(Collectors.toList());

        List<String> sortedList = rsService.calculateSimilarities(rsService.convertBasketTagToVectors(basketGamesTags), rsService.convertGameTagsToVectors(gamesWithoutBasket));

        // 장바구니 내부에서 카운트가 높은 태그 순으로 5개 가져오기
        List<String> fiveTags = gameService.getFiveTags(dupCheck);
        signinUserEntity.setRecommendedGameTags(fiveTags);

        List<List<String>> fiveTagsGameList = gameService.getFiveTagsGameList(fiveTags, sortedList);

        List<List<Long>> finalFiveTagsGameList = new ArrayList<>();
        for(List<String> GameList : fiveTagsGameList ) {
            List<Long> temp = new ArrayList<>();
            for (String game : GameList){
                GameDTO gameDTO = gameService.findByName(game);
                temp.add(gameDTO.getId());
            }
            finalFiveTagsGameList.add(temp);
        }
        signinUserEntity.setRecommendedGame(finalFiveTagsGameList);

        userRepository.save(signinUserEntity);
    };
}
