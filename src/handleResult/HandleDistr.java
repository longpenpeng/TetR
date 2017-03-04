package handleResult;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.Arith;
import io.Module;
import io.PubFunc;

/**
 * this class contains some function to handle the result in "distr" file 
 * which contains some useful distribution
 * 
 * @author longpengpeng
 *
 */
public class HandleDistr {
	public static void splitDistr(String distrPath, Map<Integer, Integer> hasIdentMap,
			Map<Integer, Integer> hitMap, Map<Integer, Integer>hasDissimilarMap,
			List<String> hasResultAccList, List<String> noResultAccList,
			Map<Integer, Integer>  noDissimilarMap, Map<Integer, Integer>
			noIdentMap
			){
		
		
		
		int counter = 0 ;
		List<Module> modules = Module.getModules(distrPath, ">>>");
		Iterator<Module> iter = modules.iterator();
		String acc;
		String accLine;
		String identLine ;
		String dissimilarLine;
		String hitLine;
		while(iter.hasNext()){
			
			Module m = iter.next();
			if(m.getNum() != 7){
				continue;
			}
			accLine =m.readLine(0);
			acc = accLine.replace(">>>", "");
			identLine =m.readLine(2); 
			dissimilarLine = m.readLine(4);
			hitLine = m.readLine(6);
			if(noResultAccList.contains(acc)){
				splitIdent(identLine, noIdentMap);
				splitDissimia(dissimilarLine, noDissimilarMap);
			}else {
				splitIdent(identLine, hasIdentMap);
				splitDissimia(dissimilarLine, hasDissimilarMap);
			} 
		
			splitHitDistr(hitLine, hitMap); 
			counter ++;
		}
		System.out.println(counter);
	}
	 public static void splitDissimia(String line,
			 Map<Integer, Integer>dissimilarMap) {
		 String[] spt = line.split("\\|");
		 int bins;
		 int freq ;
		 int prevFreq;
		 if(spt.length != 10){
			 throw new IllegalArgumentException("the bins of dissimilar distribution "
			 		+ "should be 10!");
		 }else {
			 for(int i = 0; i<spt.length; i++) {
				 int index = spt[i].indexOf(":");
				 bins = Integer.parseInt(spt[i].substring(0,index));
//				 System.out.println(bins + " " + index);
				 freq = Integer.parseInt(spt[i].substring(index+1, 
						                               spt[i].length()));
				 
				 prevFreq = dissimilarMap.get(bins);
				 dissimilarMap.put(bins,prevFreq + freq );
//				 System.out.println(bins +" " + (prevFreq + freq) + " " + spt.length);
			 }
		} 
	 }
	 
	 public static void  splitHitDistr(String line, Map<Integer, Integer> hitMap){
		 String[] spt = line.split("\\|");
		 int bins;
		 int freq ;
		 int prevFreq;
		 for(int i = 0; i<spt.length; i++) {
			 int index = spt[i].indexOf(":");
			 bins = Integer.parseInt(spt[i].substring(0,index));
			 freq = Integer.parseInt(spt[i].substring(index+1, 
					                               spt[i].length()));
			 if(hitMap.containsKey(bins)){
				 prevFreq = hitMap.get(bins);
				 hitMap.put(bins,prevFreq + freq );
			 }else {
				 hitMap.put(bins, freq );
			} 
		 } 
	 }
	 public static void splitIdent(String line, Map<Integer, Integer> identMap){
		 String[] spt = line.split("\\|");
		 int bins;
		 int freq ;
		 int prevFreq;
		 if(spt.length != 10){
			 throw new IllegalArgumentException("the bins of dissimilar distribution "
			 		+ "should be 10!");
		 }else {
			 for(int i = 0; i<spt.length; i++) {
				 int index = spt[i].indexOf(":");
				 bins = Integer.parseInt(spt[i].substring(0,index));
				 freq = Integer.parseInt(spt[i].substring(index+1, 
						                               spt[i].length()));
				 prevFreq = identMap.get(bins);
				 identMap.put(bins,prevFreq + freq );
			 }
		} 
	 }
	 
