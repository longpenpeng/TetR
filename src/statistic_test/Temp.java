package statistic_test;

import java.util.List;

import search.Palindrome;

public class Temp {
	public static void main(String[] args){
		String path = args[0];
		String upstream = args[1];
		List<Palindrome> pls = Palindrome.getPalindromesFromFile(path, upstream);
		System.out.print(pls.size());
	}
}
