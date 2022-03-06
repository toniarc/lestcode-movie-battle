package br.com.lestcode.moviebattle.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.lestcode.moviebattle.dto.MatchStatusDto;
import br.com.lestcode.moviebattle.dto.RankinkDto;
import br.com.lestcode.moviebattle.dto.RoundDto;
import br.com.lestcode.moviebattle.dto.UserGuessDto;
import br.com.lestcode.moviebattle.exception.MatchInProgressException;
import br.com.lestcode.moviebattle.exception.NoMatchInProgressException;
import br.com.lestcode.moviebattle.mapper.RoundMapper;
import br.com.lestcode.moviebattle.model.Match;
import br.com.lestcode.moviebattle.model.MatchStatus;
import br.com.lestcode.moviebattle.model.Round;
import br.com.lestcode.moviebattle.model.User;
import br.com.lestcode.moviebattle.repository.MatchRepository;
import br.com.lestcode.moviebattle.repository.RoundRepository;
import br.com.lestcode.moviebattle.util.Util;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MatchService {

	private static final String CORRECT_GUESS_MESSAGE = ":) Você acertou o palpite!! Vá para a próxima rodada.";
	private static final String INCORRECT_GUESS_MESSAGE = ":( Você errou o palpite mas você ainda tem %d chance(s). Vá para a próxima rodada.";
	private static final String GAME_OVER_MESSAGE = "Game Over!! A sua pontuação final foi de %2.02f pontos";
	
	private final RoundRepository roundRepository;
	private final MatchRepository matchRepository;
	private final User user;
	
	public RoundDto startMatch() {
		
		Match currentMatch = matchRepository.findByUserAndStatus(user.getUserName(), MatchStatus.RUNNING);
		
		if(currentMatch != null) {
			throw new MatchInProgressException();
		}
		
		Match match = Match.startMatch(user.getUserName(), getFirstRound());
		
		matchRepository.save(match);
		
		return RoundMapper.INSTANCE.map(match.getCurrentRound());
	}

	private Match getCurrentMatch(String userName) {
		Match currentMatch = matchRepository.findByUserAndStatus(userName, MatchStatus.RUNNING);
		
		if(currentMatch == null) {
			throw new NoMatchInProgressException();
		}
		
		return currentMatch;
	}
	
	public Round getFirstRound() {
		Integer roundId = Util.getRandomPageNumber(1,1000);
		return roundRepository.findById(roundId).orElseThrow();
	}
	
	public Round getNextRound(Match match) {
		
		Integer roundId = -1;
		Boolean noneMatch;
		
		do {
			Integer randomId = Util.getRandomPageNumber(1,1000);
			noneMatch = match.getPreviousRounds().stream().noneMatch( pr -> pr.getId().getRoundId().equals(randomId) );
			
			if(noneMatch) {
				roundId = randomId;
			}
			
		} while (!noneMatch);
		
		return roundRepository.findById(roundId).orElseThrow();
	}
	
	public MatchStatusDto endMatch() {
		
		Match currentMatch = getCurrentMatch(user.getUserName());
		
		currentMatch.endMatch();
		
		matchRepository.save(currentMatch);
		
		MatchStatusDto matchStatus = new MatchStatusDto();
		matchStatus.setMessage(String.format(GAME_OVER_MESSAGE, currentMatch.getScore()));
		matchStatus.setStatus(currentMatch.getStatus());
		
		return matchStatus;
	}

	public RoundDto getCurrentRound() {
		Match currentMatch = getCurrentMatch(user.getUserName());
		return RoundMapper.INSTANCE.map(currentMatch.getCurrentRound());
	}

	public MatchStatusDto checkAnwswer(UserGuessDto userGuess) {
		
		Match currentMatch = getCurrentMatch(user.getUserName());
		
		Boolean isUserGuestCorrect = currentMatch.isUserGuestCorrect(userGuess.getGuess());
		
		currentMatch.moveCurrentRoundToPreviousRound(isUserGuestCorrect);
		
		MatchStatusDto matchStatus = new MatchStatusDto();
		
		if(isUserGuestCorrect) {
			
			matchStatus.setMessage(CORRECT_GUESS_MESSAGE);
			currentMatch.setCurrentRound(getNextRound(currentMatch));
			
		} else {
			
			int remainAttempts = currentMatch.decreaseRemainAttempts();
			
			if(remainAttempts > 0) {
				matchStatus.setMessage(String.format(INCORRECT_GUESS_MESSAGE, remainAttempts));
				currentMatch.setCurrentRound(getNextRound(currentMatch));
			} else {
				currentMatch.endMatch();
				matchStatus.setMessage(String.format(GAME_OVER_MESSAGE, currentMatch.getScore()));
			}
		}
		
		matchStatus.setStatus(currentMatch.getStatus());
		
		matchRepository.save(currentMatch);
		
		return matchStatus;
	}

	public List<RankinkDto> listRanking() {
		List<Match> list = matchRepository.findAll(Sort.by("score").descending());
		
		return IntStream.range(0, list.size())
        .mapToObj(i -> { 
        	Match match = list.get(i);
        	return new RankinkDto(i+1, match.getUser(), match.getScore()); 
        })
        .collect(Collectors.toList());
	}

}
