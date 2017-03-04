package statistic_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map; 

import genome.Fasta;
import genome.MyFasta;
import io.Module;
import io.PubFunc;
import search.Palindrome;
import statistic_test.*;

public class Main_2 {
	
	public static void main(String[] args){
		
		String wholeDBD = args[0];//="/home/longpengpeng/refine_TETR/metarial/wholeDBD";
		String accFile = args[1];//= "/home/longpengpeng/refine_TETR/metarial/wholePalin"
				//+ "dromic/acc";
		
		String wholeUpstream = args [2];//= "/home/longpengpeng/refine_TETR/metarial/wholeUpstream";
		int minSeqSize = Integer.parseInt(args[3]);
		int maxSeqSize =  Integer.parseInt(args[4]);
		String outDir = args[5];
		String usefulPal = args[6];
		int brEnd = 42;
		int brStart = 20;
		int cutoff =10;
		List<String> accTotalList = PubFunc.getListFromFile(accFile, 1, " ");
		
		List<Fasta> fastas = Fasta.getFastasFromFile(wholeDBD);
		Map<String, Fasta> acc2Fasta = new HashMap<String, Fasta>();
		for(Fasta fasta:fastas){
			acc2Fasta.put(fasta.getAcc(), fasta); 
		}
		
		List<MyFasta> upstreamList = MyFasta.getFastasFromFile(wholeUpstream, ">");
		Map<String, MyFasta> acc2Upstream = new HashMap<String, MyFasta>();
		for(MyFasta myFasta:upstreamList){
			acc2Upstream.put(myFasta.getAcc(), myFasta);
		} 
		Map<String, Module>acc2PalModules = new HashMap<String, Module>(); 
		getMapFromFile(usefulPal, acc2PalModules); 
		
		String dir;
		String dir2;
		String obtainSeqPath = "";// = "/home/longpengpeng/refine_TETR/test_statistic/seq";
		String alignOut = "";// = "/home/longpengpeng/refine_TETR/test_statistic/aligOut";
		String matchedUpstream = "";// = "/home/longpengpeng/refine_TETR/test_statistic/"
				//+ "matchedUpstream";
		String palOut= "";// = "/home/longpengpeng/refine_TETR/test_statistic/palOut";
		String distr;// = "/home/longpengpeng/refine_TETR/test_statistic/distr"; 
		System.out.println("into loop");
		for(int i = minSeqSize; i <maxSeqSize +1; i++ ){
		
			dir =  outDir + PubFunc.os +i; 
			dir2 = dir + PubFunc.os + "out";
			distr = dir + PubFunc.os + "distr"; 
			PubFunc.creatDir(outDir);
			PubFunc.creatDir(dir);
			PubFunc.creatDir(dir2);
			obtainSeqPath = dir2 + PubFunc.os + "seq"; 
			alignOut = dir2 + PubFunc.os + "aligOut";
			matchedUpstream =  dir2 + PubFunc.os +  "matchedUpstream";
			palOut= dir2 + PubFunc.os + "palOut";
			for(int j = 0; j <1000; j++){
				try {
					run(accTotalList, i, acc2Fasta, obtainSeqPath, alignOut, brStart, brEnd,
							cutoff, acc2Upstream, matchedUpstream, palOut, distr,
							acc2PalModules);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		} 
	}
	
	public static void run(List<String> accList,int setSize, Map<String, Fasta> 
		acc2Fasta,String obtainSeqPath, String alignOut, int brStart, int brEnd,
		    int cutoff,Map<String, MyFasta> acc2Upstream,String matchedUpstream,
		    String palOut, String distr, Map<String, Module> acc2PalModules) throws FileNotFoundException{
		//String out = new File(distr).getParent() + PubFunc.os + "out";
		String query = SelectDBDSet.selectQuery(accList);
		//String queryDir = out  + PubFunc.os +query;
//		PubFunc.creatDir(out);
//		PubFunc.creatDir(queryDir);
//		
//		obtainSeqPath = queryDir + PubFunc.os + "seq"; 
//		alignOut = queryDir + PubFunc.os + "aligOut";
//		matchedUpstream =  queryDir + PubFunc.os +  "matchedUpstream";
//		palOut= queryDir + PubFunc.os + "palOut";
		System.out.println("run");
		List<String> obtainedAcc  = new ArrayList<String>();
		obtainedAcc.add(query);
//		List<String> obtainedDBDSet = SelectDBDSet.(accList, query,
//				setSize, acc2Fasta, obtainSeqPath, alignOut, brStart,
//				brEnd, cutoff, obtainedAcc ); 
		//SelectDBDSet.palSet(obtainedDBDSet, acc2Upstream, matchedUpstream, palOut);
		Module m;
		MyFasta myFasta;
		Palindrome queryPal = new Palindrome();
		List<Palindrome> pls = new ArrayList<Palindrome>();
		for(String acc:obtainedAcc){
			m = acc2PalModules.get(acc);
			if(acc.equals(query)){
				queryPal = new Palindrome(m, acc2Upstream, matchedUpstream);
			}else {
				pls.add(new Palindrome(m, acc2Upstream, matchedUpstream));
			}
			pls.add(new Palindrome(m, acc2Upstream, matchedUpstream));
		}
//		List<Palindrome> pls = new ArrayList<Palindrome>();
//		for(String acc:obtainedAcc){
//			if(acc.equals(query)){
//				continue;
//			}
//			if(acc2pls.containsKey(acc)){
//				pls.add(acc2pls.get(acc));
//			}
//		}
//		 
//		Palindrome queryPal = acc2pls.get(query); 
		 
		statistic_test.SeqSearch.seqSearch(pls,query, queryPal,
				matchedUpstream, distr);
	}
	public  static void getMapFromFile(String pal, Map<String, Module>acc2PalModules ) {
	 
		List<Module> modules = Module.getModules(pal, "Palindromes of");
		Iterator<Module> iter = modules.iterator(); 
		Module m ; 
		String acc ;
		//Map<String, Module> acc2PalModules = new HashMap<String, Module>();
		while(iter.hasNext()){
			m = iter.next();
			acc = m.readLine(0).split(" +")[2];
			acc2PalModules.put(acc,m);
		}
		 
	}
}
