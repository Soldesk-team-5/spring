package com.soldesk.TeamProject.game;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private List<GameDTO> gameLists;

    @PostConstruct
    private void init() {

        gameLists = getAllEntity(); // getAllEntity() 메소드 호출

    }

    // 모든 게임 DTO를 가져오는 메소드
    public List<GameDTO> getAllGames() {
        if (gameLists == null) {
            gameLists = getAllEntity();
        }
        return gameLists;
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

    // dto는 웹 기능을 위해 장르와 태그들을 리스트로 변환 시킴 (찾는 용도로) => 하나라도 충족하면 모두 가져오기
    public GameDTO convertToDTO(GameEntity gameEntity) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(gameEntity.getId());
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
    
    // 장르 태그 모두 충족하는 게임들만 가져오기
    // 게임의 장르들이 모두 주어진 장르들과 일치하는지 체크하
    public boolean isGenresMatched(GameDTO game, List<String> genres) {
        if (genres == null || genres.isEmpty()) {
            return true; // 장르가 주어지지 않은 경우, 모든 게임을 매칭
        }
        List<String> gameGenres = game.getGenre();
        return gameGenres.containsAll(genres); // 모든 장르가 게임 장르에 포함되는지 체크
    }

    // 게임의 태그들이 모두 주어진 태그들과 일치하는지 체크
    public boolean isTagsMatched(GameDTO game, List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return true; // 태그가 주어지지 않은 경우, 모든 게임을 매칭함
        }
        List<String> gameTags = game.getTag();
        return gameTags.containsAll(tags); // 모든 태그가 게임 태그에 포함되는지 체크
    }

    // 모든 entity를 들고와서 dto 리스트에 저장
    public List<GameDTO> getAllEntity() {
        List<GameDTO> gameLists = new ArrayList<>();
        List<GameEntity> gameEntities = gameRepository.findAll();
        int gamesNum = gameRepository.findGameNum();
        for (int i=0; i < gamesNum; i++) {
            GameDTO dto = convertToDTO(gameEntities.get(i));
            gameLists.add(dto);
        };
        // 모든 entity가 서버에 dto list에 담겨 저장됨
        return gameLists;
    };

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

    // 이름으로 게임찾기
    public GameDTO findByGameId(Long gameId) {

        for (GameDTO gameDTO : gameLists) {
            if(gameDTO.getId().equals(gameId)){
                return gameDTO;
            };
        };
        return null;
    };

    public GameDTO findByName(String name) {

        for (GameDTO gameDTO : gameLists) {
            if(gameDTO.getName().equals(name)){
                return gameDTO;
            };
        };
        return null;
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

    // 게임명으로 게임들의 장르와 태그들 가져오기 => list 형태로 return (장바구니에서 선택된 게임들의 정보 가져올 때 사용)
    public List<String> getGameGenresTags(List<String> gamenames){
        List<String> genresTags = new ArrayList<>();
        for (String gamename : gamenames) {
            for (GameDTO gameDTO : gameLists) {
                if (gameDTO.getName().equals(gamename)) {
                    List<String> genres = gameDTO.getGenre();
                    List<String> tags = gameDTO.getTag();
                    List<String> mergedList = new ArrayList<>(genres);
                    mergedList.removeAll(tags);
                    mergedList.addAll(tags);

                    for (String tag : mergedList){
                    genresTags.add(tag);
                    };
                };
            };
        };
        // 해당하는 게임명이 없을 때 (이럴 일은 사실 없음)
        return genresTags;
    };

    public Map<String, Integer> listDupCheck(List<String> genresTags){
        Map<String, Integer> dupCheck = new HashMap<String, Integer>();
        for (String str : genresTags) {
            Integer count = dupCheck.get(str);
            if (count == null) {
                dupCheck.put(str, 1);
            } else {
                dupCheck.put(str, count + 1);
            }
        }

        return dupCheck;
        // dupCheck.getKey() 키값 / dupCheck.getValue() value값
    };

    // 내부에 있는 게임 데이터 가져오기 단 (장바구니에 있는거 제외)
    public Map<String, List<String>> getGameGenresTagsWithoutBasket(List<String> gamenames){
        Map<String, List<String>> games = new HashMap<>();

        for (GameDTO gameDTO : gameLists) {
            if (!gamenames.contains(gameDTO.getName())) {
                List<String> genres = gameDTO.getGenre();
                List<String> tags = gameDTO.getTag();
                List<String> mergedList = new ArrayList<>(genres);
                mergedList.removeAll(tags);
                mergedList.addAll(tags);

                games.put(gameDTO.getName(), mergedList);
            };

        };
        // 해당하는 게임명이 없을 때 (이럴 일은 사실 없음)
        return games;
    };

    // 태그 5개만 가져오기
    public List<String> getFiveTags(Map<String, Integer> dupCheck){
        List<Map.Entry<String, Integer>> entryList = new LinkedList<>(dupCheck.entrySet());
        entryList.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        System.out.println(entryList);
        List<String> tags = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            tags.add(entry.getKey());
            if (tags.size() >= 5) {
                break;
            };
        };
        return tags;
    };

    // 태그 5개가 해당하는 게임들 중복제거해서 가져오기
    public List<List<String>> getFiveTagsGameList(List<String> fiveTags, List<String> sortedList) {
        List<List<String>> fiveTagsGameList = new ArrayList<>();
        List<String> remainingGames = new ArrayList<>(sortedList); // sortedList를 수정하지 않고 복사본을 만듦

        for (String tag : fiveTags) {
            List<String> tagsGameList = new ArrayList<>();

            // 복사본인 remainingGames를 사용하여 반복문 수행
            for (String game : remainingGames) {
                GameDTO gameDTO = findByName(game);
                if (gameDTO.getGenre().contains(tag) || gameDTO.getTag().contains(tag)) {
                    tagsGameList.add(game);
                };
                if(tagsGameList.size() >= 10){
                    break;
                };
            };

            // 해당 태그와 매칭된 게임들을 tagsGameList에 추가하고, remainingGames에서 삭제
            tagsGameList.forEach(remainingGames::remove);
            fiveTagsGameList.add(tagsGameList);
        };
        return fiveTagsGameList;
    };
}
