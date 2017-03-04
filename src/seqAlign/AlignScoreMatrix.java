package seqAlign;

public abstract class AlignScoreMatrix {

	private int[][] score;
	private String seqType;
	private int gapPenalty;
	private int gapExtension;
	
	public abstract int getScore(char a,char b);
	
	public abstract int gapPenalty();
	
	public abstract int gapExtension();
	
	
}
