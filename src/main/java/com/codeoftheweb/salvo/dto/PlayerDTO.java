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
        dto.put("nombre", this.player.getName());

        return dto;
    }

    public Map<String, Object> makePlayerScoreDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> score = new LinkedHashMap<>();

        dto.put("id", this.player.getId());
        dto.put("email", this.player.getEmail());
        dto.put("score", score);
            score.put("total", this.player.getTotalScore());
            score.put("won", this.player.getWonScore());
            score.put("lost", this.player.getLostScore());
            score.put("tied", this.player.getTiedScore());
        return dto;
    }

}
