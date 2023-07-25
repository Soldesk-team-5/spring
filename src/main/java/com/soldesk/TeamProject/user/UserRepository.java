package com.soldesk.TeamProject.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //어떤 entity, pk 어떤 타입인지를 파악 => DB에서 찾음
public interface  UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByNickname(String nickname);
}
