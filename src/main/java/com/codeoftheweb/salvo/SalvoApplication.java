package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);


	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository Grepository, GamePlayerRepository GPrepository, ShipRepository SRepository, SalvoRepository salvoRepository) {
		return (args) -> {

		//Players
			Player player1 = new Player("david@gmail.com","David");
			Player player2 = new Player("rocket@gmail.com","Rocket");
			Player player3 = new Player("alex@gmail.com","Alex");
			Player player4 = new Player("nacho@gmail.com","Nacho");
			Player player5 = new Player("juan@gmail.com","Juan");
			Player player6 = new Player("sergio@gmail.com","Sergio");

			repository.save(player1);
			repository.save(player2);
			repository.save(player3);
			repository.save(player4);
			repository.save(player5);
			repository.save(player6);

		//Games
			Game game1 = new Game(LocalDateTime.now().plusHours(0));
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			Game game3 = new Game(LocalDateTime.now().plusHours(2));
			Grepository.save(game1);
			Grepository.save(game2);
			Grepository.save(game3);

			//GamePlayers
			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
			GamePlayer gamePlayer2 = new GamePlayer(player2, game1);
			GamePlayer gamePlayer3 = new GamePlayer(player3, game2);
			GamePlayer gamePlayer4 = new GamePlayer(player4, game2);
			GamePlayer gamePlayer5 = new GamePlayer(player5, game3);
			GamePlayer gamePlayer6 = new GamePlayer(player6, game3);

			GPrepository.save(gamePlayer1);
			GPrepository.save(gamePlayer2);
			GPrepository.save(gamePlayer3);
			GPrepository.save(gamePlayer4);
			GPrepository.save(gamePlayer5);
			GPrepository.save(gamePlayer6);

		//Ships
			Ship ship1 = new Ship("Carrier", List.of("A1", "A2", "A3","A4", "A5"), gamePlayer1);
			Ship ship2 = new Ship("Battleship", List.of("B1", "B2", "B3","B4"), gamePlayer1);
			Ship ship3 = new Ship("Submarine", List.of("C1", "C2", "C3"), gamePlayer1);
			Ship ship4 = new Ship("Destroyer", List.of("D1", "D2", "D3"), gamePlayer1);
			Ship ship5 = new Ship("PatrolBoat", List.of("E1", "E2"), gamePlayer1);
			Ship ship6 = new Ship("Carrier", List.of("A1", "A2", "A3","A4", "A5"), gamePlayer2);
			Ship ship7 = new Ship("Battleship", List.of("B1", "B2", "B3","B4"),gamePlayer2);
			Ship ship8 = new Ship("Submarine", List.of("C1", "C2", "C3"), gamePlayer2);
			Ship ship9 = new Ship("Destroyer", List.of("D1", "D2", "D3"), gamePlayer2);
			Ship ship10 = new Ship("PatrolBoat", List.of("E1", "E2"), gamePlayer2);

			SRepository.save(ship1);
			SRepository.save(ship2);
			SRepository.save(ship3);
			SRepository.save(ship4);
			SRepository.save(ship5);
			SRepository.save(ship6);
			SRepository.save(ship7);
			SRepository.save(ship8);
			SRepository.save(ship9);
			SRepository.save(ship10);

		//Salvos
			Salvo salvo1 = new Salvo();
		};
	}
}
