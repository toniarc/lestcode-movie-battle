package br.com.lestcode.moviebattle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.lestcode.moviebattle.model.Movie;
import br.com.lestcode.moviebattle.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

	private final MovieRepository repository;
	
	public void save(List<Movie> movies) {
		repository.saveAll(movies);
	}
	
}
