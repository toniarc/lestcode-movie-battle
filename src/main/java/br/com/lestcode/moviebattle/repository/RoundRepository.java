package br.com.lestcode.moviebattle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lestcode.moviebattle.model.Round;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer>{

}
