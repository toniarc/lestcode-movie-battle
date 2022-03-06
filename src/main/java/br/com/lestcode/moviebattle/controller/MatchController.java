package br.com.lestcode.moviebattle.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lestcode.moviebattle.dto.MatchStatusDto;
import br.com.lestcode.moviebattle.dto.RankinkDto;
import br.com.lestcode.moviebattle.dto.UserGuessDto;
import br.com.lestcode.moviebattle.model.Round;
import br.com.lestcode.moviebattle.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "security_auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/matches")
public class MatchController {

	private final MatchService service;
	
	@PreAuthorize("hasRole('User')") 
	@Operation(summary = "Inicia uma nova partida", security = @SecurityRequirement(name = "security_auth"))
	@PostMapping
	public Round startMatch() {
		return service.startMatch();
	}
	
	@PreAuthorize("hasRole('User')")
	@Operation(summary = "Retorna a rodada atual", security = @SecurityRequirement(name = "security_auth"))
	@GetMapping("/rounds/current")
	public Round getCurrentRound() {
		return service.getCurrentRound();
	}
	
	@PreAuthorize("hasRole('User')")
	@Operation(summary = "Avalia o palpite do usuário para a rodada atual", security = @SecurityRequirement(name = "security_auth"))
	@PostMapping("/rounds/current/guess")
	public MatchStatusDto getCurrentRound(@RequestBody UserGuessDto userGuess) {
		return service.checkAnwswer(userGuess);
	}
	
	@PreAuthorize("hasRole('User')")
	@Operation(summary = "Encerra uma partida", security = @SecurityRequirement(name = "security_auth"))
	@DeleteMapping
	public MatchStatusDto endMatch() {
		return service.endMatch();
	}
	
	@PreAuthorize("hasRole('User')")
	@Operation(summary = "Lista o ranking dos usuários", security = @SecurityRequirement(name = "security_auth"))
	@GetMapping("/ranking")
	public List<RankinkDto> getRanking() {
		return service.listRanking();
	}
}
