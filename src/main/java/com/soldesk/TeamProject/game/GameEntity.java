package com.soldesk.TeamProject.game;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gamedata")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity {
    @Id
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String genre;

    @Column(length = 255)
    private String tag;

    @Column(name = "spec_min", columnDefinition = "TEXT")
    private String minimumSpecification;

    @Column(name = "spec_rec", columnDefinition = "TEXT")
    private String recommendedSpecification;

    @Column(name = "steam_link")
    private String steamLink;

    @Column
    private String eval;

    private GameEntity convertToEntity(GameDTO gameDTO) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setName(gameDTO.getName());
        gameEntity.setDescription(gameDTO.getDescription());
        gameEntity.setGenre(String.join(",", gameDTO.getGenre()));
        gameEntity.setTag(String.join(",", gameDTO.getTag()));
        gameEntity.setMinimumSpecification(gameDTO.getMinimumSpecification());
        gameEntity.setRecommendedSpecification(gameDTO.getRecommendedSpecification());
        gameEntity.setSteamLink(gameDTO.getSteamLink());
        gameEntity.setEval(gameDTO.getEval());
        return gameEntity;
    }
}