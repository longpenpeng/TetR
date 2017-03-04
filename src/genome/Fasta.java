package genome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List; 

import io.*;


/**
 * this {@code Fasta} can cache the nucleotide  sequence  and amino acids sequence
 * in  which nucleotides or amino acids are represented using single-letter code
 *
 * <p>
 * the {@code Fasta} always contains two parts: header line  and sequence lines
 * 	header line : ">Sequence_1|xxxxx"
 *   here , in this {@code Fasta}, the "Sequence_1"{@code String} should be 
 *   accession of the sequence. and the "xxxxx", here, it means something 
 *   description words.It is not useless in {@code Fasta}
 *  Sequence lines:
 *   sequence lines is very simply,which contains the nucleotides or amino 
 *   acids sequence
 * 	
 */ 
public class Fasta implements Serializable {

	private static final long serialVersionUID = 9181485198468751150L;
	/** the accession of the sequence*/
	private String acc;
	/**the amino acids or nucleotides sequences*/
	private String seq;
	
	public Fasta(){
		
	} 
	/**
	 *  initialize a Fasta instance
	 * @param acc
	 */
	public Fasta(String acc) {
		this.acc= acc;
	}
	/**
	 *  initialize a Fasta instance 
	 * @param acc
	 * @param seq
	 */
	public Fasta(String acc, String seq) {
		this.acc = acc;
		this.seq = seq;
	}
	//***********************setter************************
	/**
	 * change the sequence of a {@code Fasta} instance
	 * @param seq
	 * @return the resultant sequence  
	 */
	public String setSeq(String seq) {
		return this.seq = seq;
	}
	/**
	 * change the accession of a {@code Fasta} instance
	 * @param acc
	 * @return  the resultant accession
	 */
	public String setAcc(String acc) {
		 return this.acc =acc;
	}
	
	//***********************getter************************ 
	public String getSeq() {
		return this.seq;
	}
	public String getAcc() {
		return this.acc;
	}
	/**
	 * the {@code Module} is a class which can store a module which use a certain
	 * or letter as a start and end. Here ,the {@code Module} should like following:
	 * 	the first line: ">accession|xxxxx"
	 * the subsequent lines are the sequence lines
	 * @param Module 
	 * @return Fasta instance
	 */
	public Fasta(Module m){
		String firstLine = m.readLine(0);
		String line;
		this.acc = firstLine.split("\\|")[0].replace(">", "");
	//	PrintUtils.printStar("first line in Fasta Constructor with parameter Module m :" + firstLine +"  " + acc ); 
		String sequence = "";
		for(int i =1; i < m.getNum(); i++) {
			line = m.readLine(i);
			sequence += line;
		}
		 this.seq =sequence;
		 
	}
	/**
	 * obtain whole {@code Fasta} from a given file 
	 * @param filePath
	 * @return an arrayList which contain fastas
	 */
	public static List<Fasta> getFastasFromFile(String filePath) {
		List<Module> modules = Module.getModules(filePath, ">");
		List<Fasta> fastas = new ArrayList<Fasta>();
		Iterator<Module> iter = modules.iterator();
		while(iter.hasNext()) {
			Module m = iter.next();
			fastas.add(new Fasta(m));
		}
		return fastas;
	}
	public String toString() {
		String line = ">" + acc + PubFunc.lineSeparator + seq;
		return line;
		
	}
}
