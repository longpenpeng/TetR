package genome;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
 
import io.*;;

public class HmmResult {
	private String entryName;
	private double score;
	private double evalue;
	private int hmmFrom;
	private int hmmTo;
	private int aliFrom;
	private int aliTo;
	private String domainSeq;
	public HmmResult(){
		
	}
	public static ArrayList<HmmResult> getHmmResults(String hmmOutPath) throws IOException{
	   
		HmmResult hmmResult=null;  
		ArrayList<String>entries=new ArrayList<String>();
		ArrayList<HmmResult> hmmResults=new ArrayList<HmmResult>();
		ArrayList<Module>modules=new ArrayList<Module>();
		modules=Module.getModules(hmmOutPath,">> "); 
		for(Module m:modules){
			hmmResult=new HmmResult(); 
			hmmResult.setEntryName(firstLine(m));
			if(entries.contains(firstLine(m))){
				continue;
			}else{
				entries.add(firstLine(m));
			}
			 
			if(m.getNum()!=13){
				continue;
			} 
			String scoreLine=m.readLine(3);
			String[] spt=scoreLine.split(" +");
			hmmResult.setScore(spt[3]);
			hmmResult.setEvalue(spt[5]);
			hmmResult.setHmmFrom(spt[7]);
			hmmResult.setHmmTo(spt[8]);
			hmmResult.setAliFrom(spt[10]);
			hmmResult.setAliTo(spt[11]);
			String domianLine=m.readLine(10);
			String[] spt2=domianLine.split(" +");
			
			hmmResult.setDomainSeq(spt2[3]);  
			hmmResults.add(hmmResult); 
			 
		}
		return hmmResults;
	}
	public static String firstLine(Module m){
		
		String firstLine=m.readLine(0);
		String entry;
		if(firstLine.contains("|")){
			String[] spt=firstLine.split("\\|");
			entry=spt[1];
		}else{
			entry=firstLine.replace(">>","").trim();
		} 
		return entry; 
	}
	public String setEntryName(String entryName) {
		return this.entryName=entryName;
		
	}
	public double setScore(String score){
		return this.score=Double.parseDouble(score);
	}
	public double   setEvalue(String evalue) {
		return this.evalue=Double.parseDouble(evalue); 
	}
	public int setHmmFrom(String hmmFrom){
		return this.hmmFrom=Integer.parseInt(hmmFrom);
	}
	public int setHmmTo(String hmmTo){
		return this.hmmTo=Integer.parseInt(hmmTo);
	}
	public int setAliFrom(String aliFrom){
		return this.aliFrom=Integer.parseInt(aliFrom);
	}
	public int setAliTo(String aliTo){
		return this.aliTo=Integer.parseInt(aliTo);
	}
	public  String setDomainSeq(String domainSeq) {
		return this.domainSeq=domainSeq;
		
	} 
	public double  getScore() {
		return this.score; 
	}
	public String getDomainSeq(){
		return this.domainSeq;
	}
	public String getEntryName(){
		return this.entryName;
	}
	
	public String toString() {
		String line=">"+this.entryName+"|"+this.score+"|"+this.evalue+"|"+aliFrom+"|"+aliTo+
				"|"+this.hmmFrom+"|"+this.hmmTo+ PubFunc.lineSeparator+this.domainSeq+ PubFunc.lineSeparator;
		return line;
	}
	public static void getHmmResult(String hmmOutPath,String outpath,int minscore) throws IOException{
		File hmmOutFile=new File(hmmOutPath);
		PrintWriter outFile=new PrintWriter(outpath);
		ArrayList<HmmResult> hmmResults=new ArrayList<HmmResult>(); 
	    hmmResults=getHmmResults(hmmOutPath); 
		for(HmmResult hS:hmmResults){
			if(hS.getScore()<minscore){
				continue;
			}
			outFile.write(hS.toString());
		}
		outFile.flush();
		outFile.close(); 
	}
	
	public static void testGetHR(String path,String outpath) throws IOException{
		
		ArrayList<HmmResult> hmmResults=new ArrayList<HmmResult>();
		hmmResults=getHmmResults(path);
		PrintWriter writer=new PrintWriter(outpath); 
		for(HmmResult hm:hmmResults){
			writer.write(hm.toString()); 
		}
		writer.flush();
		writer.close(); 
	}
	

}
