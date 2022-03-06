package br.com.lestcode.moviebattle.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class PreviousRoundId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "round_id")
	private Integer roundId;
	
	@Column(name = "match_id")
	private Integer matchId;

	public PreviousRoundId() {
	}
	
	public PreviousRoundId(Integer matchId, Integer roundId) {
		this.matchId = matchId;
		this.roundId = roundId;
	}
	
}

