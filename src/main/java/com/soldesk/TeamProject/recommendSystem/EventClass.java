package com.soldesk.TeamProject.recommendSystem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventClass {

    private List<Long> selectedGames;

    private String signinUser;

    public EventClass(List<Long> selectedGames, String signinUser) {
        this.selectedGames = selectedGames;
        this.signinUser = signinUser;
    }


    public List<Long> getSelectedGames(){
        return selectedGames;
    }

    public String getSigninUser(){
        return signinUser;
    }
}
