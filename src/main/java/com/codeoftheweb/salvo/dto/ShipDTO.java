package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.Ship;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShipDTO {
    public Map<String, Object> makeShipDTO(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", ship.getId());
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }
}
