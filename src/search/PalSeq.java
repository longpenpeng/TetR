package search;

import io.Module;

/**
 * the {@code PalSeq} class cache a short palindromic sequence contained in  
 * file created by the program Palindrome. 
 * @author Administrator
 *
 */
public class PalSeq {
	/** file name is the name of the nucleotide sequence file*/
	public String acc;//fileName == entry name
	/**whole length of palindromic sequence,and the gap sequence is marked as
	 *  upper characters
	 */
	public String wholeSeq; 
	/**the fragment before gap*/
	public String froPart;
	/**the fragment behind gap*/
	public String endPart; 
	/** the index of the first character of  palindromic sequence in the genome sequence*/ 
    public int start;
    /**the index of last character of  palindromic sequence in the genome sequence*/
    public int end;
    /**the gap number of the palindromic sequence*/
    public int gap;
    public int dissimilar;
    /**the entropy value of sequence (it may be useless)*/ 
    public String entropy;
    /**identity between this nucleotide sequence and query nucleotide sequence*/
    public String ident;
    /**original nucleotide sequence ,which is the input of palindrome program */
    public String originalSeq;
    
    public String lowerSeq;
    
    public PalSeq(){
    	
    }
    /**
     * Initialize a {@code PalSeq}
     * <p>
     * the original format of one palindromic sequence :
     *   	 1        ggcgga        6
     *    			  |  |||
	 *		15        cgccct       10
	 *	here ,it is necessary to convert this sequence into our format:
	 *    "ggcggannntcccgc>1...15|ACC|identity|entropy|gap"
	 *    the identity, entropy, are features measured the original nucleotide
	 *    sequence;And the input {@code Module} contains the original palindromic
	 *    sequence
     * @param module
     * @param palName
     * @param entropy
     * @param ident
     * @param upstream
     */
    public PalSeq(Module module,String palName,String ident,int dissimilar
    		,String upstream,String accession ){
        
    	this.dissimilar = dissimilar;
    	this.ident=ident; 
    	originalSeq = upstream;
    	String gapSeq="";
    	String firstLine=module.readLine(0); 
    	String[] spt0=firstLine.split(" +");
    	int start0=Integer.parseInt(spt0[0]);
    	int end0=Integer.parseInt(spt0[2]);
    	
    	String thrLine=module.readLine(2);
    	String[] spt2=thrLine.split(" +");
    	int start2=Integer.parseInt(spt2[0]);
    	int end2=Integer.parseInt(spt2[2]);
    	
    	this.start=start0;
    	this.end=start2;
    	int gapNum=end2-end0;
    	this.gap=gapNum-1; 
    	String endString="";
    	for(int i=spt2[1].length();i>0;i--){
    		endString=endString+spt2[1].substring(i-1,i);
    	}
    
    	this.froPart=spt0[1]; 
    	this.endPart=endString;
    	this.acc=palName;
    	 
    	lowerSeq = originalSeq.substring(start-1, end); 
    	gapSeq = lowerSeq.substring(froPart.length(), froPart.length() + gapNum-1);
     
    	this.wholeSeq = froPart;
    	for(int i = 0; i < gapSeq.length(); i++) {
    		wholeSeq += Character.toUpperCase(gapSeq.charAt(i));
    	}
    	this.wholeSeq += endPart;
    } 
    /**
     *this constructor, the input {@code String} line is like following:
     *	"ggcggannntcccgc>1...15|ACC|identity|entropy|gap";
     *this constructor can convert this string  into a {@code Palindrome} instance
     * @param line
     */
    public PalSeq(String line){ 
    	 
    	String[] spt = line.split(">|\\|"); 
    	this.start = Integer.parseInt(spt[1].substring(0 ,spt[1].indexOf(".")));
    	this.end = Integer.parseInt(spt[1].substring(spt[1].lastIndexOf(".") + 1 ,
    				     spt[1].length()));
    	this.acc = spt[2];
    	this.ident = spt[3];
    	this.gap = Integer.parseInt(spt[4]); 
    	this.dissimilar = Integer.parseInt(spt[5]);
    	this.entropy = spt[6];
    	int halfLength = (spt[0].length() -gap)/2;
    	this.froPart = spt[0].substring(0, halfLength);
    	this.endPart = spt[0].substring(halfLength + gap +1, spt[0].length());
    	this.wholeSeq = spt[0];
 
    }
    public String getLower(){
    	return this.lowerSeq;
    }
    public String getWholeSeq(){
    	return this.wholeSeq;
    }
    public String getseq(){
    	return this.wholeSeq;
    } 
    
    public int getEnd() {
		return this.end;
	}
    public String getEndPart() {
		return this.endPart;
	}
    public String getEntropy(){
    	return this.entropy;
    }  
    public int getStart(){
    	return this.start;
    }
    public String getFroPart() {
    	return this.froPart;
	}
    public String getAcc(){
    	return this.acc;
    }
    public String getOriginal(){
    	return this.originalSeq;
    }
    public int	getGapNum() {
		return this.gap;
	}
    
    public String frontUpstream(int length){
    	String frontSeq;
    	int start=this.getStart();
    	if(start-length<0) 
			frontSeq= this.originalSeq.substring(0,start-1);
		else  
			frontSeq=this.originalSeq.substring(start-length,start-1);
	    
    	return frontSeq;
    }
    public String endUpstream(int length) {
    	String endSeq;
    	int end = this.getEnd();
    	if(end + length > this.originalSeq.length())  
    		endSeq = this.originalSeq.substring(end, this.originalSeq.length());
    	else
    		endSeq = this.originalSeq.substring(end, end + length);
    	return endSeq;
    	 
    }
    @Override
    public String toString(){
    	
    	String line=wholeSeq + ">" + start+ ".." + end + "|" +acc + "|" + 
    			ident+"|"+gap+"|"+ dissimilar+"|" + entropy + "|"+ 0;
    	return line;
    }
    
    public static void main(String[] args) {
    	String line ="tgaggGcgtga>57..67|A0A0A0BLV2|36.364|1|6|null";
    	PalSeq pl = new PalSeq(line);
    	System.out.println(pl.toString());
    }
 
}
