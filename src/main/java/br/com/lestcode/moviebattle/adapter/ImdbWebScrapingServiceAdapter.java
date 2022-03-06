package br.com.lestcode.moviebattle.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import br.com.lestcode.moviebattle.model.Movie;
import br.com.lestcode.moviebattle.service.WebScrapingService;
import br.com.lestcode.moviebattle.util.Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImdbWebScrapingServiceAdapter implements WebScrapingService {

	private final static String URL = "https://www.imdb.com/list/ls071457904?sort=list_order,asc&st_dt=&mode=detail&page=";
	
	@Override
	public List<Movie> loadMovies() {

		List<Movie> movies = new ArrayList<Movie>();

		try {
			Integer pageNumber = Util.getRandomPageNumber(1,10);
			
			log.info("Loading movies from {} ...", URL + pageNumber);
			
			Document document = Jsoup.connect(URL + pageNumber).get();
			Elements parent = document.getElementsByClass("lister-list");

			parent.forEach(p -> {

				p.getElementsByClass("lister-item mode-detail").forEach(detail -> {

					Movie movie = new Movie();
					movie.setImdbId(getMovieId(detail));

					detail.getElementsByClass("lister-item-content").forEach(content -> {
						movie.setName(getMovieName(content));
						movie.setVotes(getMovieVotes(content));
						movie.setYear(getMovieYear(content));
						movie.setImdbScore(getMovieScore(content));

					});

					if(movie.checkIsValid()) {
						movie.calculateFinalScore();
						movies.add(movie);						
					}

				});

			});

			log.info( movies.size() + " movies loaded!");
			
			return movies;

		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Movie>();
		}

	}
	
	private Integer getMovieVotes(Element detail) {
		Elements elements = detail.getElementsByAttributeValue("name", "nv");
		
		if(elements.size() > 0) {
			String votes = elements.get(0).attr("data-value");
			return Integer.parseInt(votes);
		} 
		
		return null;
	}

	private Float getMovieScore(Element detail) {
		Elements movieRatingElement = detail.getElementsByClass("ipl-rating-star__rating");
		
		if(movieRatingElement.size() > 0) {
			String movieRating = movieRatingElement.get(0).childNode(0).toString();
			return Float.parseFloat(movieRating);
		}
		return null;
	}

	private String getMovieYear(Element detail) {
		Elements h3 = detail.getElementsByTag("h3");
		
		if(h3.size() > 0) {
			Elements movieYear = h3.get(0).getElementsByTag("span");
			String ano = movieYear.get(1).childNode(0).toString();
			return ano.replace("(", "").replace(")", "");
		}
		
		return null;
	}

	private String getMovieName(Element detail) {
		Elements h3 = detail.getElementsByTag("h3");
		
		if(h3.size() > 0) {
			Elements movieName = h3.get(0).getElementsByTag("a");
			return movieName.get(0).childNode(0).toString();
		}
		
		return null;
	}

	private String getMovieId(Element movie) {
		Elements movieIdElement = movie.getElementsByClass("lister-item-image ribbonize");

		if (movieIdElement.size() > 0) {
			return movieIdElement.get(0).attr("data-tconst");
		}
		return null;
	}
}
