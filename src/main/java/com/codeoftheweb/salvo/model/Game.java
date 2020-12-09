package com.codeoftheweb.salvo.model;


import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime created;

    @OneToMany(mappedBy = "game")
    private Set<GamePlayer> gamePlayers;

    public Game(LocalDateTime date) {
        this.created = date;
    }

    public Game(){this.created = LocalDateTime.now();}


    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return created;
    }

    public void setDate(LocalDateTime date) {
        this.created = date;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
}
