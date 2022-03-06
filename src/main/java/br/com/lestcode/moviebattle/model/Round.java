package br.com.lestcode.moviebattle.model;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.lestcode.moviebattle.exception.InvalidGuessException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Round implements Comparable<Round>{

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "movie_a_id")
	private Movie movieA;
	
	@ManyToOne
	@JoinColumn(name = "movie_b_id")
	private Movie movieB;
	
	public Round() {
	}
	
	public Round(String movieAId, String movieBId) {
		this.movieA = new Movie(movieAId);
		this.movieB = new Movie(movieBId);
	}
	
	public Round(Movie movieA, Movie movieB) {
		this.movieA = movieA;
		this.movieB = movieB;
	}

	public Boolean checkUserGuessIsCorrect(String guess) {
		
		if(guess == null || (!guess.equals("A") && !guess.equals("B")) ) {
			throw new InvalidGuessException();
		}
		
		if(guess.equals("A") && movieA.getFinalScore() > movieB.getFinalScore()) {
			return true;
		}
		
		if(guess.equals("B") && movieB.getFinalScore() > movieA.getFinalScore()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int compareTo(Round other) {
		return Comparator.comparing(Round::getMovieA)
				.thenComparing(Round::getMovieB)
				.compare(this, other);
	}

	@Override
	public String toString() {
		return "(" + movieA.getImdbId() + "," + movieB.getImdbId() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((movieA == null) ? 0 : movieA.hashCode());
		result = prime * result + ((movieB == null) ? 0 : movieB.hashCode());
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
		Round other = (Round) obj;
		if (movieA == null) {
			if (other.movieA != null)
				return false;
		} else if (!movieA.equals(other.movieA))
			return false;
		if (movieB == null) {
			if (other.movieB != null)
				return false;
		} else if (!movieB.equals(other.movieB))
			return false;
		return true;
	}

	
}
