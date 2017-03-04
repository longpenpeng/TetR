package svm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.PubFunc;

/**
 * this class is used to analysis the co-variation of  amino acid and
 *  nucleotide pair in HTH and core dna   
 * @author longpengpeng
 *
 */
public class Covariation {
	public static void preprocess(String origFile,String outPath){
		Map<String, String> acc2DBD = PubFunc.getMapFromFile(origFile,
				2, 3, " +");
		Map<String, String> acc2CoreDNA = PubFunc.getMapFromFile(origFile,
				2, 8, " +");
		try {
			PrintWriter dBDwriter = new PrintWriter(outPath + PubFunc.os +"DBD_seq");
			PrintWriter dnawWriter = new PrintWriter(outPath + PubFunc.os +"DNA_seq");
			for(String acc: acc2DBD.keySet()){
				dBDwriter.write("> " + acc + "\t" + acc2DBD.get(acc) + 
						PubFunc.lineSeparator);
				dBDwriter.flush();
			}
			for(String acc:acc2CoreDNA.keySet()){
				dnawWriter.write("> " + acc + "\t" + acc2CoreDNA.get(acc) +
						PubFunc.lineSeparator);
			}
			dBDwriter.flush();
			dnawWriter.flush();
			dBDwriter.close();
			dnawWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	public static int[][] dnaMotifMatrix(String dna_seq,String dnaMotifOut){
	
		List<String> dnaList = PubFunc.getListFromFile(dna_seq, 3, " +|\t");
		int len = dnaList.get(0).length();
		int size = dnaList.size();
		int dnaMotifMatrix[][] = new int[size][len*5];
		for(String dna:dnaList){
			if(dna.length() != len){
				throw new IllegalAccessError("first dna length is" +len + "  "+
						dna ); 
			} 
		}
		char c;
		int index;
		String dna;
		Map<String, String> dnaDict = (new CodeDna()).getDict();
		for(int i = 0; i<len; i++){
			for(int j = 0; j < size; j++){
				dna = dnaList.get(j);
				c = dna.charAt(i);
				index = Integer.parseInt(dnaDict.get(c + ""));
				dnaMotifMatrix[j][5*i+ index-1] = 1;
			}
		}
		try {
			PrintWriter writer = new PrintWriter(dnaMotifOut);
			writer.write("#2017/2/21 core Dna motif numeric Matrix:row- dna sequence ;"
					+ " column- nucleotide in each position"+ PubFunc.lineSeparator+
					"#n1 n1 n1 n1 n1 n2 n2 n2 n2 n2 (nucleotide in each position)"+
					PubFunc.lineSeparator+ "#A G  C  T  M  A  G  C  T  M  A  G  C  T  M(M means"
							+ " the nucleotide missed)" + PubFunc.lineSeparator);
			for(int i =0; i<dnaMotifMatrix.length; i++){
				for (int j = 0; j < dnaMotifMatrix[i].length; j++) {
					writer.write(dnaMotifMatrix[i][j] + "  ");
				}
				writer.write(PubFunc.lineSeparator);
			} 
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dnaMotifMatrix;
	}
	public static int[][] DBDMotifMatrix(String DBD_seq,String DBDMatrixOut) {
		List<String> DBDList = PubFunc.getListFromFile(DBD_seq, 3, " +|\t");
		int len = DBDList.get(0).length();
		int size = DBDList.size();
		int DBDMotifMatrix[][] = new int[size][len*20];
		for(String dna:DBDList){
			if(dna.length() != len){
				throw new IllegalAccessError("first dna length is" +len + "  "+
						dna );} 
		}
		char c;
		int index;
		String dna;
		Map<String, String> protDict = (new CodeProt()).getDict();
		for(int i = 0; i<len; i++){
			for(int j = 0; j < size; j++){
				dna = DBDList.get(j);
				c = dna.charAt(i);
				index = Integer.parseInt(protDict.get(c + ""));
				DBDMotifMatrix[j][20*i+ index-1] = 1;
			}
		}
		try {
			PrintWriter writer = new PrintWriter(DBDMatrixOut);
			writer.write("#2017/2/21：protein HTH motif numeric Matrix:row- DBD motif "+
					"sequence ; column- amino acid in each position" + PubFunc.lineSeparator
				   + "#aa1 aa1 aa1 aa1  ... aa2 aa2 aa2  ...aaL (amino acid in each position,"
					+"L is the length of DBD motif)" + PubFunc.lineSeparator +"#A C  D  E ...Y"
							+ " A  C  D  E ..Y ...(in each position ,there may be 20 amino acid)"
					+ PubFunc.lineSeparator);
			for(int i =0; i<DBDMotifMatrix.length; i++){
				for (int j = 0; j < DBDMotifMatrix[i].length; j++) {
					writer.write(DBDMotifMatrix[i][j] + "  ");
				}
				writer.write(PubFunc.lineSeparator);
			} 
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DBDMotifMatrix;
	} 
	public static int[][] correlation(int[][] DNAMotifMatrix, int[][] DBDMotifMatrix,
			String out) {
//		int indexInDBD =0;
//		int positionInDBD =0;
//		int indexInDNA =0;
		//System.out.println(DBDMotifMatrix[0].length+ " " +DNAMotifMatrix[0].length);
		int[][] correlationMatrix = new int[DNAMotifMatrix[0].length][DBDMotifMatrix[0].length];
		
		for(int i =0; i<DBDMotifMatrix.length; i++){
			for (int j = 0; j < DBDMotifMatrix[i].length; j++) { 
				for(int m =0; m<DNAMotifMatrix.length; m++){
					for (int n = 0; n < DNAMotifMatrix[i].length; n++) {
						 correlationMatrix[n][j] = correlationMatrix[n][j]+DBDMotifMatrix[i][j]*DNAMotifMatrix[m][n];
					} 
				} 
			}
		} 
		try {
			PrintWriter writer = new PrintWriter(out);
			for(int i = 0; i<correlationMatrix.length; i++){
				for(int j = 0; j<correlationMatrix[i].length; j++){
					writer.write(correlationMatrix[i][j] + "  ");
				}
				writer.write(PubFunc.lineSeparator);
			} 
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return correlationMatrix;
	}
	public static void mutulInfo_2_bg(int[][] correlationMatrix, String mutulOut){
		 
		double nuclBg;
		double aaBg;
		double jointPro;
		int[] aaTotalMatrix =aaTotalMatrix(correlationMatrix);
		int[][] nuclTotalMatrix = nuclTotalMatrix(correlationMatrix);
		double[][] mutulInfoMatrix = new 
				double[correlationMatrix.length][correlationMatrix[0].length]; 
		double[][] nuclbgMatrix = new 
				double[correlationMatrix.length][correlationMatrix[0].length]; 
		for(int i = 0; i < correlationMatrix.length; i ++){
			
			for(int j=0; j< correlationMatrix[i].length; j++){
				nuclBg = backgroudInNuclFreq(nuclTotalMatrix, i);
				
				aaBg = backgroundInDBDFreq(aaTotalMatrix, j);
				jointPro =jointPro(correlationMatrix, i, j); 
//				if(jointPro ==0 && nuclBg*aaBg !=0){
//					System.out.println(correlationMatrix[i][j] +" DDD " +i +" DDD " + j + " ");
//					throw new IllegalAccessError();
//				}
				if(jointPro == 0 || nuclBg*aaBg ==0){
					mutulInfoMatrix[i][j] = 0;
				}else {
					mutulInfoMatrix[i][j] = io.Arith.div(jointPro,nuclBg*aaBg,3);
				}
				
				if(i == 0){
					System.out.println("mutul  " + i + " " + j + "  " + nuclBg  +"  " + aaBg
							+"  " + jointPro+ "  " + mutulInfoMatrix[i][j]);
				}
			}
		}
		try {
			PrintWriter writer = new PrintWriter(mutulOut);
			for(int i=0; i<mutulInfoMatrix.length; i++){
				
				for(int j =0; j< mutulInfoMatrix[0].length; j++){
					writer.write(mutulInfoMatrix[i][j] + " ");
				}
				writer.write(PubFunc.lineSeparator);
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void mutulInfo_1_bg(int[][] correlationMatrix, String mutulOut){
		double nuclBg;
	//	double aaBg;
		double conditionalPro;
		//int[] aaTotalMatrix =aaTotalMatrix(correlationMatrix);
		int[][] nuclTotalMatrix = nuclTotalMatrix(correlationMatrix);
		double[][] mutulInfoMatrix = new 
				double[correlationMatrix.length][correlationMatrix[0].length]; 
//		double[][] nuclbgMatrix = new 
//				double[correlationMatrix.length][correlationMatrix[0].length]; 
		for(int i = 0; i < correlationMatrix.length; i ++){
			
			for(int j=0; j< correlationMatrix[i].length; j++){
				nuclBg = backgroudInNuclFreq(nuclTotalMatrix, i);
				conditionalPro = conditionalPro(correlationMatrix, i, j);
				if (nuclBg == 0) {
					mutulInfoMatrix[i][j] = 0.0; 
				}else {
					//System.out.println(conditionalPro + "  " + nuclBg);
					mutulInfoMatrix[i][j] = io.Arith.div(conditionalPro, nuclBg, 3);
				} 
				if(i <5&& j < 5){
					System.out.println(i +  "  "  + j + "  " + nuclBg + "  " + conditionalPro);
				}
			}
		}
		try {
			PrintWriter writer = new PrintWriter(mutulOut);
			for(int i=0; i<mutulInfoMatrix.length; i++){
				
				for(int j =0; j< mutulInfoMatrix[0].length; j++){
					writer.write(mutulInfoMatrix[i][j] + " ");
				}
				writer.write(PubFunc.lineSeparator);
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static double conditionalPro(int[][] correlationMatrix, int indexInMatrixInRow, 
			int indexInMatrixColumn ){
		int positionInDBD = positionInDBD(indexInMatrixColumn);
		int startIndexInCorrelationColumn =  (positionInDBD-1)*20  ;
		int endIndexInCorrelationColumn = positionInDBD*20 -1;
		int positionInDNA = positionInDNA(indexInMatrixInRow);
		int startIndexInCorrelationRow = (positionInDNA -1)*5;
		int endIndexInCorrelationRow = positionInDNA*5-1;
		int counter= 0;
		for(int i = startIndexInCorrelationRow; i < endIndexInCorrelationRow +1; i++){
			
			
			counter = counter + correlationMatrix[i][indexInMatrixColumn];
			if (indexInMatrixInRow <5 &&indexInMatrixColumn <5) {
				System.out.print(correlationMatrix[i][indexInMatrixColumn] + "  " +
						counter +"  ");
			}
		}
		if (counter == 0) {
			return 0.0;
		}else {
			return  io.Arith.div(
					correlationMatrix[indexInMatrixInRow][indexInMatrixColumn],counter,3);
		} 
	}
	/**
	 * 
	 * @param correlationMatrix
	 * @param indexInMatrixInRow: row number
	 * @param indexInMatrixColumn: column number
	 * @return
	 */
	public static double jointPro(int[][] correlationMatrix, int indexInMatrixInRow, 
			int indexInMatrixColumn){
		
		int positionInDBD = positionInDBD(indexInMatrixColumn);
		int startIndexInCorrelationColumn =  (positionInDBD-1)*20  ;
		int endIndexInCorrelationColumn = positionInDBD*20 -1;
		int positionInDNA = positionInDNA(indexInMatrixInRow);
		int startIndexInCorrelationRow = (positionInDNA -1)*5;
		int endIndexInCorrelationRow = positionInDNA*5-1;
		
		int counter =0;
		String str = "";
		for(int i = startIndexInCorrelationRow; i < endIndexInCorrelationRow +1;i++){
			
			for(int j = startIndexInCorrelationColumn; j < endIndexInCorrelationColumn +1;j++){
				counter = counter + correlationMatrix[i][j];
				str = str + " " + correlationMatrix[i][j];
			}
		}
//		if(correlationMatrix[indexInMatrixInRow][indexInMatrixInRow] != 0){
//			System.out.println(startIndexInCorrelationColumn + "  " +endIndexInCorrelationColumn
//					+ " "+ startIndexInCorrelationRow + " "+ endIndexInCorrelationRow 
//					+ "  " + counter + "  "+correlationMatrix[indexInMatrixInRow][indexInMatrixInRow] + "  " +io.Arith.div(
//							correlationMatrix[indexInMatrixInRow][indexInMatrixInRow],counter,3));
//			System.out.println(str);
//		}
		
		if(counter == 0){
			System.out.println("zero in total");
			return 0;
		}else {
			return  io.Arith.div(
					correlationMatrix[indexInMatrixInRow][indexInMatrixColumn],counter,3);
		}
		
	}
	/**
	 * calculate the background of nucleotide in certain position
	 * @param array
	 * @param indexInMatrixRow
	 * @return
	 */
	public static double backgroudInNuclFreq(int[][] array, int indexInMatrixRow  ) {
		//int sumNucl = sumNuclInCertainLineCertainPoi(array, indexInMatrixRow);
		int counter =0;
		int positionInDNA = positionInDNA(indexInMatrixRow);
		int startIndexInCorrelationMatrix =  (positionInDNA-1)*5  ;
		int endIndexInCorrelationMatrix = positionInDNA*5 -1;
		//System.out.println(startIndexInCorrelationMatrix + "  " + endIndexInCorrelationMatrix);
		for(int i = startIndexInCorrelationMatrix; i < endIndexInCorrelationMatrix +1; i++){
			counter = counter + array[i][0];
		}
		
		double nuclBG =io.Arith.div(array[indexInMatrixRow][0], counter,3);
				//array[indexInMatrixRow][0]/sumNucl;
		return nuclBG; 
	} 
	public  static double backgroundInDBDFreq(int[]array, int indexInMatrixColumn) {
		int counter =0;
		int positionInDNA = positionInDBD(indexInMatrixColumn);
		int startIndexInCorrelationMatrix =  (positionInDNA-1)*20  ;
		int endIndexInCorrelationMatrix = positionInDNA*20 -1;
		//System.out.println(startIndexInCorrelationMatrix + "  " + endIndexInCorrelationMatrix);
		for(int i = startIndexInCorrelationMatrix; i < endIndexInCorrelationMatrix +1; i++){
			counter = counter + array[i];
		}
		double DBDbg = io.Arith.div(array[indexInMatrixColumn], counter,3);//array[indexInMatrixColumn]/counter;
		if(indexInMatrixColumn <10 && array[indexInMatrixColumn]!= 0){
			System.out.println(" bg in dbd: " + array[indexInMatrixColumn] +" "+ counter +
					 " " +indexInMatrixColumn + "  " + DBDbg);
		}
		
		return DBDbg;
	}
	/**
	 * sum the frequency of nucleotides of one position which may contain five nucleotides
	 * @param array
	 * @param indexInMatrixRow:  the row number of query in correlation matrix
	 * @return sum of five nucleotides frequency of certain position
	 */
	public static int sumNuclInCertainLineCertainPoi(int[][] array, int indexInMatrixRow) {
		int counter =0;
		int positionInDNA = positionInDNA(indexInMatrixRow);
		int startIndexInCorrelationMatrix =  (positionInDNA-1)*5  ;
		int endIndexInCorrelationMatrix = positionInDNA*5 -1;
		//System.out.println(startIndexInCorrelationMatrix + "  " + endIndexInCorrelationMatrix);
		for(int i = startIndexInCorrelationMatrix; i < endIndexInCorrelationMatrix +1; i++){
			counter = counter + array[i][0];
		}
		return counter;
	} 
	public static int  sumAAInCertainLineCertainPoi(int[][] array, int indexInMatrixColumn) {
		int counter =0;
		//int positionInDBD = positionInDBD(indexInMatrixColumn);
		int startIndexInCorrelationMatrix =  (indexInMatrixColumn-1)*20 ;
		int endIndexInCorrelationMatrix = indexInMatrixColumn*20 -1; 
		for(int i = startIndexInCorrelationMatrix; i<endIndexInCorrelationMatrix +1; i++){
			counter = counter + array[0][i];
		}
		return counter;
	}
	/**
	 * calculate total amino acids in each position of DNA, simply sum each number
	 * in each column
	 * @param correlationMatrix
	 * @return
	 */
	public static int[] aaTotalMatrix(int[][] correlationMatrix){
		int[] aaTotalMatrix = new int[correlationMatrix[0].length];
		//System.out.println(correlationMatrix.length);
		int counter;
		
		for(int j =0 ; j< correlationMatrix[0].length; j++){
			counter = 0; 
			for(int i = 0; i < correlationMatrix.length; i++){
				counter = counter + correlationMatrix[i][j];
				
			}
			 
			aaTotalMatrix[j] = counter;
			System.out.print(counter + "  ");
		} 
		System.out.println();
		System.out.println(correlationMatrix.length + "  " +correlationMatrix[0].length );
		return aaTotalMatrix;
	}
	/**
	 * calculate total nucleotides in each position of DBD, simply sum each number 
	 * in each row 
	 * @param correlationMatrix
	 * @return nuclTotalMatrix
	 */
	public static int[][] nuclTotalMatrix(int[][] correlationMatrix) {
		int[][] nuclTotalMatrix = new int[correlationMatrix.length][1];
		int counter;
		for(int i = 0; i < correlationMatrix.length; i++){ 
			counter =0;
			for(int j = 0; j < correlationMatrix[0].length; j++){ 
				counter = counter + correlationMatrix[i][j];  
			}
			nuclTotalMatrix[i][0] = counter;
			System.out.print(counter + "  ");
		}
		System.out.println();
		return nuclTotalMatrix; 
	}
	 public static int positionInDNA(int indexInMatrix) {
		 return (int)(indexInMatrix/5) +1; 
	 }
	 public  static int nuclInDNA(int indexInMatrix) {
		return (int)(indexInMatrix%5 +1);
	}
	 public static int positionInDBD(int indexInMatrix) {
		 return (int)(indexInMatrix/20) +1; 
	}
	 public static int aaInDBD(int indexInMatrix) {
		 return (int)(indexInMatrix%20 +1); 
	}
	 
	public  static void main(String args[]) {
		String origFile ="/home/longpengpeng/refine_TETR/ML/manual_sele/HTH_DNA.txt";
		String outPath = "/home/longpengpeng/refine_TETR/ML/covariation"; 
		//preprocess(origFile, outPath);
		String dnaMotifOut = outPath + PubFunc.os + "DNAMatrix";
		String dna_seq = outPath + PubFunc.os + "DNA_seq";
		int[][] DNAMotifMatrix =dnaMotifMatrix(dna_seq, dnaMotifOut);
		String DBD_seq = outPath + PubFunc.os + "DBD_seq";
		String DBDMotifOut = outPath + PubFunc.os + "DBDMotif";
		int[][] DBDMotifMatrix =DBDMotifMatrix(DBD_seq, DBDMotifOut);
		String correlationPath= outPath + PubFunc.os +"correlation";
		int[][] correlationMatrix =correlation(DNAMotifMatrix, DBDMotifMatrix,
				correlationPath);
		String mutulInfo = outPath + PubFunc.os + "mutulInfo_2_bg";
		//mutulInfo(correlationMatrix, mutulInfo);
		//mutulInfo_2_bg(correlationMatrix, mutulInfo);
		
	}
}
