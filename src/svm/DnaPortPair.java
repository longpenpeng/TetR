package svm;

import java.io.File;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import genome.Fasta;
import io.Module;
import io.PubFunc;
import search.Palindrome_2;

/**
 * this {@code DnaProtPair} used to hander the operator and corresponding 
 * dna-binding sequence, merging these two types of sequences
 * <p>
 * 	the format is :
 * 			">acc   protseq
 *    nuclSeq1   nuclSeq2 ...."
 * @author Administrator
 *
 */
public class DnaPortPair {
	/** acc is the accession of protein sequence*/
	private String acc;
	/** protSeq caches the protein sequence */
	private String protSeq;
	/** nuclList is a list contained several nucleotide sequence*/
	private List<String> nuclLists = new ArrayList<String>();  
	
	public DnaPortPair() {
		
	}
	/**
	 * initialize a {@code DnaProtPair} instance
	 * @param acc
	 * @param protSeq
	 */
	public DnaPortPair(String acc, String protSeq) {
		this.acc = acc;
		this.protSeq = protSeq;
	}
	 
	/**
	 * initialize a instance with a protein{@code Fasta} and corresponding 
	 * operator
	 * @param protFa
	 * @param nuclMo
	 */
	public DnaPortPair(Fasta protFa, Module nuclMo ) {
		acc = protFa.getAcc();
		protSeq = protFa.getSeq();
		String nucl = "";
		for(int i = 1; i < nuclMo.getNum(); i++) {
			nucl = Palindrome_2.getSeq(nuclMo.readLine(i).trim() );
			nuclLists.add(nucl);
		} 
	} 
	
	public DnaPortPair(Module m) {
		String firstLine = m.readLine(0);
		String[] spt1 = firstLine.split(" +");
		acc = spt1[0].replace(">>>", "");
		protSeq = spt1[1];
		//System.out.println(acc + " " + protSeq);
		String secLine = m.readLine(1);
		String[] spt2 = secLine.split(" +");
		for(String rna : spt2) {
			nuclLists.add(Palindrome_2.getSeq(rna));
		}
	}
	//***********************getter************************
	public  String getProtSeq() {
		return this.protSeq;
	}
	
	public List<String> getNuclList(){
		return this.nuclLists;
	}
	
	public String getAcc(){
		return this.acc;
	}
	
	//***********************setter************************ 
	public void setProtSeq(String protSeq) {
		this.protSeq = protSeq;
	}
	
	public void setAcc(String acc) {
		this.acc = acc;
	}
	
	public void setNuclList( List<String> nuclLists) {
		this.nuclLists = nuclLists;
	}
	
	public static List<DnaPortPair> getDnaPortPairsFromFile(String file){
		List<Module> modules = Module.getModules(file, ">>>");
		List<DnaPortPair> dpps = new ArrayList<DnaPortPair>();
		for(Module m:modules) {
			dpps.add(new DnaPortPair(m));
		}
		return dpps;
	}
	 /**
	  * this method can merge the protein fasta and the nucleotide into a file
	  * <p>
	  * 	protein fasta :" 
	  *    >A0A0P7G591
	  *		LDEAMMLFWEKGYKATSLSDLTAKMGIKRPSLYAAFGDKEELFEAA
	  *		>U4T171
      *		LDRALQLFWEKGFHATSLKDIEKALDMRPGSIYATFGSKDGLFQEA 
      *		nucleotide sequence:
      *    >>>ttttantcata>435..445|Q8DWD0|100|1|0   **    5.0
	  *		ttttantcata>340..350|Q8DWD0|100|1|0         5.0
	  *		>>>ctcaccgncgatgag>201..215|Q8FR27|100|1|0   **    12.0
	  *		cgcaccgncgatgtg>186..200|Q8FR27|100|1|0         9.0
	  * output:
	  * 	>>>U4T171  LDRALQLFWEKGFHATSLKDIEKALDMRPGSIYATFGSKDGLFQEA
	  *		tgagcaatcactca>416..429|U4T171|100|0|0   atttgagtaatcactcaaat>262..281|U4T171|100|0|0   
	  *	 	>>>Z9JTG1  LDAVRDQFWTAGYAATSLEDLMRVSGLGKGSLYAAFGDKRELFLR
	  * 	tggaccgnncggtcaa>49..64|Z9JTG1|100|2|0   gttcttgaccgnncggtccagtac>16..39|Z9JTG1|100|2|0    
	  * 
	  * @param protPath
	  * @param pattern
	  * @param output
	  */

	public static void mergeProtAndNucl(String protPath, String pattern, String output) {
		List<Module> modules = Module.getModules(pattern, ">>>");
		List<Fasta> protFaList = Fasta.getFastasFromFile(protPath); 
		Map<String, String> acc2prot = new HashMap<String, String>();
		Fasta fasta;
		Iterator<Fasta> iter = protFaList.iterator();
		while(iter.hasNext()) {
			fasta = iter.next();
			acc2prot.put(fasta.getAcc(), fasta.getSeq()); 
		}
		Module m;
		String acc2; 
		String[] spt;
		Iterator<Module> mIter = modules.iterator();
		try{
			PrintWriter writer = new PrintWriter(output);
			while(mIter.hasNext()){
				m = mIter.next();
				acc2 = Palindrome_2.getAcc(m.readLine(0)).trim(); 
				if(acc2prot.containsKey(acc2)) {
					writer.write(">>>"+ acc2 +"  " + acc2prot.get(acc2) + 
							PubFunc.lineSeparator );
					for(int i = 0; i < m.getNum(); i++) {
						spt = m.readLine(i).split(" +");
						if(spt[0].contains(">>>")) {
							writer.write(spt[0].replace(">>>", "")+ "   ");
						}else
						writer.write(spt[0] + "   ");
					}
					writer.write(PubFunc.lineSeparator);
					writer.flush();
				}
			}
			writer.close();
		}catch(Exception e) {
			System.err.println("method:test(), the outfile is not existed" 
					+ e.getMessage());
		} 
	}
	
	
	public static void main(String args[]) {
		String protPath = "D:\\Softtool\\eclipse_java\\workspace_java\\TetR\\data\\sequence_0";
		String pattern = new File(protPath).getParent() + PubFunc.os + "similarPattern";
		String output = new File(protPath).getParent() + PubFunc.os + "output";
		if(!new File(output).exists()){
			PubFunc.creatFile(output);
		}
		 
		mergeProtAndNucl(protPath, pattern, output);
	}
	
	
	
	
	
	
	
	
	
}
