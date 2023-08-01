package com.soldesk.TeamProject.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public enum UserRole {
        USER, ADMIN;
    }

    //userId 중복 체크 in 회원가입, 중복되면 true return

    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }


    // nickname 중복 체크 in 회원가입 중복되면 true return
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 회원가입 : view단에서 userId, password, nickname 입력 => User로 변환 => 저장
    public void signup(SignupRequest req) {
        userRepository.save(req.toEntity());
    }

    /**
     * 로그인 기능
     * 화면에서 userId, password 입력 => loginId와 password가 일치 => User return
     * loginId가 존재하지 않거나 password가 일치 X => null return
     */
    public UserEntity signin(SigninRequest req) {
        Optional<UserEntity> optionalUser = userRepository.findByUserId(req.getUserId());
        // userId와 일치하는 UserEntity가 없으면 => null
        if (optionalUser.isEmpty()) {
            return null;
        }

        UserEntity user = optionalUser.get();
        return user;
    }

    /**
     * userId 입력받아 UserEntity return
     * 인증, 인가 시 사용
     * userId null(로그인 X) or id로 찾아온 User가 없으면 null return
     * userId로 찾아온 User가 존재 => User return
     */
    public UserEntity getLoginUserByUserId(String userId) {
        if (userId == null) return null;

        Optional<UserEntity> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }


}
