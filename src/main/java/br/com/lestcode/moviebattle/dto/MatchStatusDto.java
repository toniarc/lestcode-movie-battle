package br.com.lestcode.moviebattle.dto;

import br.com.lestcode.moviebattle.model.MatchStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchStatusDto {

	private String message;
	private MatchStatus status;
}
