package seqAlign;


public class SecondaryStructureAlignScoreMatrix extends AlignScoreMatrix
{
	private int[][] score;
	private String seqType = "HEC";
	private int gapPenalty;
	private int gapExtension;
	
	public SecondaryStructureAlignScoreMatrix()
	{
		/*score map
		 *      H   E   C
		 *  H   5  -5   0
		 *  E  -5   7  -1
		 *  C   0  -1   4
		 */
		score = new int[3][3];
		score[0][0] = 5;
		score[0][1] = -5;
		score[0][2] = 0;
		score[1][0] = -5;
		score[1][1] = 7;
		score[1][2] = -1;
		score[2][0] = 0;
		score[2][1] = -1;
		score[2][2] = 4;
	}
	
	private int findIndex(char a)
	{
		if(a == 'H') return 0;
		if(a == 'E') return 1;
		if(a == 'C') return 2;
		throw new RuntimeException(a + " is not a secondary structure type");
	}
	
	public int getScore(char a, char b)
	{
		return score[findIndex(a)][findIndex(b)];
	}
	
	public int gapPenalty()
	{
		return this.gapPenalty;
	}
	
	public int gapExtension()
	{
		return this.gapExtension;
	}
}
