package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.GamePlayerDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.dto.ScoreDTO;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    GameRepository gameRepository;

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

    @RequestMapping("/games")
    public List<Map<String, Object>> getGameAll() {
        return gameRepository.findAll()
                .stream()
                .map(game -> {
                    GameDTO gameDTO = new GameDTO(game);
                    return gameDTO.makeGameDTO();
                })
                .collect(Collectors.toList());
    }

    @RequestMapping("/scores")
    public List<Map<String, Object>> getScoreALL(){
        return scoreRepository.findAll()
                .stream()
                .map(score -> {
                    ScoreDTO scoreDTO = new ScoreDTO();
                    return scoreDTO.makeScoreDTO(score);
                })
                .collect(Collectors.toList());
    }

    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderBoard() {
        return playerRepository.findAll().stream()
                .map(player -> {
                    PlayerDTO playerDTO = new PlayerDTO(player);
                    return playerDTO.makePlayerScoreDTO();
                })
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameView(@PathVariable  long  nn){
        GamePlayer  gamePlayer  = gamePlayerRepository.findById(nn).get();
        GamePlayerDTO gamePlayerDTO = new GamePlayerDTO(gamePlayer);
        return  gamePlayerDTO.makeGameViewDTO();
    }

}
