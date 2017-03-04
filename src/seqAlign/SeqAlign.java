package seqAlign;
import java.io.*; 
import java.util.*;


class ScoreUnit
{
	public int score;
	public char pre;
	public ScoreUnit()
	{
		this.score = 0;
		this.pre = 'x';
	}
	public ScoreUnit(int s,char p)
	{
		this.score = s;
		this.pre = p;
	}
}

public class SeqAlign 
{

	public static String[] NeedlemanWunsch(String seqA, String seqB, AlignScoreMatrix asm) throws IOException
	{

//		Blosum62 blosum62 = new Blosum62();
		ScoreUnit[][] matrix = new ScoreUnit[seqA.length()+1][seqB.length()+1];
		matrix[0][0] = new ScoreUnit(0,'x');
		for(int i=1;i<seqA.length()+1;i++)
		{
			matrix[i][0] = new ScoreUnit(1-asm.gapPenalty()-i,'u');
		}
		for(int j=1;j<seqB.length()+1;j++)
		{
			matrix[0][j] = new ScoreUnit(1-asm.gapPenalty()-j,'l');
		}
		for(int i=1;i<seqA.length()+1;i++)
		{
			for(int j=1;j<seqB.length()+1;j++)
			{
				int su;
				int sl;
				int sx;
				if(matrix[i-1][j].pre == 'u')
					su = matrix[i-1][j].score-asm.gapExtension();
				else 
					su = matrix[i-1][j].score-asm.gapPenalty();
	
				if(matrix[i][j-1].pre == 'l')
					sl = matrix[i][j-1].score-asm.gapExtension();
				else
					sl = matrix[i][j-1].score-asm.gapPenalty();
				
				sx = matrix[i-1][j-1].score + asm.getScore(seqA.charAt(i-1), seqB.charAt(j-1));
				
				if(sx >= su && sx >= sl)
					matrix[i][j] = new ScoreUnit(sx,'x');
				else if(su >=sx && su >= sl)
					matrix[i][j] = new ScoreUnit(su,'u');
				else 
					matrix[i][j] = new ScoreUnit(sl,'l');
			}
		}
/*		if(seqA.length() > 22 && seqB.length() > 22)
		for(int i=seqA.length()-5;i<seqA.length()+1;i++)
		{
			for(int j=seqB.length()-50;j<seqB.length()+1;j++)
	//		for(int j=760;j<780;j++)
			{
				System.out.printf("%4d%c ",matrix[i][j].score,matrix[i][j].pre);
			}
			System.out.println();
		}
*/		
		int i=seqA.length();
		int j=seqB.length();
		String alignA = "";
		String alignB = "";
		
		while(i>0 || j>0)
		{
			if(matrix[i][j].pre == 'x')
			{
				alignA = seqA.charAt(i-1) + alignA;
				alignB = seqB.charAt(j-1) + alignB;
				i--;
				j--;
			}
			else if(matrix[i][j].pre == 'l')
			{
				alignA = "-" + alignA;
				alignB = seqB.charAt(j-1) + alignB;
				j--;
			}
			else 
			{
				alignA = seqA.charAt(i-1) + alignA;
				alignB = "-" + alignB;
				i--;
			}
		}
		
		String[] seqAlign = new String[2];
		seqAlign[0] = alignA;
		seqAlign[1] = alignB;
//		System.out.println("\n"+alignA+"\n"+alignB);
		return seqAlign;	
	}
	
	public static void printMatrix(ScoreUnit[][] matrix)
	{
		int alen = matrix.length;
		int blen = matrix[0].length;
		for(int i=0;i<alen;i++)
		{
			for(int j=0;j<blen;j++)
			{
				System.out.printf("%3d ",matrix[i][j].score);
			}
			System.out.println();
		}
	}
	
