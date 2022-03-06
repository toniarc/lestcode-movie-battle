package br.com.lestcode.moviebattle.test.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.lestcode.moviebattle.model.Movie;
import br.com.lestcode.moviebattle.service.WebScrapingService;

@SpringBootTest
@ActiveProfiles("test")
public class WebScrapingTest {

	@Autowired
	private WebScrapingService service;
	
	@Test
	public void imdbWebScrapingTest() {
		
		List<Movie> movies = service.loadMovies();
		
		Assertions.assertThat(movies).allMatch( m -> m.getImdbId() != null && m.getFinalScore() != null && m.getImdbScore() != null && m.getName() != null && m.getVotes() != null && m.getYear() != null);
	}
}
