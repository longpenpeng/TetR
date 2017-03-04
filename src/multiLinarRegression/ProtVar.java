package multiLinarRegression;

import seqAlign.Blosum62;

/**
 * {@code ProtVar} ,here VGX is short for protein variational. In other word,
 * it translate the protein dna-binding domain into the observation,and then,
 * combining the RNA identity to do the multiply linear regression  
 * <p>
 * the translation matrix is blosum62
 * @author longpengpeng
 *
 */
public class ProtVar {
	String acc1;
	String acc2;
	private String prot1;
	private String prot2;
	private int[] positionScore;
	private Blosum62 Blosum62 = new Blosum62();
	
	public ProtVar(){
		
	}
	public ProtVar(String prot1, String prot2, String acc1, String acc2) {
		if(prot1.length() != prot2.length()) {
			try {
				throw new Exception("the length of two protein must be same!");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		this.prot1 = prot1;
		this.prot2 = prot2;
		this.acc1 = acc1;
		this.acc2 = acc2;
		this.positionScore = getPositionScore();
	}
	
	public int[] getPositionScore(){
		 int[] positiScore = new int[prot1.length()];
		 char c1;
		 char c2;
		 for(int i = 0; i < prot1.length(); i++) {
			  c1 = prot1.charAt(i);
			  c2 = prot2.charAt(i); 
			  positiScore[i] = Blosum62.getScore(c1, c2);
		 }
		 return positiScore;
			   
	}  
	public String toString(){
		String line = "";
		line += acc1 + "  " + acc2 + "  ";
		for(int i = 0; i < positionScore.length; i++) {
			line += positionScore[i] + "  "; 
		}
		return line;
	}
}