	 public static void main(String[] args){
		 String distrPath ="/home/longpengpeng/refine_TETR/merge/merge/distr";
		 String hasResult = "/home/longpengpeng/refine_TETR/merge/merge/statistic"
		 		+ "/hasResult";
		 String noResult = "/home/longpengpeng/refine_TETR/merge/merge/statistic"
			 		+ "/noResult";
		 Map<Integer, Integer> noIdentMap = new TreeMap<Integer, Integer>();
		 Map<Integer, Integer> hasIdentMap = new TreeMap<Integer, Integer>();
		 for(int i =1; i < 11; i++){
			noIdentMap.put(i*10, 0);
			hasIdentMap.put(i*10, 0);
		 }
		 Map<Integer, Integer> noDissimilarMap = new TreeMap<Integer,Integer>();
		 Map<Integer, Integer> hasDissimilarMap = new TreeMap<Integer,Integer>();
		 for(int i =1; i<11; i++){
			noDissimilarMap.put(i, 0);
			hasDissimilarMap.put(i, 0);
		 }
		 Map<Integer, Integer> hitMap = new HashMap<Integer, Integer>();
		 List<String> hasResultAcc = PubFunc.getListFromFile(hasResult, 1, " +");
		 List<String> noResultAcc = PubFunc.getListFromFile(noResult, 1, " +");
		 
		 splitDistr(distrPath, hasIdentMap, hitMap, hasDissimilarMap, 
				 hasResultAcc, noResultAcc, noDissimilarMap, noIdentMap);
		 String outDir ="/home/longpengpeng/refine_TETR/merge/merge/statistic";
		 int noAccNum = noResultAcc.size();
		 int hasAccNum = hasResultAcc.size();
		 int freq;
		 double mean;
		 try {
			PrintWriter hasIdentWriter = new PrintWriter(outDir + PubFunc.os + 
					"hasResultIdent");
			PrintWriter noIdentWriter = new PrintWriter(outDir + PubFunc.os + 
					"noResultIdent");
			PrintWriter hasDissimiWriter = new PrintWriter(outDir + PubFunc.os +
					"hasResultDissimi");
			PrintWriter noDissimiWriter = new PrintWriter(outDir + PubFunc.os +
					"noResultDissimilar");
			PrintWriter hitWriter = new PrintWriter(outDir + PubFunc.os + "hit");
			for(int bins: hasIdentMap.keySet()){
				freq = hasIdentMap.get(bins);
				mean = Arith.div(freq,hasAccNum, 2);
				hasIdentWriter.write(bins + "  " + freq + "  " + mean + PubFunc.lineSeparator);
				hasIdentWriter.flush();
			}
			for(int bins:noIdentMap.keySet()){
				freq = noIdentMap.get(bins);
				//mean = freq/noAccNum;
				mean = Arith.div(freq,noAccNum, 2);
				noIdentWriter.write(bins + "  " + freq + "  " +mean + PubFunc.lineSeparator);
				noIdentWriter.flush();
			}
			for(int bins:hasDissimilarMap.keySet()){
				freq = hasDissimilarMap.get(bins);
				//mean = freq/hasAccNum;
				mean = Arith.div(freq,hasAccNum, 2);
				hasDissimiWriter.write(bins + "  " +freq + "  " +mean + PubFunc.lineSeparator);
				hasDissimiWriter.flush();
			}
			for(int bins:noDissimilarMap.keySet()){
				freq = noDissimilarMap.get(bins);
				//mean = freq/noAccNum;
				mean = Arith.div(freq,noAccNum, 2);
				noDissimiWriter.write(bins + "  " +freq + "  " +mean + PubFunc.lineSeparator);
				noDissimiWriter.flush();
			}
			for(int bins:hitMap.keySet()){
				freq = hitMap.get(bins);
				hitWriter.write(bins +"  " + freq + PubFunc.lineSeparator);
				hitWriter.flush();
			}
			hasIdentWriter.close();
			noIdentWriter.close();
			hasDissimiWriter.close();
			noDissimiWriter.close();
			hitWriter.close();
					 
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		 
		 
		 
		 
		 System.out.println(hasIdentMap + "  " + noResultAcc.size() + "  " + hasResultAcc.size());
		 System.out.println(noIdentMap);
		 System.out.println(hasDissimilarMap + "  " + noDissimilarMap);
		 System.out.println(hitMap);
			
		 
	 }
	 
}
