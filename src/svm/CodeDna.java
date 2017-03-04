package svm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.PubFunc;

public class CodeDna {
	public Map<String, String> dict;
	
	public CodeDna(){
		String currentClassPath = this.getClass().getResource("").getPath();
		//	System.out.println(currentClassPath);
		String dictPath = (new File(new File(currentClassPath).getParent()
					).getParent()) + PubFunc.os + "data" + PubFunc.os +"dna";
		dict = PubFunc.getMapFromFile(dictPath, 1, 2, " +");
	}
	
	public Map<String, String> getDict(){
		return dict;
	}
	
	
	public  static String codeDNA(String palin, String index, 
			String coreDNA,String entry) { 
		
		int start = Integer.parseInt(index.substring(0,index.indexOf("-")));
		int end = Integer.parseInt(index.substring(index.indexOf("-")+1,
				index.indexOf("-") +2));
		assert (palin.substring(start, end).equals(coreDNA)) :("the index of "
				+ "core dna is wrong" +entry);
		char c;
		CodeDna cd = new CodeDna();
		Map<String, String> dict = cd.getDict();
		String dnaVector ="";
		int counter =0;
		if(start >1){
			for(int i = start -2; i >0; i--){
			//for(int i = 0; i < start -2 ; i++){
				c = palin.charAt(i);
				dnaVector =   (31 -counter) + ":" + dict.get(c + "") + "  "
				+dnaVector;
				counter ++;
				
			}
			for(int i = start-1; i<palin.length(); i++){
				c = palin.charAt(i);
				
				dnaVector = dnaVector +(32 - start +1 +i) + ":" + dict.get(c + "")
				+ "  ";
				
			}
		}else if(start == 1) {
			for( int i = 0; i < palin.length(); i++){
				c = palin.charAt(i);
				
				dnaVector = dnaVector +(32  +i) + ":" + dict.get(c + "") + "  ";
			}
			
		}else {
			throw new IllegalAccessError("the index of first char in core dna "
					+ "should large than 0");
		}
//		System.out.println(palin + "  " + index + "  " + coreDNA +
//				PubFunc.lineSeparator + dnaVector);;
		return dnaVector;
		
	}
	public static List<String> generateNegative(String palin, String index, 
			String coreDNA,String entry){
		int start_true = Integer.parseInt(index.substring(0,index.indexOf("-")));
		int end = Integer.parseInt(index.substring(index.indexOf("-")+1,
				index.indexOf("-") +2));
		assert (palin.substring(start_true, end).equals(coreDNA)) :("the index of "
				+ "core dna is wrong" +entry);
		
		CodeDna cd = new CodeDna();
		Map<String, String> dict = cd.getDict(); 
		
		int start;
		char c;
		List<String> negaVectorList = new ArrayList<String>();
		for(int j = 0; j < palin.length(); j++){
			int counter =0;
			String dnaVector =""; 
			if(j == start_true -1){
				counter ++;
				continue;
			}
			start = j +1;
			System.out.print (start + "  ");
			if(start >1){
				
				for(int i = start -1; i > -1; i--){
				//for(int i = 0; i < start -2 ; i++){
					c = palin.charAt(i);
					System.out.print(i + "  " +c + "  ");
					dnaVector =   (32 -counter) + ":" + dict.get(c + "") + "  "
					+dnaVector; 
					counter ++; 
				} 
				if(32- counter <20){
					break;
				}
				for(int i = start; i<palin.length(); i++){
					c = palin.charAt(i); 
					dnaVector = dnaVector +(32 - start +1 +i) + ":" + dict.get(c + "")
					+ "  "; 
				}
				System.out.println(dnaVector);
				negaVectorList.add(dnaVector);
			}else if(start == 1) {
				for( int i = 0; i < palin.length(); i++){
					c = palin.charAt(i); 
					dnaVector = dnaVector +(32  +i) + ":" + dict.get(c + "") + "  ";
				}
				System.out.println(dnaVector);
				negaVectorList.add(dnaVector);
			}else {
				throw new IllegalAccessError("the index of first char in core dna "
						+ "should large than 0");
			}
		} 
		return negaVectorList;
	}
	public  static void main(String args[]) {
		String str = "agctagct";
		System.out.println(str.charAt(1) + " ");
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
