package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import io.PubFunc;
import search.PalSearch;
import search.Palindrome;

public class PalindAlign {
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		String palOut= args[0];
		String upstream = args[1];
		String query = args[2]; 
		String distr = args[3];
		String similarPatten = new File(distr).getParent() + PubFunc.os + 
					"similarPatten";
		String maxOut = new File(distr).getParent() + PubFunc.os + "maxOut";
		String matchedResult = new File(upstream).getParent() + PubFunc.os +
				"matchedResult";
		String different = new File(distr).getParent() + PubFunc.os + 
				"different";
		String palsSize = new File(distr).getParent() + PubFunc.os + 
				"palsSize";
		
		int palSeqcutoff = 1;
		double upstreaCutoff = 0.8;
		double aroundCutoff =0.6;
		 
		List<Palindrome> pls = Palindrome.getPalindromesFromFile(palOut, upstream);
		io.WriteStreamAppend.appendBufferWriter(palsSize,query + "  " + pls.size()+ 
				PubFunc.lineSeparator);
		Palindrome queryPal = Palindrome.getCertainPal(query, pls); 
		pls.remove(queryPal);
		
		PalSearch.search(queryPal, pls,  palSeqcutoff, upstreaCutoff, aroundCutoff,
				distr,  maxOut, similarPatten, matchedResult, different);
		
	}

}
