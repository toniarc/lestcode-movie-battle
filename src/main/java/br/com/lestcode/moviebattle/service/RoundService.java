package br.com.lestcode.moviebattle.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import br.com.lestcode.moviebattle.model.Movie;
import br.com.lestcode.moviebattle.model.Round;
import br.com.lestcode.moviebattle.repository.RoundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoundService {
 
	private final RoundRepository repository;
	
	public Set<Round> initRounds(List<Movie> movies) {
		Set<Round> rounds = generateRounds(movies);
		repository.saveAll(rounds);
		return rounds;
	}
	
	private Set<Round> generateRounds(List<Movie> movies){
		
		log.info("Generating rounds ...");
		
		Set<Round> rounds = new TreeSet<Round>();
		
		for(Movie movie1 : movies) {
			for(Movie movie2 : movies) {
				
				if(!movie1.equals(movie2)) {
					Round r1 = new Round(movie1, movie2);
					Round r2 = new Round(movie2, movie1);
					
					boolean containsR1 = rounds.contains(r1);
					boolean containsR2 = rounds.contains(r2);
					
					if(!containsR1 && !containsR2) {
						rounds.add(r1);
					}
					
				}
			}
		}
		
		log.info(rounds.size() + " rounds generated!!");
		
		return rounds;
	}
}
