package br.com.lestcode.moviebattle.test.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import br.com.lestcode.moviebattle.dto.MatchStatusDto;
import br.com.lestcode.moviebattle.dto.RankinkDto;
import br.com.lestcode.moviebattle.dto.UserGuessDto;
import br.com.lestcode.moviebattle.model.MatchStatus;
import br.com.lestcode.moviebattle.model.Round;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class MatchServiceTest {

	@Value("${realm-name}")
	private String realmName;
	
	@Value("${client-secret}")
	private String clientSecret;
	
	@Test
	public void userNotAuthenticatedTest() {
		
		Response response =RestAssured
		.given()
			.contentType("application/json")
		.when()
			.post("http://localhost:8083/movie-battle/matches")
		.then()
			.log().all()
			.extract()
			.response();
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(401);
	}
	
	@Test
	public void validateMatchInProgress() {
		
		String token = getToken();
		startMatch(token);
		Response response = startMatch(token);
		
		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		
		endMatch(token);
	}
	
	@Test
	public void validateGetCurrentRound() {
		
		String token = getToken();
		
		Response response1 = getCurrentRound(token);
		Assertions.assertThat(response1.getStatusCode()).isEqualTo(400);
		//Assertions.assertThat(response1.getBody().jsonPath().getString("message")).isEqualTo(NoMatchInProgressException.MESSAGE);
		
		startMatch(token);
		Response response2 = getCurrentRound(token);
		
		Round round = response2.getBody().as(Round.class);
		
		Assertions.assertThat(round.getMovieA().getImdbId()).isNotNull();
		Assertions.assertThat(round.getMovieB().getImdbId()).isNotNull();
		
		endMatch(token);
	}
	
	@Test
	public void validateUserGuess() {
		String token = getToken();
		
		startMatch(token);
		
		Response invalidResponse = sendGuess("C", token);
		Assertions.assertThat(invalidResponse.getStatusCode()).isEqualTo(400);
		//Assertions.assertThat(response1.getBody().jsonPath().getString("message")).isEqualTo(InvalidGuessException.MESSAGE);
		
		Response response = sendGuess("A", token);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		
		Response endMatch = endMatch(token);
		Assertions.assertThat(endMatch.getStatusCode()).isEqualTo(200);
	}
	
	@Test
	public void fullMatchTest() {
	
		String token = getToken();
		startMatch(token);
		
		MatchStatusDto dto;
		
		do {
			getCurrentRound(token);
			Response r = sendGuess("A", token);
			dto = r.getBody().as(MatchStatusDto.class);
			
		} while (dto.getStatus().equals(MatchStatus.RUNNING));
		
		assertEquals(dto.getStatus(), MatchStatus.FINISHED);
		
	}
	
	@Test
	public void listRankingTest() {
		
		Response rankingReponse = listRanking(getToken());
		List<RankinkDto> rankingBefore = rankingReponse.getBody().as(new TypeRef<List<RankinkDto>>() {});
		
		startMatch(getToken());
		sendGuess("A", getToken());
		sendGuess("A", getToken());
		sendGuess("A", getToken());
		endMatch(getToken());
		
		startMatch(getToken());
		sendGuess("A", getToken());
		sendGuess("A", getToken());
		sendGuess("A", getToken());
		endMatch(getToken());
		
		startMatch(getToken());
		sendGuess("A", getToken());
		sendGuess("A", getToken());
		sendGuess("A", getToken());
		endMatch(getToken());
		
		rankingReponse = listRanking(getToken());
		List<RankinkDto> ranking = rankingReponse.getBody().as(new TypeRef<List<RankinkDto>>() {});
		assertEquals(ranking.size(), 3 + rankingBefore.size());
		
	}
	
	private String getToken() {

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("client_id", "movie-battle")
                .formParam("username","testuser")
                .formParam("password", "123456")
                .formParam("grant_type", "password")
                .formParam("client_secret", clientSecret)
              .post("http://localhost:8080/realms/"+realmName+"/protocol/openid-connect/token")
              .then()
                .statusCode(200)
                .extract()
                .response();
        
        return response.getBody().jsonPath().getString("access_token");
    }
	
	private Response startMatch(String token) {
		return RestAssured
		.given()
			.auth().preemptive().oauth2(token)
			.contentType("application/json")
		.when()
			.post("http://localhost:8083/movie-battle/matches")
		.then()
			.log().all()
			.extract()
			.response();
	}
	
	private Response endMatch(String token) {
		return RestAssured
				.given()
					.auth().preemptive().oauth2(token)
					.contentType("application/json")
				.when()
					.delete("http://localhost:8083/movie-battle/matches")
				.then()
					.log().all()
					.extract()
					.response();
	}
	
	private Response getCurrentRound(String token) {
		return RestAssured
		.given()
			.auth().preemptive().oauth2(token)
			.contentType("application/json")
		.when()
			.get("http://localhost:8083/movie-battle/matches/rounds/current")
		.then()
			.log().all()
			.extract()
			.response();
	}
	
	private Response sendGuess(String guess, String token) {
		UserGuessDto dto = new UserGuessDto();
		dto.setGuess(guess);
		
		return RestAssured
		.given()
			.auth().preemptive().oauth2(token)
			.contentType("application/json")
		.when()
			.with().body(dto)
			.post("http://localhost:8083/movie-battle/matches/rounds/current/guess")
		.then()
			.log().all()
			.extract()
			.response();
	}
	
	private Response listRanking(String token) {
		return RestAssured
		.given()
			.auth().preemptive().oauth2(token)
			.contentType("application/json")
		.when()
			.get("http://localhost:8083/movie-battle/matches/ranking")
		.then()
			.log().all()
			.extract()
			.response();
	}
	
}
