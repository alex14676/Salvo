package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameDTO {

    private Game game;

    public GameDTO(Game game) {
        this.game = game;
    }


    public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", this.game.getId());
        dto.put("created", this.game.getCreated());
        dto.put("gamePlayers", this.game.getGamePlayers()
                .stream()
                .map(gamePlayer -> {
                    GamePlayerDTO gamePlayerDTO = new GamePlayerDTO(gamePlayer);
                    return gamePlayerDTO.makeGamePlayerDTO();})
                .collect(Collectors.toList()));
        dto.put("scores", this.game.getScores()
                .stream()
                .map(score -> {
                    ScoreDTO scoreDTO = new ScoreDTO();
                    return scoreDTO.makeScoreDTO(score);})
                .collect(Collectors.toList()));
        return dto;
    }

}