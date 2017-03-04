package statistic_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import io.PubFunc;
import search.Palindrome;

public class PalSearch{
		
	public static void main(String[] args) throws FileNotFoundException{
		String palOutDir = args[0];
		String upstreamOutDir =args[1];
		String distr = args[2];
		String palOut;
		String query;
		String matchedUpstream; 
	   
		for(String file:new File(palOutDir).list()){
			palOut = palOutDir + PubFunc.os + file;
			query = file.substring(0, file.indexOf("_"));
			matchedUpstream = upstreamOutDir + PubFunc.os + file;
			List<Palindrome> pls = Palindrome.getPalindromesFromFile(palOut, matchedUpstream);
		    
			Palindrome queryPal = Palindrome.getCertainPal(query, pls); 
			pls.remove(queryPal);
			statistic_test.SeqSearch.seqSearch(pls,query,queryPal, matchedUpstream, distr);
		}
			
		
	}
}	