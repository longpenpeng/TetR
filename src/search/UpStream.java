package search;

import java.util.List;


import java.util.Map;
import java.util.Set;
import java.util.TreeSet; 
import genome.MyFasta; 
import io.PubFunc;
import io.SortClass;
import runOutProgram.AlignResult; 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Iterator;
 


/**
 * this {@code Upstream} class contains some method to handle the homology
 *  protein sequence and query
 *  <p>
 *  the method compareBindingRegion: calculate the similar  binding region whose dissimilar amino acide in binding region is less than 
 * 	cutoff  
 * 	the method getMatchUpstream :get upstream nucleotides of 
 * 	getQuery: get {@code AlignResult} instance of query
 * 	compare : compare the region and count the dissimilar amino acid number. Also,in 
 * this method, some files create,such as "similarAcc", "similarDBDSeq" ,"dissimilarAcc"
 * 
 */
public class UpStream { 
	
	/**
	 * get the upstream nucleotides of similar binding region where dissimilar
	 *  amino acid number  is less than cutoff,
	 * @param query
	 * @param alignOut
	 * @param wholeUpstream
	 * @param brStart
	 * @param brEnd
	 * @param cutoff
	 * @throws FileNotFoundException
	 */
	public static int getMatchUpstream(String query,String alignOut, 
			String wholeUpstream, int brStart, 	int brEnd, int cutoff,
				String identFile, String distr)throws FileNotFoundException { 
		String outDir = new File(alignOut).getParent();
		Map<String, Integer> acc2Dismatch = new HashMap<String, Integer>();
		List<String> similarAccs = compareBindingRegion(alignOut, brStart,
				brEnd, query, cutoff, acc2Dismatch, distr);
		/*
		 * create matched upstream file and write the matched nucleotide sequn-
		 * ence to this file
		 */
		getUpstream(wholeUpstream, similarAccs, outDir, acc2Dismatch, identFile); 
		return similarAccs.size();
	} 
	  
