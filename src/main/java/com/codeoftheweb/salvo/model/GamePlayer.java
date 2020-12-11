package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PlayerID")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GameID")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<Ship>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvos = new HashSet<Salvo>();

    public GamePlayer( Player player, Game game) {
        this.joinDate = new Date();
        this.player = player;
        this.game = game;
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvos.add(salvo);
    }

    public GamePlayer getOpponent(){
        return this.getGame().getGamePlayers()
                .stream().filter(gamePlayer -> gamePlayer.getId() !=this.getId())
                .findFirst()
                .orElse(null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }

    public GamePlayer() {
    }


}