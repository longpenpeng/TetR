package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList; 
import java.util.List;
import java.util.TreeSet;
import java.util.Set;
import java.util.TreeMap;

import io.PubFunc;
import seqAlign.*; 
import io.SortClass;
public class PalSearch {
	
	public static void search(Palindrome queryPal, List<Palindrome> pls,
			int palSeqcutoff, double upstreaCutoff, double aroundCutoff,
			String distr, String maxOut, String similarPatten, String matchedResult,
			String different) 
					throws FileNotFoundException {
		String result_detail = new File(matchedResult).getParent() + 
				PubFunc.os+"result_info";
		String counter = new File(matchedResult).getParent() + PubFunc.os +
				"counter";
		PrintWriter resultWriter = new PrintWriter(matchedResult);
		PrintWriter detailWriter = new PrintWriter(result_detail);
		List<PalSeq> queryPalSeqs = queryPal.getPalSeqs(); 
		List<PalSeq> matchedPs;
		List<PalSeq> subjectPalSeqs; 
		List<Integer> hitList = new ArrayList<Integer>();
		String frontSeq;
		String endSeq;
		Set<SortClass<PalSeq>> sortClass = new TreeSet<SortClass<PalSeq>>();
		for(PalSeq queryPs : queryPalSeqs ) {
			//if gc content > 0.8 ,it will be ignored, and continue,
//			if(checkGC(queryPs)){
//				continue;
//			}
			matchedPs = new ArrayList<PalSeq>();
			matchedPs.add(queryPs);
			for(Palindrome subjectPl: pls){ 
				
				subjectPalSeqs = subjectPl.getPalSeqs();
				for(PalSeq subjectPs: subjectPalSeqs){
					if(matchPalSeq(queryPs, subjectPs, palSeqcutoff)) { 
						if(matchNucl(subjectPs, matchedPs, upstreaCutoff,
								aroundCutoff)){
							matchedPs.add(subjectPs);
						}
					} 
				}
			}
			if(matchedPs.size() >1){ 
				sortClass.add(new SortClass<PalSeq>(queryPs, matchedPs.size())); 
			}  
			if(matchedPs.size() >2){
				detailWriter.write("### total : " + matchedPs.size()
					+ PubFunc.lineSeparator);
				resultWriter.write("### total : " + matchedPs.size()
					+ PubFunc.lineSeparator);
				for(PalSeq ps:matchedPs){
					detailWriter.write(ps.toString() + PubFunc.lineSeparator);
					detailWriter.flush();
					frontSeq = ps.frontUpstream(10);
					endSeq = ps.endUpstream(10); 
					resultWriter.write(ps.toString() + "   " + frontSeq + " "+
							ps.wholeSeq + " " + endSeq + PubFunc.lineSeparator);
					resultWriter.flush();
				}
			}
		}  
		resultWriter.close();
		detailWriter.close();
		checkResult(sortClass, hitList, maxOut, similarPatten, counter,different);
		statistic.Distribute.hitDistr(hitList, distr, "hit distribute");
	}
	
	
	public static void checkResult(Set<SortClass<PalSeq>> set, List<Integer> hitList
			,String maxOut,String similarPath,String counter, String different){
		SortClass<PalSeq> maxSc;  
		SortClass<PalSeq> secSc;
		SortClass<PalSeq> thdSc;
		SortClass<PalSeq> sc;
		SortClass<PalSeq> forSc;
		int max = 0;
		PrintWriter writer = null;
		int differ;
		try{
			writer = new PrintWriter(counter);
		}catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		if(set.size() <2){
            io.WriteStreamAppend.appendBufferWriter(maxOut,0+"  "+ 0+  PubFunc.lineSeparator);
            writer.write(0 + "  " +0);
		}else if(set.size() ==2){
			maxSc = PubFunc.getCertainElement(set, 2); 
			hitList.add(maxSc.getIntVal());
			io.WriteStreamAppend.appendBufferWriter(maxOut,maxSc.getIntVal()
					+"  "+ maxSc.getObj().toString()+  PubFunc.lineSeparator);
			writer.write(maxSc.getIntVal() + "  " +maxSc.getObj().toString());
			
		}else if(set.size() == 3){
			maxSc = PubFunc.getCertainElement(set, set.size());
			secSc = PubFunc.getCertainElement(set, set.size()-1);
			if(matchPalSeq(maxSc.getObj(), secSc.getObj(), 2, true)){
				max = maxSc.getIntVal() + secSc.getIntVal();
				hitList.add(max);
				differ = max;
				io.WriteStreamAppend.appendBufferWriter(maxOut,max + "  "
						+ maxSc.getObj().toString() +"  **"+ PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(similarPath,">>>"+ maxSc.getIntVal()
						+"  "+ maxSc.getObj().toString() + PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(similarPath,  secSc.getIntVal()
						+ "  " + secSc.getObj().toString() + PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(different, max + "  " + 0 + 
						"  " + differ + "  "+ maxSc.getObj().getAcc()+ PubFunc.lineSeparator);
				writer.write(max + "  " +maxSc.getObj().toString() + "  **");
			}else {
				max = maxSc.getIntVal();
				differ = max - secSc.getIntVal();
				hitList.add(maxSc.getIntVal());
				hitList.add(secSc.getIntVal());
				io.WriteStreamAppend.appendBufferWriter(maxOut,max + "  "
						+ maxSc.getObj().toString()+ PubFunc.lineSeparator);
				writer.write(secSc.getIntVal() + "  " +secSc.getObj().toString() 
						+ PubFunc.lineSeparator);
				writer.write(max + " " + maxSc.getObj().toString());
				io.WriteStreamAppend.appendBufferWriter(different, maxSc.getIntVal() +
						 "  " + secSc.getIntVal() + "  " + differ + "  "
						+ maxSc.getObj().getAcc()+ PubFunc.lineSeparator);
			}
			
		}else {
			maxSc = PubFunc.getCertainElement(set, set.size());
			secSc = PubFunc.getCertainElement(set, set.size()-1);
			thdSc = PubFunc.getCertainElement(set, set.size() -2);
			forSc = PubFunc.getCertainElement(set, set.size() -3);
			if(isThreeSimilar(maxSc.getObj(), secSc.getObj(), thdSc.getObj() ,2)){
				max = maxSc.getIntVal()+secSc.getIntVal() + thdSc.getIntVal();
				differ = max - forSc.getIntVal();
				for(int i= 1; i < set.size()-2;i++){
					sc = PubFunc.getCertainElement(set,i);
					hitList.add( sc.getIntVal());
					writer.write(sc.getIntVal() + "  " + sc.getObj().toString() 
							+ PubFunc.lineSeparator);
				}
				hitList.add(max);
				io.WriteStreamAppend.appendBufferWriter(maxOut,max  +  "  "
						+ maxSc.getObj().toString()+ "  ***" +PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(similarPath, ">>>"+ maxSc.getIntVal()
						+ "  " +maxSc.getObj().toString() + PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(similarPath, secSc.getIntVal()
						+ "  " + secSc.getObj().toString() + PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(similarPath, thdSc.getIntVal()
						+ "  " +thdSc.getObj().toString() + PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(different, max 
						+"  " + forSc.getIntVal() + "  " + differ + "  "
						+ maxSc.getObj().getAcc()+ PubFunc.lineSeparator);
				 writer.write(max + "  " + maxSc.getObj().toString());
			}else if(matchPalSeq(maxSc.getObj(), secSc.getObj(), 2, true)){
				max = maxSc.getIntVal()+secSc.getIntVal();
				differ = max - thdSc.getIntVal();
				for(int i = 1; i<set.size() -1; i++){
					sc = PubFunc.getCertainElement(set,i);
					hitList.add( sc.getIntVal());
					writer.write(sc.getIntVal() + "  " + sc.getObj().toString() 
							+ PubFunc.lineSeparator); 
				}
				hitList.add(max);
				io.WriteStreamAppend.appendBufferWriter(maxOut,max + "  "
						+ maxSc.getObj().toString() + "  **"+ PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(similarPath, ">>>"+ maxSc.getIntVal()
						+"  " + maxSc.getObj().toString() + PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(similarPath, secSc.getIntVal()
						+ "  " + secSc.getObj().toString() + PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(different, max + "  "+
						thdSc.getIntVal()+ "  "+differ + "  "
						+ maxSc.getObj().getAcc()+ PubFunc.lineSeparator);
				 writer.write(max + "  " + maxSc.getObj().toString());
			}else {
				 
				max = maxSc.getIntVal();
				differ = max - secSc.getIntVal();
				for(int i = 1; i<set.size(); i++){
					sc = PubFunc.getCertainElement(set,i);
					hitList.add( sc.getIntVal());
					writer.write(sc.getIntVal() + "  " + sc.getObj().toString() 
							+ PubFunc.lineSeparator); 
				}
				hitList.add(max);
				io.WriteStreamAppend.appendBufferWriter(maxOut,max + "  "
						+ maxSc.getObj().toString()+ PubFunc.lineSeparator);
				io.WriteStreamAppend.appendBufferWriter(different, max +"  "+
						secSc.getIntVal()+"  "+differ + "  "
						+ maxSc.getObj().getAcc()+ PubFunc.lineSeparator);
				writer.write(max + "  "+ maxSc.getObj().toString());
			}
			writer.flush();
			writer.close(); 
		} 
	}
	public static boolean matchPalSeq(PalSeq queryPalSeq,PalSeq subjectPalSeq,int cutoff){
		String queryPs = queryPalSeq.getLower();
		String subjectPs = subjectPalSeq.getLower(); 
		if(Math.abs(queryPs.length()-subjectPs.length())>8){ 
			   return false;
		  }
		if(matchPalSeq(queryPs, subjectPs)){
				return true;
		}
		if(matchPalSeq(queryPs, subjectPs, cutoff)){
				return true;
		} 
		return false;
	} 
	public static boolean matchPalSeq(PalSeq queryPalSeq,PalSeq subjectPalSeq,
			int cutoff, boolean compare){
		String queryPs = queryPalSeq.getLower();
		String subjectPs = subjectPalSeq.getLower();
		if(isOverlap(queryPalSeq, subjectPalSeq)){
			return false;
		}
		if(Math.abs(queryPs.length()-subjectPs.length())>8){ 
			   return false;
		  }
		if(queryPs.length()<11 && subjectPs.length()<11){
			if(matchPalSeq(queryPs, subjectPs)){
				return true;
			}
			 
		}else {
			if(matchPalSeq(queryPs, subjectPs)){
				return true;
			}
			if(matchPalSeq(queryPs, subjectPs, cutoff)){
				return true;
			} 
		}
		
		return false;
	} 
	public static boolean matchPalSeq(String str1, String str2, int cutoff){
		if(Math.abs(str1.length()-str2.length())>8){ 
			   return false;
		  }
		int counter =0; 
		String frontStr1 = str1.substring(0,half(str1.length()));
		String endStr1 = str1.substring(half(str1.length()), str1.length());
		String frontStr2 = str2.substring(0, half(str2.length()));
		String endStr2 = str2.substring(half(str2.length()), str2.length());
		if(str1.length() == str2.length()) {
			for(int i = 0; i < str1.length(); i++){
				if(str1.charAt(i) != str2.charAt(i)){
					counter ++;
				}
			}
			if(counter > cutoff){
				return false;
			}else {
				return true;
			}
		}else if(str1.length()> str2.length()){
			for(int i =0; i< frontStr2.length(); i++){
				if(frontStr1.charAt(frontStr1.length()-1-i) != 
						frontStr2.charAt(frontStr2.length() -1 -i)){
					counter ++;
				}
			}
			for(int i = 0; i < endStr2.length(); i++){
				if(endStr1.charAt(i) != endStr2.charAt(i)){
					counter ++;
				}
			}
			if(counter > cutoff) 
				return false;
			else 
				return true;
						
		}else {
			for(int i =0; i< frontStr1.length(); i++){
				if(frontStr1.charAt(frontStr1.length()-1-i) != 
						frontStr2.charAt(frontStr2.length() -1 -i)){
					counter ++;
				}
			}
			for(int i = 0; i < endStr1.length(); i++){
				if(endStr1.charAt(i) != endStr2.charAt(i)){
					counter ++;
				}
			}
			if(counter > cutoff) 
				return false;
			else 
				return true;
		} 
	}
	public static boolean matchPalSeq(String str1, String str2) {

		String frontStr1 = str1.substring(0,half(str1.length()));
		String endStr1 = str1.substring(half(str1.length()), str1.length());
		String frontStr2 = str2.substring(0, half(str2.length()));
		String endStr2 = str2.substring(half(str2.length()), str2.length());
 
		int counter =0;
		int num =0;
		if(str1.length()>=str2.length()){
			num= str2.length();
		}else{
			num=str1.length();
		}  
		for(int i = 1; i<6; i++){
			if(frontStr1.charAt(frontStr1.length()-i) != 
					frontStr2.charAt(frontStr2.length() -i)){
				counter ++;
			}
		}
		for(int i = 0; i< 5; i++){
			if(endStr1.charAt(i) != endStr2.charAt(i)){
				counter ++;
			}
		}
		if(counter <= 1){
			return true;
		}else {
			return false;
		} 	
	}
	public static boolean matchNucl(PalSeq subjectPalSeq, 
			List<PalSeq> matchedPalSeqs, double upstreamCutoff, double aroundCutoff){
		String subUpstream = subjectPalSeq.getOriginal();
		String matchedUpstream ;
		for(PalSeq ps:matchedPalSeqs){
			matchedUpstream = ps.getOriginal();
			try {
				if(isUpstreamSimilar(subUpstream, matchedUpstream, upstreamCutoff)){
					return false;
				}
				if(isAroundSimilar(subjectPalSeq, ps, aroundCutoff)){
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return true;
	}
	public static boolean isUpstreamSimilar(String upstream1, String upstream2,
			double cutoff)throws IOException {
		NuclMatrix nMatrix = new NuclMatrix();
		double ident = seqAlign.SeqAlign.localIdentity(upstream1, 
				upstream2, nMatrix);
		if(ident > cutoff) {
			return true;
		}else {
			return false;
		}
	}
	 
	
	public static boolean isAroundSimilar(PalSeq ps1, PalSeq ps2, double maxIdent) throws IOException{
		String ps1Front = ps1.frontUpstream(10);
		String ps1End = ps1.endUpstream(10);
		String ps2Front = ps2.frontUpstream(10);
		String ps2End = ps2.endUpstream(10);
		NuclMatrix nuclMatrix = new NuclMatrix();
		 
		if(ps1Front.length()>5&&ps2Front.length()>5){
			
			double frontIdent=SeqAlign.localIdentity(ps1Front, ps2Front, nuclMatrix);
			//System.out.println(frontIdent+"  "+matchedFrontSeq+"  "+frontSeqOfSubject);
			if(frontIdent>maxIdent){
				return  true;
			} else{
				//
			}
		}else if(ps1End.length() > 5&& ps2End.length() > 5){
			double endIdent=seqAlign.SeqAlign.localIdentity(ps1End, ps2End, nuclMatrix); 
			//System.out.println("endIdnet  "+endIdent+"  "+matchedEndSeq+"  "+endSeqOfSubject);
			if(endIdent>maxIdent){
				return  true;
			}else{
				
			}
		} 
		return false;
	}
	public static boolean checkGC(String seq,double gcCutoff){
		//String seq = ps.getseq();
		 
		char c;
		int counter = 0;
		for(int i = 0; i < seq.length(); i++){
			c = seq.charAt(i);
			if((c == 'C')||(c == 'c')){
				counter ++;
			} 
			if((c == 'G')||(c == 'g')){
				counter ++;
			} 
		}
		//System.out.println(counter + "  " + seq.length() + " "+ (double)counter/seq.length());
		if((double)counter/seq.length() > gcCutoff){
			return true;
		}else {
			return false;
		}
		
	}
	public static int half(int num) {
		if(PubFunc.isOdd(num)){
			return (int)(num/2) +1;
		}else {
			return (int)(num/2);
		}
	}
	 
	public static boolean  isThreeSimilar(PalSeq ps1, PalSeq ps2, PalSeq ps3, int cutoff) {
		if(matchPalSeq(ps1, ps2, 2, true)&& matchPalSeq(ps1, ps3, 2, true)){
			return true;
		}else {
			return false;
		}
	}
	public static boolean isOverlap(PalSeq ps1, PalSeq ps2){
		int start1 = ps1.getStart();
		int end1 = ps1.getEnd();
		int start2 = ps2.getStart();
		int end2 = ps2.getEnd();
		TreeMap<Integer, Integer> start2End = new TreeMap<Integer, Integer>();
		start2End.put(start1,end1);
		start2End.put(start2, end2);
		int counter = 0;
		for(Integer start: start2End.keySet()){
	//		System.out.println(start +"  " + start2End.get(start));
			if(counter == 0 ){
				start1 = start;
				end1 = start2End.get(start);
			}
			if(counter == 1){
				start2 = start;
				end2 = start2End.get(start);
			}
			counter ++; 
		}
		
		if(end1 <= start2){
			return false;
		}else {
			return true;
		}
	}
	public static void main(String[] args) {
		String str1 = "gtgtcgacgc";;
		String frontStr1 = str1.substring(0,half(str1.length()));
		String endStr1 = str1.substring(half(str1.length()), str1.length());
		System.out.println(frontStr1 + "  " + endStr1 + half(str1.length()));
		PalSeq ps1 = new PalSeq("gtgtcgacgc>3..20|F3NBS3|100|0|0|null|0");
		PalSeq ps2 = new PalSeq("gtgtcgacgc>20..66|F3NBS3|100|0|0|null|0");
		if(matchPalSeq(ps1, ps2, 2, true)){
			System.out.println("overlap !");
		}else {
			System.out.println("not overlap!");
		}
		
	}

} 