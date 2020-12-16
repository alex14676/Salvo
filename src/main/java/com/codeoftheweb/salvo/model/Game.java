package com.codeoftheweb.salvo.model;


import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private ZonedDateTime created;

    @OneToMany(mappedBy = "game")
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game(ZonedDateTime date) {
        this.created = date;
    }

    public Game(){this.created = ZonedDateTime.now();}

    public Game(Set<Score> scores) {
        this.scores = scores;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public long getId() {
        return id;
    }

    public ZonedDateTime getDate() {
        return created;
    }

    public void setDate(ZonedDateTime date) {
        this.created = date;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

}
