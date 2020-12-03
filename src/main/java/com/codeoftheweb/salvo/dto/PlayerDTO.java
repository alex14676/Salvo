package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Player;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerDTO {

    private Player player;

    public PlayerDTO(Player player) {
        this.player = player;
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", this.player.getId());
        dto.put("email", this.player.getEmail());
        dto.put("name", this.player.getName());

        return dto;
    }

    public Map<String, Object> makePlayerScoreDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> scores = new LinkedHashMap<>();
        dto.put("id", this.player.getId());
        dto.put("email", this.player.getEmail());
        dto.put("scores", scores);
            scores.put("total", this.player.getTotalScore());
            scores.put("won", this.player.getWonScore());
            scores.put("lost", this.player.getLostScore());
            scores.put("tied", this.player.getTiedScore());
        return dto;
    }

}
