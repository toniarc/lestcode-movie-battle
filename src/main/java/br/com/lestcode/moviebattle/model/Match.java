package br.com.lestcode.moviebattle.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Match {

	@Id
	@GeneratedValue
	private Integer id;

	private Float score;
	private String user;
	
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
	private List<PreviousRound> previousRounds;
	
	@ManyToOne
	@JoinColumn(name = "round_id")
	private Round currentRound;
	
	private MatchStatus status;
	
	private int totalRemainingAttempts;
	
	public Match() {
	}
	
	public static Match startMatch(String user, Round currentRound) {
		Match match = new Match();
		match.setUser(user);
		match.setCurrentRound(currentRound);
		match.setStatus(MatchStatus.RUNNING);
		match.setCurrentRound(currentRound);
		match.setPreviousRounds(new ArrayList<PreviousRound>());
		match.setScore(0F);
		match.setTotalRemainingAttempts(3);
		return match;
	}
	
	public void endMatch() {
		this.status = MatchStatus.FINISHED;
		Integer totalQuizzes = previousRounds.size();
		Long totalOfCorrectAnswers = previousRounds.stream().filter( r -> r.getGuessCorrect() ).count();
		
		if(totalQuizzes > 0) {
			Float percentageOfHits = new BigDecimal(((float) totalOfCorrectAnswers) / totalQuizzes * 100).setScale(2, RoundingMode.HALF_UP).floatValue();
			this.score = new BigDecimal(((float)totalQuizzes) * percentageOfHits).setScale(2, RoundingMode.HALF_UP).floatValue();
		} else {
			this.score = 0F;
		}
	}
	
	public int decreaseRemainAttempts() {
		return --this.totalRemainingAttempts; 
	}
	
	public void moveCurrentRoundToPreviousRound(Boolean isUserGuestCorrect) {
		PreviousRound previousRound = new PreviousRound();
		PreviousRoundId id = new PreviousRoundId(this.getId(), currentRound.getId());
		previousRound.setId(id);
		previousRound.setGuessCorrect(isUserGuestCorrect);
		this.getPreviousRounds().add(previousRound);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean isUserGuestCorrect(String guess) {
		return this.getCurrentRound().checkUserGuessIsCorrect(guess);
	}

}
