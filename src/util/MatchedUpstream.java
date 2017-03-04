package util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import io.PubFunc;
import io.WriteStreamAppend; 
 
public class MatchedUpstream {
	
	public static void main(String[] args) throws FileNotFoundException { 
		String query = args[0];
		String alignOut = args[1];
		String wholeUpstream = args[2];
		int brStart = Integer.parseInt(args[3]);
		int  brEnd = Integer.parseInt(args[4]);
		int cutoff = Integer.parseInt(args[5]);	
		String identFile = args[6];
		String distr = args[7];
		
		WriteStreamAppend.appendBufferWriter(distr, ">>>"+query + 
				PubFunc.lineSeparator);
		identDistr(identFile, distr);
		
		int similarAccSize = search.UpStream.getMatchUpstream(query, alignOut, wholeUpstream,
				brStart, brEnd, cutoff, identFile, distr);
		/*
		 * if the size of similar accs is too small , here, it is necessary to widen
		 *  the cutoff
		 */
		
		 
		
	}
	public static void identDistr(String ident, String distr) {
		List<String> list =  PubFunc.getListFromFile(ident, 7, " +");
		List<Double> newList = new ArrayList<Double>();
		double d;
		for(String str : list) {
			d = Double.parseDouble(str);
			newList.add(d);
		}
		statistic.Distribute.distr(10,newList, distr, "ident distribute");
	}

}
