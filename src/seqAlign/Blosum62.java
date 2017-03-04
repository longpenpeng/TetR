package seqAlign; 

import java.io.*; 
import io.*;
 
public class Blosum62 extends AlignScoreMatrix
{
	private int[][] score=new int[24][24];
	private final String aa="ARNDCQEGHILKMFPSTWYVBZX-";
	private int gapPenalty = 11;
	private int gapExtension = 1;
	private final double[] aaProbability={0.0839, 0.01262,0.05929,0.06811,0.04094,
			                              0.0735, 0.02354,0.05755,0.05669,0.094,
			                              0.01668,0.04268,0.04628,0.03748,0.05172,
			                              0.05879,0.05456,0.0713 ,0.0146 ,0.03578};
	public Blosum62()  
	{ 
		String currentClassPath = this.getClass().getResource("").getPath();
	//	System.out.println(currentClassPath);
		String blosumFile = (new File(new File(currentClassPath).getParent()
				).getParent()) + PubFunc.os + "data" + PubFunc.os +"blosum62";
		System.out.println(blosumFile);
		try {
			BufferedReader in = new BufferedReader(new FileReader(blosumFile));
			String line;
			int i=-1;
			while((line=in.readLine())!=null)
			{
				if(line.charAt(0)=='#'||line.charAt(0)==' ')
				{
					continue;
				}
				i++;
				for(int j=0;j<24;j++)
				{
					score[i][j]=Integer.parseInt(line.substring(3*j+2,3*j+4).trim()); 
				}
			}
			in.close();
		} catch (Exception e) {
			System.err.println("the blosum62 file is not existed" + e.getMessage());
		}		
		
	}
	
	private int findIndex(char a)
	{
		for(int i=0;i<24;i++)
		{
			//System.out.println(this.aa.charAt(i) + " " + (a+"").toUpperCase());
			if((this.aa.charAt(i)==a)||
					((this.aa.charAt(i)+"").equals((a+"").toUpperCase())))
			{
				return i;
			}
		}
		throw new RuntimeException(a + " is not a residue type");
	}
	
//	public double distanceToRandom(char aa)
//	{
//		double score=0;
//		for(int j=0;j<20;j++)
//		{
//			char b=ResName.intToSin(j);
//			score+=getScore(aa,b)*aaProbability[j];
//		}
//		return score;
//	}
//	
//	public double distanceToRandom(String seq)
//	{
//		double score=0;
//		for(int i=0;i<seq.length();i++)
//		{
//			score+=distanceToRandom(seq.charAt(i));
//		}
//		return score;
//	}
	
	public int getScore(char a,char b)
	{
		//
		int i=findIndex(a);
		int j=findIndex(b);
		return this.score[i][j];
	}
	
	public int gapPenalty()
	{
		return this.gapPenalty;
	}
	
	public int gapExtension()
	{
		return this.gapExtension;
	}
	
	public int getScore(String a,String b)
	{
		if(a.length()!=b.length())
		{
			throw new RuntimeException(a.length()+","+b.length()+" sequence length not equal");
		}
		int score=0;
		for(int i=0;i<a.length();i++)
		{
			score+=getScore(a.charAt(i),b.charAt(i));
		}
		return score;
	}
	
//	public static void main(String[] args) throws IOException
//	{
//		Blosum62 scoreMatrix=new Blosum62();
//		int a=scoreMatrix.getScore('D', 'I');
//		System.out.println(a);
//		for(int i=0;i<24;i++)
//		{
//			for(int j=0;j<24;j++)
//			{
//				System.out.printf("%2d ",scoreMatrix.score[i][j]);
//			}
//			System.out.print("\n");
//		}
//	}
	
}