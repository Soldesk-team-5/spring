package com.soldesk.TeamProject.game;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private List<GameDTO> gameLists;

    @PostConstruct
    private void init() {

        gameLists = getAllEntity(); // gameRepository가 null이 아닐 때에만 getAllEntity() 메소드 호출

    }

    // 장르 중복제거
    public List<String> getUniqueGenres() {

        List<String> allGenres = gameRepository.findAllGenres();
        List<String> uniqueGenres = new ArrayList<>();

        for (String genres : allGenres) {
            String[] genresArray = genres.split(",");
            for (String genre : genresArray) {
                //String trimmedGenre = genre.trim();
                if (!uniqueGenres.contains(genre)) {
                    uniqueGenres.add(genre);
                }
            }
        }
        return uniqueGenres;
    }

    // 태그 중복제거
    public List<String> getUniquetags() {

        List<String> allTags = gameRepository.findAllTags();
        List<String> uniqueTags = new ArrayList<>();

        for (String tags : allTags) {
            String[] tagsArray = tags.split(",");
            for (String tag : tagsArray) {
                //String trimmedTag = tag.trim();
                if (!uniqueTags.contains(tag)) {
                    uniqueTags.add(tag);
                }
            }
        }
        return uniqueTags;
    }

    // dto는 웹 기능을 위해 장르와 태그들을 리스트로 변환 시킴 (찾는 용도로)
    public GameDTO convertToDTO(GameEntity gameEntity) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setName(gameEntity.getName());
        gameDTO.setDescription(gameEntity.getDescription());

        // genre list로 만들어서 dto 저장
        List<String> genres_list = new ArrayList<>();
        String genres = gameEntity.getGenre();
        String[] genresArray = genres.split(",");
        for (String genre : genresArray) {
            //String trimmedGenre = genre.trim();
            if (!genres_list.contains(genre)) {
                genres_list.add(genre);
            }
        }
        gameDTO.setGenre(genres_list);

        // tag list로 만들어서 dto 저장
        List<String> tags_list = new ArrayList<>();
        String tags = gameEntity.getTag();
        String[] tagsArray = tags.split(",");
        for (String tag : tagsArray) {
            //String trimmedTag = tag.trim();
            if (!tags_list.contains(tag)) {
                tags_list.add(tag);
            }
        }
        gameDTO.setTag(tags_list);

        gameDTO.setMinimumSpecification(gameEntity.getMinimumSpecification());
        gameDTO.setRecommendedSpecification(gameEntity.getRecommendedSpecification());
        gameDTO.setSteamLink(gameEntity.getSteamLink());
        gameDTO.setEval(gameEntity.getEval());
        return gameDTO;
    }

    // 모든 entity를 들고와서 dto 리스트에 저장
    private List<GameDTO> getAllEntity() {
        List<GameDTO> gameLists = new ArrayList<>();
        List<GameEntity> gameEntities = gameRepository.findAll();
        int gamesNum = gameRepository.findGameNum();
        for (int i=0; i < gamesNum; i++) {
            GameDTO dto = convertToDTO(gameEntities.get(i));
            gameLists.add(dto);
        };
        return gameLists;
    };
    
    // 모든 entity가 서버에 dto list에 담겨 저장됨


    public List<String> findByGenre(List<String> genres) {
        List<String> selectedGame = new ArrayList<>();
        int gamesNum = gameRepository.findGameNum();
        for (int j = 0; j < genres.size(); j++) {
            for (int i = 0; i < gamesNum; i++) {
                GameDTO dto = gameLists.get(i);
                List<String> genreList = dto.getGenre();
                if (genreList.contains(genres.get(j))) {
                    selectedGame.add(dto.getName());
                };
            };
        };
        return selectedGame;
    };

    public List<String> findByTag(List<String> tags) {
        List<String> selectedGame = new ArrayList<>();
        int gamesNum = gameRepository.findGameNum();
        for (int j = 0; j < tags.size(); j++) {
            for (int i = 0; i < gamesNum; i++) {
                GameDTO dto = gameLists.get(i);
                List<String> tagList = dto.getTag();
                if (tagList.contains(tags.get(j))) {
                    selectedGame.add(dto.getName());
                };
            };
        };
        return selectedGame;
    };

    public List<String> allGame() {
        List<String> selectedGame = new ArrayList<>();
        int gamesNum = gameRepository.findGameNum();
        for (int i = 0; i < gamesNum; i++) {
            GameDTO dto = gameLists.get(i);
            selectedGame.add(dto.getName());
        };
        return selectedGame;
    };

}
