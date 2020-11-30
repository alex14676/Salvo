package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Salvo;
import java.util.LinkedHashMap;
import java.util.Map;

public class SalvoDTO {
    public Map<String, Object> makeSalvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", salvo.getTurn());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getLocations());
        return dto;
    }
}
