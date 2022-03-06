package br.com.lestcode.moviebattle.model;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Movie implements Comparable<Movie>{

	@Id
	@Column(name = "imdb_id")
	private String imdbId;
	
	private String name;
	private Float imdbScore;
	private String year;
	private Integer votes;
	private Float finalScore;
	
	public Movie() {
	}
	
	public Movie(String imdbId) {
		super();
		this.imdbId = imdbId;
	}
	
	public void calculateFinalScore() {
		this.finalScore = imdbScore * votes;
	}
	
	public boolean checkIsValid() {
		return votes != null && imdbScore != null && imdbId != null && year != null && name != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (imdbId == null) {
			if (other.imdbId != null)
				return false;
		} else if (!imdbId.equals(other.imdbId))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imdbId == null) ? 0 : imdbId.hashCode());
		return result;
	}

	@Override
	public int compareTo(Movie o) {
		return Comparator.comparing(Movie::getImdbId)
				.compare(this, o);
	}

}
