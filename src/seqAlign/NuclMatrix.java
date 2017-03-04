package seqAlign;

import java.io.IOException;

public class NuclMatrix extends AlignScoreMatrix {

	
	private int[][] score = new int[5][5];
	private final String nucl = "atcgn";
	private int gapPenalty = 11;
	private int gapExtension = 1;
	
	public NuclMatrix() 
	{
		try {
			int[][] matrix={{5,-4,-4,-4,-4},{-4,5,-4,-4,-4},{-4,-4,5,-4,-4},
					{-4,-4,-4,5,-4},{-4,-4,-4,-4,5}};
			this.score=matrix; 
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		} 
	private int findIndex(char a)
	{
		for(int i=0;i<4;i++)
		{
			if(this.nucl.charAt(i)==a || 
					this.nucl.charAt(i) == Character.toLowerCase(a))
			{
				return i;
			}
		} 
		throw new RuntimeException(a + " is not a residue type");
	}
	 
	public boolean regularChar(char a){
		if(a=='a'||a=='c'||a=='t'||a=='g') 			 
			return false;
		 else if(a=='A'||a=='C'||a=='T'||a=='G')
			return false;
		 else  
			return true; 
	}
	public int getScore(char a,char b)
	{
		//
		if(regularChar(a)||regularChar(b)){ 
			return 0;
		}else{
			int i=findIndex(a);
			int j=findIndex(b);
			
			return this.score[i][j];
		}
		
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
	
	public static void main(String[] args) throws IOException
	{
		NuclMatrix scoreMatrix=new NuclMatrix();
		//int a=scoreMatrix.getScore('a', 'a');
		//System.out.println(a);
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				System.out.println (i+" "+j+" "+scoreMatrix.score[i][j]+"   ");
			}
		
		//	System.out.print("\n");
		}
	}
	


}
