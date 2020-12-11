package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dto.SalvoDTO;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.SalvoRepository;
import com.codeoftheweb.salvo.repository.ScoreRepository;
import com.codeoftheweb.salvo.util.Util;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.codeoftheweb.salvo.util.Util.getGameState;
import static com.codeoftheweb.salvo.util.Util.outOfBoundsLocation;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SalvoRepository salvoRepository;

    @Autowired
    ScoreRepository scoreRepository;

    private void setGamePlayerListSalvos(List<Salvo> salvos, GamePlayer gamePlayerId) {
        for (Salvo salvo : salvos) {
            salvo.setGamePlayer(gamePlayerId);
        }
    }

    @RequestMapping(path = "/games/players/{gamePlayerID}/salvoes", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>>getSalvoes(@PathVariable long gamePlayerID,

                                                         Authentication authentication) {
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "You must Log In"), HttpStatus.FORBIDDEN);
        }
        Map dto = new LinkedHashMap();
        Player player = playerRepository.findByEmail(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerID).get();

        if (!(gamePlayer.getPlayer().getId() == player.getId())){
            return new ResponseEntity<>(Util.makeMap("error", "This is NOT your game"), HttpStatus.FORBIDDEN);
        }
        dto.put("salvoes", gamePlayer.getSalvos()
                .stream()
                .map(salvo -> SalvoDTO.makeSalvoDTO(salvo))
                .collect(Collectors.toList()));
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @RequestMapping(path = "games/players/{gamePlayerID}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Object>setSalvoes(@PathVariable long gamePlayerID, @RequestBody Salvo salvo, Authentication authentication){
        if(Util.isGuest(authentication))
            return new ResponseEntity<>(Util.makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);
        Player player = playerRepository.findByEmail(authentication.getName());
        if(player == null)
            return new ResponseEntity<>(Util.makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        GamePlayer gamePlayerMe = gamePlayerRepository.findById(gamePlayerID).orElse(null);
        if(gamePlayerMe == null)
            return new ResponseEntity<>(Util.makeMap("error", "Database error. GamePlayer not found."), HttpStatus.INTERNAL_SERVER_ERROR);
        if(player != gamePlayerMe.getPlayer())
            return new ResponseEntity<>(Util.makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);
        if(!getGameState(gamePlayerMe).equals("PLAY"))
            return new ResponseEntity<>(Util.makeMap("error", "Playing is not allowed"), HttpStatus.UNAUTHORIZED);

        if(gamePlayerMe.getOpponent() == null)
            return new ResponseEntity<>(Util.makeMap("error", "You don\'t have a rival yet!"), HttpStatus.FORBIDDEN);

        String status = salvoValidity(salvo);
        if(status != "OK")
            return new ResponseEntity<>(Util.makeMap("error", "Invalid salvo! " + status), HttpStatus.FORBIDDEN);

        for(Salvo pastSalvo : gamePlayerMe.getSalvos())
            if(overlapSalvoes(pastSalvo, salvo))
                return new ResponseEntity<>(Util.makeMap("error", "Overlapping salvoes!"), HttpStatus.FORBIDDEN);

        int myTurn = gamePlayerMe.getSalvos().size();
        int opponentTurn = gamePlayerMe.getOpponent().getSalvos().size();
        if(myTurn > opponentTurn)
            return new ResponseEntity<>(Util.makeMap("error", "Wait for your rival to finish this turn"), HttpStatus.FORBIDDEN);
        if(myTurn+1 < opponentTurn)
            return new ResponseEntity<>(Util.makeMap("error", "Server problem. You are many turns behind your rival."), HttpStatus.INTERNAL_SERVER_ERROR);

        salvo.setTurn((long) (myTurn+1));
        gamePlayerMe.addSalvo(salvo);
        salvoRepository.save(salvo);
        return new ResponseEntity<>(Util.makeMap("OK", "Your salvoes were fired!"), HttpStatus.CREATED);
    }

    private boolean overlapSalvoes(Salvo salvo1, Salvo salvo2){
        for(String location : salvo1.getLocations())
            if(salvo2.getLocations().contains(location))
                return true;
        return false;
    }

    private String salvoValidity(Salvo salvo){
        if(salvo.getLocations().size() > 5)
            return "Too many shots!";

        for(String location : salvo.getLocations())if(outOfBoundsLocation(location))
            return "Location out of bounds.";

        long dist = salvo.getLocations().stream().distinct().count();
        long total = salvo.getLocations().size();
        if(dist < total)
            return "Locations repeated!";

        return "OK";
    }

}
