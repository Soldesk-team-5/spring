package com.soldesk.TeamProject.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GameDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> genre;
    private List<String> tag;
    private String minimumSpecification;
    private String recommendedSpecification;
    private String steamLink;
    private String eval;



}
