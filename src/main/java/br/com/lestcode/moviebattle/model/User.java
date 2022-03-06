package br.com.lestcode.moviebattle.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

	private String userName;
	
	public User() {
	}

	public User(String userName) {
		super();
		this.userName = userName;
	}
	
}
