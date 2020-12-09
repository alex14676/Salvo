package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.GamePlayerDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.dto.ScoreDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ScoreRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayerAll() {
        return playerRepository.findAll()
                .stream()
                .map(player -> {
                    PlayerDTO playerDTO = new PlayerDTO(player);
                    return playerDTO.makePlayerDTO();
                })
                .collect(Collectors.toList());
    }



    @RequestMapping("/scores")
    public List<Map<String, Object>> getScoreALL(){
        return scoreRepository.findAll()
                .stream()
                .map(scores -> {
                    ScoreDTO scoreDTO = new ScoreDTO();
                    return scoreDTO.makeScoreDTO(scores);
                })
                .collect(Collectors.toList());
    }

    @RequestMapping("/leaderBoard")
    public List<Map<String, Object>> getLeaderBoard() {
        return playerRepository.findAll().stream()
                .map(player -> {
                    PlayerDTO playerDTO = new PlayerDTO(player);
                    return playerDTO.makePlayerScoreDTO();
                })
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{id}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable  long  id, Authentication authentication){
        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer  gamePlayer  = gamePlayerRepository.findById(id).get();

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("Error", "Not Logged in"), HttpStatus.UNAUTHORIZED);
        }

        if (player.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(Util.makeMap("Error", "This is NOT your game"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayerDTO gamePlayerDTO = new GamePlayerDTO(gamePlayer);
        return new ResponseEntity<>(gamePlayerDTO.makeGameViewDTO(), HttpStatus.ACCEPTED);
    }

}
