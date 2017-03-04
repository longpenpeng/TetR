package handleResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import genome.Fasta;
import genome.MyFasta;
import io.Module;
import io.PubFunc;
import search.PalSeq;

public class MergeTrueOperator {
	/**
	 * extract the true operator based on the maxOut and similar patterns.
	 * maxOut file and similar pattern are overlapped, So, if an accession exists
	 * in both, here, just take the pattern in similar pattern file
	 * @param args
	 */
	public static void main(String[] args){
		String maxOut = "/home/longpengpeng/refine_TETR/merge/merge/maxOut";
		String sepLen = "/home/longpengpeng/refine_TETR/merge/merge/statistic/similar/similardetail";
		String similarPattern ="/home/longpengpeng/refine_TETR/merge/merge/similarPatten";
		String wholeDBD = "/home/longpengpeng/refine_TETR/metarial/wholeDBD";
		int maxCutoff = 35;
		int similarCutoff = 100;
		Map<String, String> accToPalseq = new HashMap<String, String>();
		List<String> accList = new ArrayList<String>();// store the acc just in similar 
		
		List<String> accList2  =new ArrayList<String>();//store the acc just in maxOut
		List<String> totalAccList = new ArrayList<String>();
		getTrueFromMaxOut(maxOut, maxCutoff, accToPalseq);
		getTrueFromSimilar(sepLen, similarCutoff, accList);
		System.out.println(accList.size()+ "  " + accToPalseq.size());
		for(String acc: accToPalseq.keySet()){
			if(accList.contains(acc)){
				continue;
			}else {
				accList2.add(acc);
			}
		}
		System.out.println(accList.size() +" "+ accList2.size()+"  "+(accList.size() + accList2.size()));
		totalAccList.addAll(accList2);
		totalAccList.addAll(accList);
		System.out.println(totalAccList.size());
		String line ="";
		String out ="/home/longpengpeng/refine_TETR/merge/merge/trueOperator";
				
		try {
			PrintWriter writer = new PrintWriter(out); 
			for(String acc:accList2){
				line = accToPalseq.get(acc);
				writer.write(">>>"+line + PubFunc.lineSeparator);
			}
			List<Module> modules = Module.getModules(similarPattern, ">>>");
			Map<String, Module> acc2Module = new HashMap<String,Module>();
			for(Module m:modules){
				String firstLine = m.readLine(0).split(" +")[1];
				acc2Module.put(new PalSeq(firstLine).getAcc(), m);
			}
			for(String acc:accList){
				writer.write(acc2Module.get(acc).toString());
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String DBDOut = "/home/longpengpeng/refine_TETR/merge/merge/DBDofTrueOperator";
		getDBD(wholeDBD, totalAccList,DBDOut);
	 
		
	}
	
	public static void getTrueFromMaxOut(String maxOut, int cutoff,
			Map<String, String> accToPalseq){
		
		 
		String line ="";
		String acc ="";
		String[] spt;
		PalSeq ps;
		int max;
		int counter = 0;
		try{
			BufferedReader in = new BufferedReader(new FileReader(maxOut));
			while((line = in.readLine())!= null){
				counter ++;
				spt = line.split(" +");
				ps = new PalSeq(spt[1]);
				acc = ps.getAcc();
				max = Integer.parseInt(spt[0]);
				if(max >= cutoff) {
					accToPalseq.put(acc, line);
				} 
			}
			in.close();
			System.out.println("maxOut  " + counter );
		}catch (Exception e) {
			System.err.println(e.getMessage());
		} 
	}
	public static void getTrueFromSimilar(String sepLen,int cutoff,List<String> accList){
		 
		String line ="";
		String acc ="";
		String[] spt;
		PalSeq ps;
		int max;
		int counter =0;
		try{
			BufferedReader in = new BufferedReader(new FileReader(sepLen));
			while((line = in.readLine())!= null){
				counter ++;
				spt = line.split(" +");
				acc = spt[spt.length-1];
				max = Integer.parseInt(spt[spt.length-2]);
				if(max <= cutoff) {
					accList.add(acc);
				} 
			}
			System.out.println("similar  " + counter );
			in.close();
		}catch (Exception e) {
			System.err.println(e.getMessage());
		} 
	}
	public static void getDBD(String wholeDBD,List<String> totalAccList,String DBDOut){
		List<Fasta> myFastas = Fasta.getFastasFromFile(wholeDBD);
		Map<String, Fasta> acc2MyFasta = new HashMap<String,Fasta>();
		for(Fasta fasta:myFastas){
			acc2MyFasta.put(fasta.getAcc(), fasta);
		}
		Map<String, Fasta> matchedAcc2Fasta = new HashMap<String, Fasta>();
		Fasta fasta;
		for(String acc:totalAccList){
			fasta = acc2MyFasta.get(acc);
			matchedAcc2Fasta.put(acc,acc2MyFasta.get(acc));  
		} 
		try {
			PrintWriter writer = new PrintWriter(DBDOut);
			for(String acc:matchedAcc2Fasta.keySet()){
				writer.write(matchedAcc2Fasta.get(acc).toString() +PubFunc.lineSeparator);
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
