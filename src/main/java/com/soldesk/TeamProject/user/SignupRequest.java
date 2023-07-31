package com.soldesk.TeamProject.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    private boolean hasErrors;

    private String userId;

    private String password;

    private String passwordCheck;

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