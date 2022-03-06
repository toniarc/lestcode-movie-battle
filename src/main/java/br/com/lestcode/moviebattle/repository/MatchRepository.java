package br.com.lestcode.moviebattle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lestcode.moviebattle.model.Match;
import br.com.lestcode.moviebattle.model.MatchStatus;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer>{

	Match findByUserAndStatus(String userName, MatchStatus running);

}
