package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;



@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}




	@Bean
	public CommandLineRunner initData(PlayerRepository repository,
									  GameRepository Grepository,
									  GamePlayerRepository GPrepository,
									  ShipRepository SRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {

		//Players
			Player player1 = new Player("david@gmail.com","David", passwordEncoder().encode("12"));
			Player player2 = new Player("rocket@gmail.com","Rocket", passwordEncoder().encode(""));
			Player player3 = new Player("alex14676@hotmail.com","Alex", passwordEncoder().encode("46445883"));
			Player player4 = new Player("nacho@gmail.com","Nacho", passwordEncoder().encode(""));
			Player player5 = new Player("juan@gmail.com","Juan", passwordEncoder().encode(""));
			Player player6 = new Player("sergio@gmail.com","Sergio", passwordEncoder().encode(""));

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
			Salvo salvo1 = new Salvo(1, gamePlayer1, List.of("A1", "B1", "C1"));
			Salvo salvo2 = new Salvo(1,  gamePlayer2, List.of("A2", "B2", "C2"));

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);


		//Scores
			Score score1 = new Score(player1, game1, 1, LocalDateTime.now());
			Score score2 = new Score(player2, game1, 0, LocalDateTime.now());
			Score score3 = new Score(player1, game1, 0, LocalDateTime.now());
			Score score4 = new Score(player2, game1, 1, LocalDateTime.now());
			Score score5 = new Score(player1, game1, 0.5, LocalDateTime.now());
			Score score6 = new Score(player2, game1, 0.5, LocalDateTime.now());

			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);

		};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputEmail-> {
			Player player = playerRepository.findByEmail(inputEmail);
			if (player != null) {
				return new User(player.getEmail(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputEmail);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/game_view/*").hasAuthority("USER")
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/api/games").permitAll();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}




