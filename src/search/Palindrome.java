package search;

import java.io.File;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.Other;

import genome.MyFasta;
import io.Module;
import io.PrintUtils;
import io.PubFunc;

public class Palindrome {
	
	/**the accession*/
	private String palName; 
	private int length;
	private String ident;
	/**dissimilar number in the binding region of palName's protein binding region*/
	private int dissimilar;
	private List<PalSeq> palSeqs;
	/**the nucleotide sequences which took as the input of this palindrome program */
	private String upstreamSeq;
	
	public Palindrome(){
		
	}
	public Palindrome(Module m, Map<String, MyFasta> acc2Upstream, String upstreamPath){
		upstreamSeq =upstreamPath;
		String line = "";
		boolean flag = false; 
		List<Module> modules = new ArrayList<Module>();
		Module module = null;
		int counter = 0;
		for(int i = 0; i < m.getNum(); i++){
			line = m.readLine(i);
			/*
			 * if boolean flag is true and the  ,it means 
			 * if the line start with "Palindromes:", initialize a Module,
			 * prepare for adding lines
			 */
			if(flag &&line.trim().length() == 0){ 
				counter ++;
				module=new Module();
			 	modules.add(module);
			 	continue; 
			}
			/*check whether is end or not*/
			if(counter >1){
				break;
			}
			if(flag &&line.trim().length() > 0){
				counter =0;
				module.add(line);
				continue;
			} 
			/*
			 * if the line start with "Palindromes:", initialize a Module,
			 * prepare for adding lines
			 */
			if(line.startsWith("Palindromes:")){
				flag = true;  
				module=new Module();
			 	modules.add(module);
				continue;
			} 
			if(line.startsWith("Palindromes of")){
				palName = line.split(" +")[2];
				continue;
			}
			if(line.startsWith("Sequence")){
				length = Integer.parseInt(line.split(" +")[3]);
				continue;
			}
		}
		MyFasta fasta = acc2Upstream.get(palName);
		upstreamSeq = fasta.getSeq();
		ident =fasta.getIdent();
		dissimilar = fasta.getDissimilar();
		Iterator<Module> iter = modules.iterator(); 
		palSeqs = new ArrayList<PalSeq>();
		while(iter.hasNext()) {
			module = iter.next();
			if(module.getNum() == 0){
				continue;
			}
			PalSeq ps = new PalSeq(module, palName, ident, dissimilar, upstreamSeq, 
					fasta.getAcc());
			palSeqs.add(ps);
		}
	}
	public static List<Palindrome> getPalindromesFromFile(String pal, String upstream){
		List<MyFasta> myFastas = MyFasta.getFastasFromFile(upstream, ">");
		List<Module> modules = Module.getModules(pal, "Palindromes of");
		Iterator<Module> iter = modules.iterator();
		Module m ;
		Palindrome pl;
		Map<String, MyFasta> acc2Upstream = new HashMap<String, MyFasta>();
		for(MyFasta myFasta:myFastas){
			acc2Upstream.put(myFasta.getAcc(), myFasta);
		} 
		List<Palindrome> pls = new ArrayList<Palindrome>();
		
		System.out.println(modules.size());
		
		while(iter.hasNext()){
			m = iter.next();
			pl = new Palindrome(m, acc2Upstream, upstream);
			pls.add(pl);
		}
		return pls;
	}
	public  static void getMapFromFile(String pal, String upstream,
			Map<String, MyFasta> acc2Upstream,Map<String, Palindrome> acc2Pls) {
		//List<MyFasta> myFastas = MyFasta.getFastasFromFile(upstream, ">");
		List<Module> modules = Module.getModules(pal, "Palindromes of");
		Iterator<Module> iter = modules.iterator();
//		Map<String, MyFasta> acc2Upstream = new HashMap<String, MyFasta>();
//		for(MyFasta myFasta:myFastas){
//			acc2Upstream.put(myFasta.getAcc(), myFasta);
//		} 
		Module m ;
		Palindrome pl; 
		while(iter.hasNext()){
			m = iter.next();
			pl = new Palindrome(m, acc2Upstream, upstream);
			acc2Pls.put(pl.getPalName(), pl);
		}
	 
	}
	public static Palindrome getCertainPal(String acc, List<Palindrome> pls){
		Iterator<Palindrome> iter = pls.iterator();
		Palindrome ps1;
		Palindrome ps2 = new Palindrome();
		while(iter.hasNext()) {
			ps1 = iter.next();
			if(ps1.getPalName().equals(acc)) {
				ps2 =ps1;
				break;
			}
		}
		return ps2;
	}
	public static void write2File(List<Palindrome> pls,String upstream){
		String outDir = new File(upstream).getParent() + PubFunc.os + "PalOut";
		PubFunc.creatDir(outDir);
		Iterator<Palindrome> iter = pls.iterator();
		PrintWriter writer;
		while (iter.hasNext()) {
			Palindrome pl = iter.next();
			try {
				writer = new PrintWriter(outDir + PubFunc.os + pl.palName);
				writer.write(pl.toString());
				writer.flush();
				writer.close();
			} catch (Exception e) {
				System.err.println("the file not existed!" + e.getMessage());
			}
		} 
	}
	
	//***********************getter************************
	public String getPalName() {
		return this.palName;
	}
	public List<PalSeq> getPalSeqs(){
		return this.palSeqs;
	}
	
	
	public String toString(){
		String str= ">"+palName + "|" + dissimilar + "|" + length +"|" +ident +
				PubFunc.lineSeparator;
		Iterator<PalSeq> iter = this.palSeqs.iterator();
		while(iter.hasNext()){
			str += iter.next().toString() + PubFunc.lineSeparator;
		}
		return str;
	}
	public static void main(String[] args) {
		String upstream = "/home/longpengpeng/refine_TETR/test/test_prog/M7MXG9/out/palindrome/matchedUpstream";
		String pal = "/home/longpengpeng/refine_TETR/test/test_prog/M7MXG9/out/palindrome/pal.pal";
		String out = "/home/longpengpeng/refine_TETR/test/test_prog/M7MXG9/out/palindrome/out";
		List<Palindrome> pls =getPalindromesFromFile(pal, upstream);
		System.out.println(pls.size());
		write2File(pls, upstream);
				
	}

}
