package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private long turn;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="GamePlayerID")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "locations")
    private List<String> locations;

    public Salvo() {
        this.locations = new ArrayList<>();
    }

    public Salvo(Long turn, GamePlayer gamePlayer, List<String> locations) {
        this.turn = turn;
        this.gamePlayer = gamePlayer;
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTurn() {
        return turn;
    }

    public void setTurn(long turn) {
        this.turn = turn;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
