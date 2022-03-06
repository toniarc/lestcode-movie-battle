package br.com.lestcode.moviebattle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoMatchInProgressException extends RuntimeException {

	private static final long serialVersionUID = 5614959719300918484L;
	public static final String MESSAGE = "Não existe nenhuma partida em andamento no momento.";

	public NoMatchInProgressException() {
		super(MESSAGE);
	}
}
