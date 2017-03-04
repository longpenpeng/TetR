package search;

import java.io.IOException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.Module; 
import search.PalSeq;
import io.PubFunc;

public class Palindrome_2 {
	
	/** palindromic name is same with  sequence accession*/
	public String palName;
	/**  */
	public int length;
	public String entropy="100";
	public String ident;
	/**the upstream  whole length nucleotide sequence*/
	public String upstreamSeq;
	/**in one palindrome file, there are many {@code PalSeq} */ 
	public ArrayList<PalSeq> palSeqs=new ArrayList<PalSeq>(); 
	
	public Palindrome_2() {
		
	}
	/**
	 * the input {@code Module} module contains one result of a upstream 
	 * sequence(500bp)
	 * @param module
	 * @param upstream
	 */

	public   Palindrome_2(Module module,HashMap<String, String> entryToEntropyMap,HashMap<String, String>entryToident){
		boolean boFirst=false;
		boolean boSecd=false;
		ArrayList<Module> modules=new ArrayList<Module>();
		Module m=null;
		for(int i=0;i<module.getNum();i++){
			String line=module.readLine(i);
			if(line.startsWith("Palindromes of")){
				String[] spt=line.split(" +");
				this.palName=spt[2];
				if(entryToEntropyMap.keySet().contains(palName)){
					this.entropy=entryToEntropyMap.get(palName);
				} 
				if(entryToident.keySet().contains(palName)){
					this.ident=entryToident.get(palName);
				}
			}
			if(line.startsWith("Sequence length is:")){
				String[] spt=line.split(" +");
				this.length=Integer.parseInt(spt[3]);
				}
			if(line.startsWith("Palindromes:")){
				boFirst=true;
			 	m=new Module();
			 	modules.add(m); 
			 	continue;
			}
			if(boFirst&&line.length()>0){
					boSecd=true;
			}
			if(boFirst&&line.length()==0){
			//	System.out.println(line);
				boSecd=false;
				m=new Module();
			 	modules.add(m);
			 	continue;
		    }
			if(boFirst==true&&boSecd==true){
				m.add(line); 
			}
		}
		ArrayList<PalSeq> palSeqs=new ArrayList<PalSeq>();
//		for(Module module2:modules){
//			if(module2.getNum()==0){
//				continue;
//			}  
////			PalSeq palSeq=new PalSeq(module2, this.palName,this.entropy,
////					this.ident,this.upstreamSeq); 
//			palSeqs.add(palSeq); 
//			this.palSeqs=palSeqs; 
//	    } 
		
	}
	public static ArrayList<Palindrome_2> getPals(String upstreamPath,String 
			similarEntryPath,String identPath) throws IOException{
		Map<String, String> entryToIdentMap=new HashMap<String, String>();
		entryToIdentMap= io.PubFunc.getMapFromFile(identPath, 1, 7, " +");
		ArrayList<Module> modules=new ArrayList<Module>(); 
		modules=Module.getModules(upstreamPath, "Palindromes of:");
		ArrayList<Palindrome_2> palindromes=new ArrayList<Palindrome_2>();
		Map<String ,String> entryToEntropyMap=io.PubFunc.getMapFromFile(
				similarEntryPath, 1, 2, " +");
//		for(Module module :modules){ 
//			Palindrome_2 palindrome=new Palindrome_2(module,entryToEntropyMap,entryToIdentMap);
//			if(palindrome.getPalSeqs().size()==0){
//				continue;
//			}
//			//System.out.println(palindrome.getPalName());
//			palindromes.add(palindrome); 
//			//writer.write(palindrome.toString()+FinalClass.lineSeparator);
//		//	System.out.println(palindrome.getPalSeqs().size());
//		}
		return palindromes;
	}
	public static ArrayList<Palindrome_2> getPals(String upstreamPath) throws IOException{
		ArrayList<Module> modules=new ArrayList<Module>(); 
		modules=Module.getModules(upstreamPath, "Palindromes of:");
		ArrayList<Palindrome_2> palindromes=new ArrayList<Palindrome_2>(); 
//		for(Module module :modules){ 
//			Palindrome_2 palindrome=new Palindrome_2(module);
//			if(palindrome.getPalSeqs().size()==0){
//				continue;
//			}
//			//System.out.println(palindrome.getPalName());
//			palindromes.add(palindrome); 
//			//writer.write(palindrome.toString()+FinalClass.lineSeparator);
//		//	System.out.println(palindrome.getPalSeqs().size());
//		}
		return palindromes;
	}
	public static Palindrome_2 getPalindrome(ArrayList<Palindrome_2> palindromes,String palName) {
		Palindrome_2 palindrome=new Palindrome_2();
		for(Palindrome_2 pal:palindromes){
			if(pal.getPalName().equals(palName)){
				palindrome=pal;
			}
		}
		return palindrome;
	}
	public ArrayList<PalSeq>  getPalSeqs(){
		return this.palSeqs;
	}
	public String getPalName(){
		return this.palName;
	}
//	public String toString(){
//		String line=this.palName+"|"+this.length+FinalClass.lineSeparator;
//		
//		for(PalSeq palSeq:this.palSeqs){
//			 line=line+palSeq.toString();
//		   }
//		return line ;
//	} 
	public static String upper(PalSeq ps){
		String seq=ps.getseq();
		String upper="";
		for(int i=0;i<seq.length();i++){
			char str=translate(seq.charAt(i));
			upper=upper+str;
		}
		return upper;
		
	}
	public static char translate(char c){
		if(c=='a'){
			return 'A';
		}else if(c=='g'){
			return 'G';
		}else if(c=='t'){
			return 'T';
		}else if(c=='c'){
			return 'C'; 
		}else  {
			return 'n';
		} 
	}
	 
	
	
	
	
	
	
	public static String getAcc(String line) {
		String[] spt = line.split("\\|");
		String acc = spt[1];
		return acc;
	}
	public static String getSeq(String line) {
		String[] spt = line.split(">|\\|");
		return spt[0];
		
	}
	public  static void  main(String[] args) {
		String line = "ttttantcata>435..445|Q8DWD0|100|1|0   **    5.0";
		System.out.println(getSeq(line));
	}
}
