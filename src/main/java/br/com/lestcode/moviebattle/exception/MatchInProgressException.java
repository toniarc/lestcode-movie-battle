package br.com.lestcode.moviebattle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MatchInProgressException extends RuntimeException {

	public static final String MESSAGE = "Já existe uma partida em andamento.";
	private static final long serialVersionUID = -2038322133009792720L;

	public MatchInProgressException() {
		super(MESSAGE);
	}
}
