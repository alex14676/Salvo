package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.util.Util;

import java.util.*;


public class HitsDTO {
    private GamePlayer gamePlayer;

    public HitsDTO() {
    }

    public HitsDTO(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<Map<String, Object>> makeHitsDTO(GamePlayer gamePlayer) {
        List<Map<String, Object>> hits = new ArrayList<>();
        long carrierDamage = 0;
        long battleshipDamage = 0;
        long submarineDamage = 0;
        long destroyerDamage = 0;
        long patrolboatDamage = 0;


        List<String> carrierLocations = Util.getLocationsByType("carrier",gamePlayer);
        List<String> battleshipLocations = Util.getLocationsByType("battleship",gamePlayer);
        List<String> submarineLocations = Util.getLocationsByType("submarine",gamePlayer);
        List<String> destroyerLocations = Util.getLocationsByType("destroyer",gamePlayer);
        List<String> patrolBoatLocations = Util.getLocationsByType("patrolboat",gamePlayer);


        for (Salvo salvoShot : gamePlayer.getOpponent().getSalvos()) {
            Map<String, Long> damagesPerTurn = new LinkedHashMap<>();

            Map<String, Object> hitsMapPerTurn = new LinkedHashMap<>();
            List<String> hitCellsList = new ArrayList<>();
            int missedShots = salvoShot.getLocations().size();
            long carrierHitsInTurn = 0;
            long battleshipHitsInTurn = 0;
            long submarineHitsInTurn = 0;
            long destroyerHitsInTurn = 0;
            long patrolboatHitsInTurn= 0;

            for (String location : salvoShot.getLocations()) {



                if (carrierLocations.contains(location)) {
                    hitCellsList.add(location);
                    carrierHitsInTurn++;
                    carrierDamage++;
                    missedShots--;
                }

                if (battleshipLocations.contains(location)) {
                    hitCellsList.add(location);
                    battleshipHitsInTurn++;
                    battleshipDamage++;
                    missedShots--;
                }

                if (submarineLocations.contains(location)) {
                    hitCellsList.add(location);
                    submarineHitsInTurn++;
                    submarineDamage++;
                    missedShots--;
                }

                if (destroyerLocations.contains(location)) {
                    hitCellsList.add(location);
                    destroyerHitsInTurn++;
                    destroyerDamage++;
                    missedShots--;
                }

                if (patrolBoatLocations.contains(location)) {
                    hitCellsList.add(location);
                    patrolboatHitsInTurn++;
                    patrolboatDamage++;
                    missedShots--;
                }



            }
            damagesPerTurn.put("carrierHits", carrierHitsInTurn);
            damagesPerTurn.put("battleshipHits", battleshipHitsInTurn);
            damagesPerTurn.put("submarineHits", submarineHitsInTurn);
            damagesPerTurn.put("destroyerHits", destroyerHitsInTurn);
            damagesPerTurn.put("patrolboatHits", patrolboatHitsInTurn);
            hitsMapPerTurn.put("turn", salvoShot.getTurn());
            hitsMapPerTurn.put("hitLocations", hitCellsList);
            hitsMapPerTurn.put("damages", damagesPerTurn);
            hitsMapPerTurn.put("missed", missedShots);
            hits.add(hitsMapPerTurn);
            damagesPerTurn.put("carrier", carrierDamage);
            damagesPerTurn.put("battleship", battleshipDamage);
            damagesPerTurn.put("submarine", submarineDamage);
            damagesPerTurn.put("destroyer", destroyerDamage);
            damagesPerTurn.put("patrolboat", patrolboatDamage);

        }



        return hits;
    }
}