package myKMeans;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import genome.Fasta;
import io.PubFunc;
import seqAlign.Blosum62;

/**
 * this class contains some method to generate the matrix which is the input 
 * of cluster progress
 * the matrix contains the identity of each protein sequence
 * @author longpengpeng
 *
 */
public class Preprocess {
	
	public static void main(String[] args){
		String seqPath = args[0];
		String outDir = args[1];
		Preprocess pp = new Preprocess();
		pp.generateIdentMatrix(seqPath, outDir);
	}
	public void generateIdentMatrix(String seqPath, String outDir){
		
		String int2AccOut = outDir + PubFunc.os + "int2Acc";
		String identMatrix = outDir + PubFunc.os + "matrix";
		
		List<Fasta> fastas = Fasta.getFastasFromFile(seqPath);
		Map<Integer, String> int2Acc = new TreeMap<Integer, String>();
		String seqA;
		String seqB;
		double ident;
		Blosum62 bsm = new Blosum62();
		try {
			PrintWriter int2AccWriter = new PrintWriter(int2AccOut);
			for(int i = 1; i < fastas.size() +1; i++){
				int2Acc.put(i, fastas.get(i-1).getAcc());
				int2AccWriter.write(i + "  " + fastas.get(i -1).getAcc() + PubFunc.lineSeparator);
			}
			int2AccWriter.flush();
			int2AccWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("int2Acc file does not exist" + e.getMessage());
		}
		
		try {
			PrintWriter matrixWriter = new PrintWriter(identMatrix);
			
			for(int i = 1;i < fastas.size() +1; i++){ 
				seqA = fastas.get(i-1).getSeq();
				for(int j = i +1; j < fastas.size() +1 ; j++){
					seqB = fastas.get(j-1).getSeq();
					ident = seqAlign.SeqAlign.globalIdentity(seqA, seqB, bsm);
					matrixWriter.write(i + "  " + j + " " + statistic.Distribute.format(ident) + PubFunc.lineSeparator);
					 
				}
					
			}
			matrixWriter.flush();
			matrixWriter.close();
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}
}
