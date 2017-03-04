package svm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.sasl.SaslServer;
import javax.xml.crypto.Data;

import io.PubFunc;

public class EncodeManual {
	public static void extractInfo(String file, String out){
		String line = "";
		String id;
		String entry;
		String prot;
		String palin; 
		String index;
		String coreDNA;
		String protVector;
		String dnaVector;
		String vector;
		Map<String, String> entry2protVector = new HashMap<String, String>();
		Map<String, String> entry2DnaVector = new HashMap<String, String>();
		
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			//PrintWriter writer  = new PrintWriter(out);
			PrintWriter writer2 = new PrintWriter(out);
			//writer.write("#code rules: HTH encode from 1 -20; dna: the first char"
		//			+ " in core dna(which should be 1) encodes 32" + PubFunc.lineSeparator);
			while((line = in.readLine()) != null){
				if(line.startsWith("#")){
					continue;
				}
				if (line.trim().length() == 0) {
					continue;
				}
				String[] spt = line.split(" +");
				id = spt[0];
				entry = spt[1];
				prot = spt[2];
				palin = spt[4];
				index = spt[5];
				coreDNA = spt[6];
				protVector =CodeProt.codeProt(prot, entry);
			 	dnaVector = CodeDna.codeDNA(palin, index, coreDNA, entry);
			 	writer2.write(">"+entry + PubFunc.lineSeparator + prot +PubFunc.lineSeparator);
//				entry2protVector.put(entry, protVector);
//				entry2DnaVector.put(entry, dnaVector);
			 	vector = protVector + dnaVector + "# " + entry;
			//	writer.write("+1  " + vector + PubFunc.lineSeparator);
			//	writer.flush(); 
				List<String> negaDnaVector = svm.CodeDna.generateNegative(
						palin, index, coreDNA, entry);
				if(negaDnaVector.size() >3){
					for(int i = 0; i< 4; i++){
						String negavector = negaDnaVector.get(i);
						vector = protVector + "  " + negavector+ "# " + entry;
						//writer.write("-1  " + vector + PubFunc.lineSeparator);
					} 
				}else {
					for(String negavector:negaDnaVector){
						vector = protVector + "  " + negavector+ "# " + entry;
						//writer.write("-1  " + vector + PubFunc.lineSeparator);
					} 
				}
				writer2.flush();
				
				
				//writer.write("+1  " + vector + PubFunc.lineSeparator);
				//writer.flush(); 
			}
//			boolean flag;
//			for(String entryId1:entry2protVector.keySet()){
//				 flag =false;
//				int counter = 0;
//				protVector  = entry2protVector.get(entryId1);
//				for(String entryId2:entry2DnaVector.keySet()){
//					if(entryId1.equals(entryId2)){
//						flag = true;
//						continue;
//					}
//					if(flag == true){
//						dnaVector = entry2DnaVector.get(entryId2);
//						vector = protVector + "  " + dnaVector + "# " + entryId1 + 
//								"  " + entryId2;
//						writer.write("-1  " + vector + PubFunc.lineSeparator);
//						counter ++;
//						if (counter ==3) {
//							break;
//						}
//					}
//					writer.flush();
//					} 
//			}
			in.close();
			writer2.close();
		//	writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

	
	public static void  main(String args[]) {
		String palin = "ttcgtacg";
		String index = "1-3";
		String prot = "TTGEIVKLSESSKGNLYYH";  
		String coreDNA = "cgat";
		String entry = "P0A0N4";
		List<String> list =CodeDna.generateNegative(palin, index, coreDNA, entry);
 
		System.out.println(CodeProt.codeProt(prot, entry));
		
		
		String input = "/home/longpengpeng/refine_TETR/ML/manual_sele/HTH_DNA.txt";
		String out= "/home/longpengpeng/refine_TETR/ML/manual_sele/protSeq";
		extractInfo(input, out);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
