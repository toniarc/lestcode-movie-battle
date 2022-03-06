package br.com.lestcode.moviebattle.test.unit;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.lestcode.moviebattle.model.Movie;
import br.com.lestcode.moviebattle.model.Round;
import br.com.lestcode.moviebattle.repository.RoundRepository;
import br.com.lestcode.moviebattle.service.RoundService;

@ExtendWith(MockitoExtension.class)
public class RoundServiceTest {

	@Mock RoundRepository repository;
	
	@Test
	public void generateRoundsTest() {
		
		List<Movie> movies = Arrays.asList(new Movie("1"), new Movie("2"), new Movie("3"), new Movie("4")); 
		
		/** 
		 * 1-1 -> inválido, não pode repetir o mesmo filme 
		 * 1-2 -> ok 
		 * 1-3 -> ok
		 * 1-4 -> ok
		 * 
		 * 2-1 -> inválido, já existe o par 1-2, 
		 * 2-2 -> inválido, não pode repetir o mesmo filme 
		 * 2-3 -> ok 
		 * 2-4 -> ok
		 * 
		 * 3-1 -> inválido, já existe o par 1-3, 
		 * 3-2 -> inválido, já existe o par 2-3, 
		 * 3-3 -> inválido, não pode repetir o mesmo filme 
		 * 3-4 -> ok
		 * 
		 * 4-1 -> inválido, já existe o par 1-4, 
		 * 4-2 -> inválido, já existe o par 2-4, 
		 * 4-3 -> inválido, já existe o par 3-4, 
		 * 4-4 -> inválido, não pode repetir o mesmo filme
		 * 
		 * lista final deve conter: [1-2],[1-3],[1-4],[2,3],[2,4],[3-4]
		 * lista final não deve conter: [1-1],[2-1],[2-2],[3-1],[3-2],[3-3],[4-1],[4-2],[4-3],[4-4]
		 */
		
		RoundService service = new RoundService(repository);
		Set<Round> rounds = service.initRounds(movies);
		
		System.out.println(rounds);
		
		Assertions.assertThat(rounds).containsAll(Arrays.asList(new Round("1","2"), new Round("1","3"),new Round("1","4"),new Round("2","3"),new Round("2","4"),new Round("3","4")));
		Assertions.assertThat(rounds).doesNotContain(new Round("1","1"),new Round("2","1"),new Round("2","2"),new Round("3","1"),new Round("3","2"),new Round("3","3"),new Round("4","1"),new Round("4","2"),new Round("4","3"),new Round("4","4"));
	}
}
