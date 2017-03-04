package statistic_test;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import genome.Fasta;
import genome.MyFasta;
import io.PubFunc;
import runOutProgram.*; 

/**
 * this file contains some functions selecting query DBD and generate DBD set
 * with two different criterion
 * @author longpengpeng
 *
 */
public class SelectDBDSet {
	
	public static String selectQuery(List<String> accList){
		int rand = (int)(Math.random()*accList.size());
		//System.out.println(Math.random());
		return accList.get(rand);
	}
	
	public static List<String> generateDBDSetRand(List<String> accList,String query, int setSize){
		List<String> obtainedAcc = new ArrayList<String>();
		obtainedAcc.add(query);
		int rand;
		String acc;
		for(int i = 0; obtainedAcc.size() -1 < setSize; i++){
			rand = (int)(Math.random()*accList.size());
			acc = accList.get(rand);
			System.out.println(acc);
			 
			if(obtainedAcc.contains(acc)){
				continue;
			}else {
				obtainedAcc.add(acc);
				continue;
			} 
		}
		//obtainedAcc.remove(query);
		System.out.println(obtainedAcc.size());
		return obtainedAcc;
	}

	public static void generateDBDSetDissimiar(List<String> accList, String query,
				int setSize, Map<String, Fasta> acc2Fasta, String obtainSeqPath) 
						throws FileNotFoundException{	
		// List<String> obtainedAcc : store matched accession
		PrintWriter writer = new PrintWriter(obtainSeqPath);
		// a temp list stored the accession generated randomly
		List<String> accs = new ArrayList<String>(); 
		accs.add(query);
		int rand;
		String acc;
		int addSize = 0;
		if(setSize <50){
			addSize = 50;
		}else if(setSize <100){
			addSize = 100;
		}else if( setSize < 200){
			addSize = 150;
		}
		else if(setSize < 300){
			addSize = 200;
		}else {
			addSize =500;
		}
		for(int i = 0; accs.size() -1 < setSize + addSize ; i++){
			rand = (int)(Math.random()*accList.size());
			acc = accList.get(rand); 
			if(accs.contains(acc)){
				continue;
			}else {
				accs.add(acc);
				continue;
			} 
		}
		//System.out.println(accs.size());
		//get the DBD sequence of corresponding accession and write into a file
		for(String accession:accs){
			if(acc2Fasta.containsKey(accession)){
				writer.write(acc2Fasta.get(accession).toString()+ 
						PubFunc.lineSeparator);
			}
			writer.flush();
		}
		writer.close();
	} 
	public static void checkSimilar(String query,List<AlignResult> ars,
			List<String> obtainedAcc,int brStart,int brEnd, int cutoff, int setSize,
			String setSizeError){
		AlignResult queryAR = new AlignResult();
		for(AlignResult ar:ars){
			if(ar.getAcc().equals(query)){
				queryAR = ar;
			}
		} 
		obtainedAcc.add(query);
		Iterator<AlignResult> it = ars.iterator();
		AlignResult ar;
		int counter;
		String acc;
		String str1 = queryAR.getSeq();
		String str2;
		while(it.hasNext()){
			ar = it.next(); 
			acc = ar.getAcc();
			 
			str2 = ar.getSeq();//.substring(brStart, brEnd);
			if(ar == null || ar.getSeq() ==null){
				continue;
			}
			if(str2.length() < (brEnd - brStart)){
				continue;
			} 
			counter = 0;  
			for(int i = brStart; i< brEnd; i++) {
				if(str1.charAt(i) != str2.charAt(i)) {
					counter ++;  
				}  
			}
			if(counter > cutoff) {
				if(obtainedAcc.contains(acc)){
					continue;
				}else {
					obtainedAcc.add(acc);
				}
			}  
			if(obtainedAcc.size() >= setSize){
				break;
			}
		}
		if(obtainedAcc.size() < setSize){
			io.WriteStreamAppend.appendBufferWriter(setSizeError, setSize+ " " + 
					query + " " +obtainedAcc.size()+ PubFunc.lineSeparator);
		}
	} 
	public  static void extractAccFromPalFile() {
		String wholePal ="/home/longpengpeng/refine_TETR/metarial/wholePalindromic/usefulUpstream";
		String out = "/home/longpengpeng/refine_TETR/metarial/wholePalin"
				+ "dromic/acc";
	 
		try {
			BufferedReader in = new BufferedReader(new FileReader(wholePal));
			String line ="";
			PrintWriter writer = new PrintWriter(out);
			while((line = in.readLine())!= null){
				if(line.startsWith(">")){
					String[] spt = line.split("\\|");
					writer.write(spt[spt.length-1] + PubFunc.lineSeparator);
				}
			}
			
			in.close();
			writer.flush();
			writer.close();
					
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	 
	public static void palSet(List<String> obtainedDBDSet, Map<String,MyFasta>
						acc2Upstream, String out) {
		
		try {
			MyFasta myFasta;
			PrintWriter writer = new PrintWriter(out);
			for(String acc:obtainedDBDSet){
				if(acc2Upstream.containsKey(acc)){
					myFasta = acc2Upstream.get(acc);
					writer.write(">" + myFasta.getAcc() +"|"+ myFasta.getLocation()
							+"|"+myFasta.getOrientation()+"|"+myFasta.getFileName()
							+"|" +null+"|"+0+"|"+null+ "|"+0.0+"|"+myFasta.getAcc()+
							PubFunc.lineSeparator+myFasta.getSeq()+PubFunc.lineSeparator);
				}
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//runOutProgram.RunPalidrome rp = new RunPalidrome();
		//rp.runPal(out, palOut, 5, 20, 6,2, false); 
	}
public static void main(String[] args){
		
		String wholeDBD = args[0];//="/home/longpengpeng/refine_TETR/metarial/wholeDBD";
		String accFile = args[1];//= "/home/longpengpeng/refine_TETR/metarial/wholePalin"
				//+ "dromic/acc"; 
		String outDir = args[2]; 
		List<String> accTotalList = PubFunc.getListFromFile(accFile, 1, " ");
		
		List<Fasta> fastas = Fasta.getFastasFromFile(wholeDBD);
		Map<String, Fasta> acc2Fasta = new HashMap<String, Fasta>();
		for(Fasta fasta:fastas){
			acc2Fasta.put(fasta.getAcc(), fasta); 
		} 
		int setSize = Integer.parseInt(args[3]);
		String dir;
		String dir2; 
		dir =  outDir + PubFunc.os +setSize; 
		//dir2 = dir + PubFunc.os + "out";
		String DBD = dir + PubFunc.os +"DBD"; 
		PubFunc.creatDir(outDir);
		PubFunc.creatDir(dir);
		//PubFunc.creatDir(dir2);
		//PubFunc.creatDir(DBD); 
		int counter = 0;
		try {
		
			for(int j = 0; j <3000; j++){
				counter ++;
				String query = SelectDBDSet.selectQuery(accTotalList);  
				SelectDBDSet.generateDBDSetDissimiar(accTotalList, query,
						setSize, acc2Fasta, DBD + PubFunc.os + query+"_"+j);
			}
			System.out.println(counter);
		} catch (Exception e) {
			e.printStackTrace();
		}
}
}
	 
