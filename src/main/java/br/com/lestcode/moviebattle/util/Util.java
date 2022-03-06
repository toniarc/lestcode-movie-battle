package br.com.lestcode.moviebattle.util;

import java.util.Random;

public class Util {

	public static Integer getRandomPageNumber(int startRange, int endRange) {
		Random r = new Random();
		return r.nextInt(endRange - startRange) + startRange;
	}
}