	public static String[] SmithWaterman(String seqA, String seqB,AlignScoreMatrix asm) throws IOException
	{
//		Blosum62 blosum62 = new Blosum62();
		ScoreUnit[][] matrix = new ScoreUnit[seqA.length()+1][seqB.length()+1];
		matrix[0][0] = new ScoreUnit(0,'o');
		for(int i=1;i<seqA.length()+1;i++)
		{
			matrix[i][0] = new ScoreUnit(0,'o');
		}
		for(int j=1;j<seqB.length()+1;j++)
		{
			matrix[0][j] = new ScoreUnit(0,'o');
		}
		for(int i=1;i<seqA.length()+1;i++)
		{
			for(int j=1;j<seqB.length()+1;j++)
			{
				int su;
				int sl;
				int sx;
				if(matrix[i-1][j].pre == 'u')
					su = matrix[i-1][j].score-asm.gapExtension();
				else 
					su = matrix[i-1][j].score-asm.gapPenalty();
	
				if(matrix[i][j-1].pre == 'l')
					sl = matrix[i][j-1].score-asm.gapExtension();
				else
					sl = matrix[i][j-1].score-asm.gapPenalty();
				
				sx = matrix[i-1][j-1].score + asm.getScore(seqA.charAt(i-1), seqB.charAt(j-1));
				
				if(sx >= su && sx >= sl && sx > 0)
					matrix[i][j] = new ScoreUnit(sx,'x');
				else if(su >= sx && su >= sl && su > 0)
					matrix[i][j] = new ScoreUnit(su,'u');
				else if(sl >= sx && sl >= su && sl > 0)
					matrix[i][j] = new ScoreUnit(sl,'l');
				else 
					matrix[i][j] = new ScoreUnit(0,'o');
			}
		}
/*		
		System.out.println("waterman");
		for(int i=0;i<seqA.length()+1;i++)
		{
			for(int j=0;j<seqB.length()+1;j++)
			{
				System.out.printf("%4d%c ",matrix[i][j].score,matrix[i][j].pre);
			}
			System.out.println();
		}
*/		
		int endI=0;
		int endJ=0;
		int maxScore = 0;
		for(int i=1;i<seqA.length()+1;i++)
		for(int j=1;j<seqB.length()+1;j++)
		{
			if(matrix[i][j].score > maxScore)
			{
				maxScore = matrix[i][j].score;
				endI = i;
				endJ = j;
			}
		}
		
		int i=endI;
		int j=endJ;
		String alignA = "";
		String alignB = "";
		
		while(matrix[i][j].score > 0)
		{
			if(matrix[i][j].pre == 'x')
			{
				alignA = seqA.charAt(i-1) + alignA;
				alignB = seqB.charAt(j-1) + alignB;
				i--;
				j--;
			}
			else if(matrix[i][j].pre == 'l')
			{
				alignA = "-" + alignA;
				alignB = seqB.charAt(j-1) + alignB;
				j--;
			}
			else 
			{
				alignA = seqA.charAt(i-1) + alignA;
				alignB = "-" + alignB;
				i--;
			}
		}
		
		String[] seqAlign = new String[2];
		seqAlign[0] = alignA;
		seqAlign[1] = alignB;
		return seqAlign;	
	}
	
	public static double simpleIdentity(String seqA, String seqB)
	{
		if(seqA.length() != seqB.length())
			throw new RuntimeException("seqLength not equal");
		int samNum=0;
		for(int i=0;i<seqA.length();i++)
		{
			if(seqA.charAt(i) == seqB.charAt(i))
				samNum ++;
		}
		return 1.0*samNum/seqA.length();
	}
	
	public static double similarity(String mutSeq, String natSeq) throws IOException
	{
		if(mutSeq.length() != natSeq.length())
			throw new RuntimeException("seqLength not equal");
		Blosum62 bl = new Blosum62();
		double total = 0.0;
		double align = 0.0;
		for(int i=0;i<mutSeq.length();i++)
		{
			total += bl.getScore(natSeq.charAt(i), natSeq.charAt(i));
			align += bl.getScore(mutSeq.charAt(i), natSeq.charAt(i));
		}
		return align/total;
	}
	
	public static double globalIdentity(String seqA, String seqB,AlignScoreMatrix asm) throws IOException
	{
		String[] align = NeedlemanWunsch(seqA,seqB,asm);
		return simpleIdentity(align[0],align[1]);
	}
	
	public static double localIdentity(String seqA, String seqB,AlignScoreMatrix asm) throws IOException
	{
		String[] align = SmithWaterman(seqA,seqB,asm);
		int minLength = seqA.length();
		if(seqA.length()>seqB.length())
			minLength = seqB.length();
		return simpleIdentity(align[0],align[1])*align[0].length()/minLength;
	}
	
	
	
	
	public static void main(String[] args) throws IOException
	{
		String seqA = "EALIQLLSEDKFENISISKLCKRAGINRGTFYLHYEDKYQMID";
		String seqB = "EALIQLLSEDKFENISISKLCKRAGINRYLHYEDKYQMID";
		//String seqB = "IIAGAAARIRTHGVAATTLDDIREQTATSKSQLFHYFPgGREDLLLAV";
		String seqC = "gtggccggaccc"; 
		String seqD = "gcacacgacccgtgc";
		
		Blosum62 blosum62 = new Blosum62();
		NuclMatrix ncm = new NuclMatrix();
		String[] align1 = NeedlemanWunsch(seqA,seqB,blosum62);
		String[] align2 = SmithWaterman(seqC, seqD, ncm);
		System.out.println(align2[0]+"  " +align2[1]);
		System.out.println(globalIdentity(seqA,seqB,blosum62));
		System.out.println(localIdentity(seqC, seqD, ncm));
	}
}
