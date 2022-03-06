package br.com.lestcode.moviebattle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidGuessException extends RuntimeException {

	private static final long serialVersionUID = -8827735521652624230L;
	public static final String MESSAGE = "Formato do palpite inválido. Informe somente os valores 'A' ou 'B'. Ex: 'guess': 'A' ";

	public InvalidGuessException() {
		super(MESSAGE);
	}
}
