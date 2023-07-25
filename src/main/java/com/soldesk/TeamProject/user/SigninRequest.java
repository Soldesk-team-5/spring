package com.soldesk.TeamProject.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SigninRequest {
    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;

    @NotBlank(message = "올바른 비밀번호를 입력해주세요")
    private String password;
}
