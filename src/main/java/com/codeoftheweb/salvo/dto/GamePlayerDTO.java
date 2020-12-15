package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.util.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GamePlayerDTO {

    private GamePlayer gamePlayer;

    public GamePlayerDTO(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        PlayerDTO playerDTO = new PlayerDTO(gamePlayer.getPlayer());
        dto.put("id", this.gamePlayer.getId());
        dto.put("player", playerDTO.makePlayerDTO());

        return dto;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Map<String,  Object> makeGameViewDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();
        HitsDTO hitsDTO = new HitsDTO();
        dto.put("id", this.gamePlayer.getGame().getId());
        dto.put("created", this.gamePlayer.getGame().getCreated());
        dto.put("gamePlayers", this.gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer -> {
                    GamePlayerDTO gamePlayerDTO = new GamePlayerDTO(gamePlayer);
                    return gamePlayerDTO.makeGamePlayerDTO();
                })
                .collect(Collectors.toList()));
        dto.put("ships",  this.gamePlayer.getShips().stream().map(ships  -> {
            ShipDTO shipDTO = new ShipDTO();
            return  shipDTO.makeShipDTO(ships);
        }));
        dto.put("salvoes",  gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvos()
                        .stream()
                        .map(salvo -> {
                            SalvoDTO salvoDTO = new SalvoDTO();
                            return salvoDTO.makeSalvoDTO(salvo);
                        }))
                .collect(Collectors.toList()));
        if (gamePlayer.getGame().getGamePlayers().size() == 2){
            hits.put("self", hitsDTO.makeHitsDTO(gamePlayer));
            hits.put("opponent", hitsDTO.makeHitsDTO(gamePlayer.getOpponent()));
        }else {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        }
        dto.put("hits", hits);
        dto.put("gameState", Util.gameState(gamePlayer));
        return  dto;

    }
}
