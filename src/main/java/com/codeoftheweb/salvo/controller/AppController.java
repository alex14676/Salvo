package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.GamePlayerDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.dto.ScoreDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Score;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
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

        if(Util.gameState(gamePlayer) == "WON"){
            if(gamePlayer.getGame().getScores().size()<2) {
                Set<Score> scores = new HashSet<>();
                Score scoreSelf = new Score();
                scoreSelf.setPlayer(gamePlayer.getPlayer());
                scoreSelf.setGame(gamePlayer.getGame());
                scoreSelf.setFinishDate(LocalDateTime.now());
                scoreSelf.setScore(1.0D);
                scoreRepository.save(scoreSelf);
                Score scoreOpponent = new Score();
                scoreOpponent.setPlayer(gamePlayer.getOpponent().getPlayer());
                scoreOpponent.setGame(gamePlayer.getGame());
                scoreOpponent.setFinishDate(LocalDateTime.now());
                scoreOpponent.setScore(0.0D);
                scoreRepository.save(scoreOpponent);
                scores.add(scoreSelf);
                scores.add(scoreOpponent);

                gamePlayer.getPlayer().addScore(scoreSelf);
                gamePlayer.getOpponent().getPlayer().addScore(scoreOpponent);

            }
        }
        if(Util.gameState(gamePlayer) == "TIE"){
            if(gamePlayer.getGame().getScores().size()<2) {
                Set<Score> scores = new HashSet<>();
                Score scoreSelf = new Score();
                scoreSelf.setPlayer(gamePlayer.getPlayer());
                scoreSelf.setGame(gamePlayer.getGame());
                scoreSelf.setFinishDate(LocalDateTime.now());
                scoreSelf.setScore(0.5D);
                scoreRepository.save(scoreSelf);
                Score scoreOpponent = new Score();
                scoreOpponent.setPlayer(gamePlayer.getOpponent().getPlayer());
                scoreOpponent.setGame(gamePlayer.getGame());
                scoreOpponent.setFinishDate(LocalDateTime.now());
                scoreOpponent.setScore(0.5D);
                scoreRepository.save(scoreOpponent);
                scores.add(scoreSelf);
                scores.add(scoreOpponent);

                gamePlayer.getPlayer().addScore(scoreSelf);
                gamePlayer.getOpponent().getPlayer().addScore(scoreOpponent);
            }
        }

        GamePlayerDTO gamePlayerDTO = new GamePlayerDTO(gamePlayer);
        return new ResponseEntity<>(gamePlayerDTO.makeGameViewDTO(), HttpStatus.ACCEPTED);
    }

}
