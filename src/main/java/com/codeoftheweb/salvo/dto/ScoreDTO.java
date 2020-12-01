package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Score;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreDTO {
    public Map<String, Object> makeScoreDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", score.getId());
        dto.put("score", score.getScore());
        dto.put("finishDate", score.getFinishDate());
        dto.put("player", score.getPlayer().getEmail());
        return dto;
    }
}
