package runOutProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.*;

public class AlignResult {
	
	private String acc;
	private double score;
	private double evalue; 
	private int aliFrom;
	private int aliTo; 
	private String seq;
	
	public AlignResult(){
		
	}
	 
	public AlignResult(String line) {
		String[] spt = line.split(" +");
		acc = spt[0];
		seq = spt[1];
	}
	public AlignResult(String acc, String seq) {
		this.acc =acc;
		this.seq = seq;
	}
	public AlignResult(String acc, String score, String evalue, String aliFrom,
			String aliTo, String seq){
		this.acc = acc;
		this.score = Double.parseDouble(score);
		this.evalue = Double.parseDouble(evalue);
		this.aliFrom = Integer.parseInt(aliFrom);
		this.aliTo = Integer.parseInt(aliTo);
		this.seq = seq;
		
	}
	/**
	 * get the  each align result from the msa out put file
	 * there are two formats of sequence header:
	 * 	(1) ">acc  seq" (when the k =1)
	 * 		it is a simply format.For this format,only one string Accession 
	 * 		need to extract; the accession and sequence can
	 * 		extract easily
	 * 	(2) ">acc|hmmScore|Evalue|aliFro|aliTo  seq"(when the k = 5)
	 * @param DBDSeqPath
	 * @param k  k=1, or k=5
	 * 			k means there are k string need to extract in the header
	 * 
	 * @return
	 */
	public static List<AlignResult> getAlignReslt(String DBDSeqPath,int k) {
		String line = ""; 
		Map<String, AlignResult> acc2AR = new HashMap<String, AlignResult>(); 
		AlignResult ar;
		String[] spt;
		try {
			BufferedReader in = new BufferedReader(new FileReader(DBDSeqPath));
			while((line = in.readLine()) != null) {
				if(line.startsWith("CLUSTAL") || line.trim().length() ==0){
					continue;
				}
				if(line.contains(":")||line.contains("*") || line.trim().length()<5){
					continue;
				}
				spt = line.split(" +");
				String seq = spt[1];
			 
				if( k == 1) { 
					String acc = spt[0]; 
					if(!acc2AR.containsKey(acc)){
						ar = new AlignResult(acc, seq);
						acc2AR.put(acc, ar);  
					}else { 
						 acc2AR.get(acc).setSeq(acc2AR.get(acc).getSeq() +seq); 
					}
				}else {
					String[] spt2 = spt[0].split("\\|");
					String acc = spt2[0];
					String hmmScore=spt2[1];
					String Evalue=spt2[2];
					String aliFro=spt2[3];
					String aliTo=spt2[4];
					if(!acc2AR.containsKey(acc)) {
						ar = new AlignResult(acc, hmmScore, Evalue, aliFro, 
								aliTo, seq);
						acc2AR.put(acc, ar); 
					}else {
						acc2AR.get(acc).setSeq(acc2AR.get(acc).getSeq() + seq);
					}
				}
			}
			in.close();
		}catch (Exception e) {
			System.err.println(e.getMessage());
		} 
		List<AlignResult> ars= new ArrayList<AlignResult>(acc2AR.values()); 
		return ars; 
	}
	
	public static List<AlignResult> write2File(String input,int k, String output) {
		List<AlignResult> ars = getAlignReslt(input, k);
		try{
			PrintWriter writer = new PrintWriter(output);
			for(AlignResult ar: ars) {
				writer.write(ar.toString() + PubFunc.lineSeparator);
				writer.flush();
			}
			writer.close();
		}catch (FileNotFoundException e) {
			System.err.println("align result file does not exist! " + 
					e.getMessage());
		}
		return ars;
	}
	
