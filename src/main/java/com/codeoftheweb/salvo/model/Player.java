package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Set;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String email;
    private String name;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayerSet;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public long getWonScore(){
        return this.getScores()
                .stream()
                .filter(score -> score.getScore() == 1.0D)
                .count();
    }

    public long getLostScore(){
        return this.getScores().stream()
                .filter(score -> score.getScore() == 0.0D)
                .count();
    }

    public long getTiedScore(){
        return this.getScores()
                .stream()
                .filter(score -> score.getScore() == 0.5D)
                .count();
    }

    public double getTotalScore(){
        return getWonScore() * 1.0D + getLostScore() * 0.0D + getTiedScore() * 0.5D;
    }

    public void addScore(Score score){
        scores.add(score);
    }

    public Player() { }

    public Player(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name + " " + email;
    }

    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayerSet;
    }

    public void setGamePlayerSet(Set<GamePlayer> gamePlayerSet) {
        this.gamePlayerSet = gamePlayerSet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}

