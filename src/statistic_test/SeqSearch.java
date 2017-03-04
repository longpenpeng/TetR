package statistic_test;

import java.io.File;
import search.*;
import java.io.FileNotFoundException;
import java.util.List;

import io.PubFunc;
import search.Palindrome;

public class SeqSearch {
public static void  seqSearch(List<Palindrome> pls,String query,Palindrome queryPal,
		   String upstream,	String distr) throws FileNotFoundException{
		
		  
		String similarPatten = new File(distr).getParent() + PubFunc.os + 
					"similarPatten";
		String maxOut = new File(distr).getParent() + PubFunc.os + "maxOut";
		String matchedResult = new File(upstream).getParent() + PubFunc.os +
				"matchedResult";
		String different = new File(distr).getParent() + PubFunc.os + 
				"different";
		
		int palSeqcutoff = 1;
		double upstreaCutoff = 0.8;
		double aroundCutoff =0.6;
		 
//		List<Palindrome> pls = Palindrome.getPalindromesFromFile(palOut, upstream);
//		 
//		Palindrome queryPal = Palindrome.getCertainPal(query, pls); 
//		pls.remove(queryPal);
		 
		search.PalSearch.search(queryPal, pls,  palSeqcutoff, upstreaCutoff, aroundCutoff,
				distr,  maxOut, similarPatten, matchedResult, different);
		
	}
	
}
