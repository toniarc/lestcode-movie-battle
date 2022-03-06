package br.com.lestcode.moviebattle.bootstrap;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.lestcode.moviebattle.model.Movie;
import br.com.lestcode.moviebattle.service.MovieService;
import br.com.lestcode.moviebattle.service.RoundService;
import br.com.lestcode.moviebattle.service.WebScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("default")
public class InitApplication {

	private final MovieService movieService;
	private final WebScrapingService scrapingService;
	private final RoundService roundService;
	
	@PostConstruct
    public void init() {
		
		log.info("Initializing application...");
		
		List<Movie> movies = scrapingService.loadMovies();
		movieService.save(movies);
		roundService.initRounds(movies);
		
		log.info("Application initialized!!");
    }
}
