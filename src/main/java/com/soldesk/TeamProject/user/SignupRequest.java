package com.soldesk.TeamProject.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요 ")
    private String password;

    @NotBlank(message = "비밀번호 체크를 입력해주세요 ")
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;


    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(this.userId)
                .password(this.password)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .build();
    }

}