package br.com.lestcode.moviebattle.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PreviousRound {

	@EmbeddedId
	private PreviousRoundId id; 
	
	@ManyToOne
	@JoinColumn(name = "round_id", insertable = false, updatable = false)
	private Round round;
	
	@ManyToOne
	@JoinColumn(name = "match_id", insertable = false, updatable = false)
	private Match match;
	
	private Boolean guessCorrect;
}