	/**
	 * 	extract multiply sequence align result of the binding domains and write
	 *  into "extractMSA"
	 *  compare the binding region between the homology proteins' binding domain 
	 *  with query's binding region, finding out the similar binding region
	 * @param alignOut
	 * @param brStart
	 * @param brEnd
	 * @param query
	 * @param cutoff
	 * @return similar acc list
	 * @throws FileNotFoundException 
	 */
	public static  List<String> compareBindingRegion(String alignOut, int brStart,
			int brEnd, String query ,int cutoff, Map<String, Integer>acc2Dismatch
			,String distr) throws FileNotFoundException{
		String outDir = new File(alignOut).getParent() + File.separator;
		String extractMSA = outDir + "extractMSA";
		List<AlignResult> ars = AlignResult.write2File(alignOut, 5, extractMSA); 
		AlignResult queryAR = getQuery(query, ars);  
		 
		/*create similar accession file ,dissmilar accession file, similar 
		 * DBDseq file 
		 */
		List<String> similarAcc  = compare(ars, queryAR, brStart, 
				brEnd, outDir, cutoff, acc2Dismatch, distr);
 
		return similarAcc;
		
	}
	/**
	 * get the upstream nucleotide from the pre-extract "wholeUpstream" file,
	 * and extract the matched upstream nucleotide 
	 * @param wholeUpstream : 
	 * @param accs
	 * @return List of {@code MyFasta}
	 */
	public static List<MyFasta> getUpstream(String wholeUpstream, List<String> accs,
			String outDir, Map<String, Integer>acc2Dismatch, String identFile) {
		MyFasta myFasta;
		String acc;
		String matchedUpstream = outDir + PubFunc.os + "matchedUpstream";
		List<MyFasta> subjectFasta = new ArrayList<MyFasta>();
		Map<String , MyFasta> acc2MyFasta = new HashMap<String, MyFasta>();
		//PrintUtils.printCurTime("time");
		try {
			List<MyFasta> upstreamFasta = MyFasta.getFastasFromFile(wholeUpstream,">");
			Iterator<MyFasta> iter = upstreamFasta.iterator();
			while(iter.hasNext()){
				myFasta = iter.next();
				acc = myFasta.getAcc(); 
				acc2MyFasta.put(acc, myFasta);
			}
			//PrintUtils.printCurTime("last time");
			int dissimilar ;
			PrintWriter writer = new PrintWriter(matchedUpstream);
			Map<String,String > acc2Ident = PubFunc.getMapFromFile(identFile, 1,
					7, " +");
			
			for(String accession:accs) {
				//System.out.println(accession); 
				if(acc2MyFasta.containsKey(accession)){
					dissimilar = acc2Dismatch.get(accession);
					myFasta = acc2MyFasta.get(accession);
					myFasta.setDissimilar(dissimilar);
					myFasta.setIdent(acc2Ident.get(accession));
					subjectFasta.add(myFasta);
					writer.write(myFasta.toUpstreamFile() + PubFunc.lineSeparator);
					writer.flush();
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return subjectFasta;
		
	}
	 /**
	  * compare the binding region ,and return the mismatch amino acids number
	  * @param str1
	  * @param str2
	  * @return mismatch counter
	 * @throws FileNotFoundException 
	  */
	public static List<String> compare(List<AlignResult> alignResults, AlignResult queryAR, 
			int brStart, int brEnd, String outDir, int cutoff, Map<String, Integer> 
					acc2Dismatch, String distr) throws FileNotFoundException {
		
		
		String str1 = queryAR.getSeq();//.substring(brStart, brEnd);
		Iterator<AlignResult> it = alignResults.iterator();
		
		String similarDBDSeq = outDir + PubFunc.os + "similarDBDSeq";
		String similarAcc = outDir + PubFunc.os + "similarAcc";
		String disSimilarAcc = outDir + PubFunc.os + "dissimilarAcc";
		PrintWriter writer = new PrintWriter(similarDBDSeq); 
		PrintWriter simialrAccWriter = new PrintWriter(similarAcc);
		PrintWriter dissmilAccWriter = new PrintWriter(disSimilarAcc);
		Set<SortClass<AlignResult>> set = new TreeSet<SortClass<AlignResult>>();
		List<Integer> counterList = new ArrayList<Integer>();
		String str2;
		AlignResult ar;
		int counter;
		String acc;
		while(it.hasNext()){
			ar = it.next(); 
			acc = ar.getAcc();
			str2 = ar.getSeq();//.substring(brStart, brEnd);
			if(str1.length() != str2.length()) {
				try {
					throw new Exception("the binding region should be same length"
							+ " when compare the binding region :" +ar.getAcc() 
							+ "DBD length :" + str1.length() + "; query DBD length :" +
							queryAR);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			counter = 0; 
		 
			for(int i = brStart; i< brEnd; i++) {
				if(str1.charAt(i) != str2.charAt(i)) {
					counter ++;  
				}  
			}
			if(counter <= cutoff) {
				set.add(new SortClass<AlignResult>(ar, counter));
				acc2Dismatch.put(acc, counter);
				counterList.add(counter);
				simialrAccWriter.write(acc + PubFunc.lineSeparator);
				simialrAccWriter.flush();
			}else {
				dissmilAccWriter.write(acc + PubFunc.lineSeparator);
				dissmilAccWriter.flush();
			}  
		}
		statistic.Distribute.distr(1, counterList, distr, "dissimilar distrubition");
		simialrAccWriter.close();
		dissmilAccWriter.close();
		
		Iterator<SortClass<AlignResult>> iter = set.iterator(); 
		List<String> similarAccs = new ArrayList<String>();
		while(iter.hasNext()) {
			
			ar  = iter.next().getObj();
			similarAccs.add(ar.getAcc());
			writer.write(ar.toString() + PubFunc.lineSeparator);
			str2 = ar.getSeq();//.substring(brStart, brEnd);
			for(int i = brStart; i< brEnd; i++) {
				if(str1.charAt(i) != str2.charAt(i)) { 
					writer.write(str1.charAt(i) + ""+ i + "" + str2.charAt(i) + " ");
				}  
			}
			writer.write(PubFunc.lineSeparator + PubFunc.lineSeparator);
			writer.flush(); 
		}
		writer.close();  
		return similarAccs;
	}
	public static AlignResult getQuery(String query, List<AlignResult> ars){
		Iterator<AlignResult> it = ars.iterator();
		AlignResult queryAR = new AlignResult();
		AlignResult ar;
		while(it.hasNext()) {
			ar = it.next();
			//System.out.println(ar.getAcc() + " " + query);
			if(ar.getAcc().equals(query)) {
				queryAR = ar;
				break;
			}
		} 
		return queryAR; 
	}
	 
}
