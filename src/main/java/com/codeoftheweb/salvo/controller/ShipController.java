package com.codeoftheweb.salvo.controller;


import com.codeoftheweb.salvo.dto.ShipDTO;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    ShipRepository shipRepository;

    private void setGamePlayerListShips(List<Ship> ships, GamePlayer gamePlayerId) {
        for (Ship ship : ships) {
            ship.setGamePlayer(gamePlayerId);
        }
    }

    @RequestMapping(path = "/games/players/{gamePlayerID}/ships", method = RequestMethod.GET)
    public ResponseEntity<Object>getShips(@PathVariable long gamePlayerID, Authentication authentication){
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "You must Log In"), HttpStatus.FORBIDDEN);
        }
        List<Map<String, Object>> dto;
        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerID).get();

        if (!(gamePlayer.getPlayer().getId() == player.getId())){
            return new ResponseEntity<>("Error, This is NOT your game", HttpStatus.FORBIDDEN);
        }
        dto = gamePlayer.getShips().stream().map(ship -> ShipDTO.makeShipDTO(ship)).collect(Collectors.toList());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gamePlayerID}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setShips(@PathVariable Long gamePlayerID, @RequestBody List<Ship> ships, Authentication authentication) {
        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer gamePlayer= gamePlayerRepository.findById(gamePlayerID).get();

        if(player.getId()== gamePlayer.getPlayer().getId()){
            if(gamePlayer.getShips().size() + ships.size()>=5){
                setGamePlayerListShips(ships, gamePlayer);
                shipRepository.saveAll(ships);
                return new ResponseEntity<>(Util.makeMap("OK", "Saved ships"), HttpStatus.CREATED);
            }else {
                return new ResponseEntity<>(Util.makeMap("error", "Ships full"), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<>(Util.makeMap("error", "This not your section"), HttpStatus.UNAUTHORIZED);
        }
    }
}