	public static ArrayList<AlignResult>  getAlignResults(String DBDSeqPath) throws IOException {
		File DBDSeqFile=new File(DBDSeqPath);
		ArrayList<AlignResult> alignResults=new ArrayList<AlignResult>();
		BufferedReader in=null;
		try {
			 in=new BufferedReader(new FileReader(DBDSeqFile));
		} catch (FileNotFoundException e) { 
			System.out.println(e);
		}
		String line="";
		AlignResult alignResult=null; 
		ArrayList<String>entries=new ArrayList<String>();
		String seq="";
		@SuppressWarnings("unused")
		int counter = 0;
		while((line=in.readLine())!=null){ 
			counter++;
			if(line.startsWith("CLUSTAL")||line.length()==0){
				continue;
			} 
			if(line.contains(":")||line.contains("*") || line.trim().length()<5){
				continue;
			}
			if(!line.substring(0,20).contains("|")){
				continue;
			}
			String[] spt2=line.split(" +");
			seq=spt2[1];
			String[] spt=spt2[0].split("\\|"); 
			String acc=spt[0]; 
		 
			if(entries.contains(acc)){ 
				//System.out.println(entries.size());
				for(AlignResult aR:alignResults){
					if(aR.getAcc().equals(acc)){
						aR.setSeq(aR.getSeq()+seq);
						//System.out.println(spt[5]);
					}
				}   
			}else{  
				alignResult=new AlignResult(); 
				//alignResults.add(alignResult);
				entries.add(acc); 
				//System.out.println(entries.size());
				String hmmScore=spt[1];
				String Evalue=spt[2];
				String aliFro=spt[3];
				String aliTo=spt[4];
				//String hmmFro=spt[5];
				//String hmmTo=spt[6];
				//System.out.println(entry+" "+hmmScore+ " "+Evalue+" "+aliFro+ " "+aliTo+" "+seq+" ");
				//String seq=spt[5]; 
			//	System.out.println(aliTo+"   "+seq+" "+spt[6]);
		    	 //System.out.println(entry+" "+hmmScore+ " "+Evalue+" "+aliFro+ " "+aliTo+" "+seq+" ");
				alignResult.setAcc(acc);
				alignResult.setScore(hmmScore);
				alignResult.setEvalue(Evalue);
				alignResult.setAliTo(aliTo);
				alignResult.setAliFrom(aliFro);
				alignResult.setSeq(seq); 
				if(alignResults.contains(alignResult)){
					//System.out.println(alignResult.toString());
					continue;
					
				}else{
					alignResults.add(alignResult);
				}
				}
		}
		//System.out.println(counter);
		return alignResults;
	} 
	public String getAcc(){
		return this.acc;
	}
	public String getSeq() {
		return this.seq;
	}
	public String setAcc(String acc){
		return this.acc = acc;
	} 
	public double setScore(String score){
		return this.score=Double.parseDouble(score);
	}
	public double setEvalue(String evalue){
		return this.evalue=Double.parseDouble(evalue);
	}
	public 	int setAliFrom(String aliFrom) {
		return this.aliFrom=Integer.parseInt(aliFrom); 
	}
	public int setAliTo(String aliTo) {
		return this.aliTo=Integer.parseInt(aliTo); 
	}
	public double getHmmScore(double hmmScore)	{
		return this.score=hmmScore;
	}
	public int getAliFrom(int aliFro){
		return this.aliFrom;
	}
	public String setSeq(String seq) {
		return this.seq=seq; 
	}
	public int getAliTo(int aliTo){
		return this.aliTo; 
	}
	public String toString() { 
		 String line=">"+this.acc+"|"+this.score+"|"+this.evalue+"|"+
				 	this.aliFrom+"|"+this.aliTo+"|"+PubFunc.lineSeparator+this.seq;
		 return line; 
	}
	public static void main(String[] args) throws IOException{
		 String path="/home/longpengpeng/work/program_test/test_protein/test_Hmmresult/alignOut"; 
		 ArrayList<AlignResult > alignResults=new ArrayList<AlignResult>();
		 alignResults=AlignResult.getAlignResults(path);
		 for(AlignResult aR:alignResults){
			 System.out.println(aR.toString());
		 }
	 }

}
