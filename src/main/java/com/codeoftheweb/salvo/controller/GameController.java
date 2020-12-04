package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.GameDTO;
import com.codeoftheweb.salvo.dto.PlayerDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class GameController {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public Map<String, Object> getGameAll(Authentication authentication) {
        Map<String,  Object>  dto = new LinkedHashMap<>();

        if(Util.isGuest(authentication)){
            dto.put("player", "Guest");
        }else{
            Player player  = playerRepository.findByEmail(authentication.getName());
            PlayerDTO playerDTO   =   new PlayerDTO(player);
            dto.put("player", playerDTO.makePlayerDTO());
        }

        dto.put("games", gameRepository.findAll()
                .stream()
                .map(game -> {
                    GameDTO gameDTO =   new GameDTO(game);
                    return  gameDTO.makeGameDTO();
                })
                .collect(Collectors.toList()));

        return dto;

    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> Create(Authentication authentication) {
        if(Util.isGuest(authentication))
            return new ResponseEntity<>(Util.makeMap("error", "You are not logged in."), HttpStatus.FORBIDDEN);

        Player player = playerRepository.findByEmail(authentication.getName());
        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(player, game);

        gameRepository.save(game);
        gamePlayerRepository.save(gamePlayer);
        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping("/game/{idGame}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long idGame, Authentication authentication) {
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "is guest"), HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByEmail(authentication.getName());
        Game gameToJoin = gameRepository.getOne(idGame);

        if (gameToJoin == null){
            return new ResponseEntity<>(Util.makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
        }

        long gamePlayersCount = gameToJoin.getGamePlayers().size();

        if (gamePlayersCount == 1) {
            GamePlayer gamePlayer = new GamePlayer(player, gameToJoin);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(Util.makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }
    }
}
