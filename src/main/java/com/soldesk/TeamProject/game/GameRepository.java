package com.soldesk.TeamProject.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    @Query(value = "SELECT g.genre FROM GameEntity g")
    List<String> findAllGenres();

    @Query(value = "SELECT g.tag FROM GameEntity g")
    List<String> findAllTags();

    List<GameEntity> findAll();

    @Query(value = "SELECT max(id) from GameEntity")
    int findGameNum();

    @Query(value = "SELECT g FROM GameEntity g WHERE g.name = :game_name")
    GameEntity findGameByname(@Param("game_name") String name);
}