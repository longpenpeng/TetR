package svm;

import java.io.File;
import java.util.Map;

import io.PubFunc;

public class CodeProt {
	public Map<String, String> dict;
	
	
	
	public CodeProt(){
		String currentClassPath = this.getClass().getResource("").getPath();
		//	System.out.println(currentClassPath);
		String dictPath = (new File(new File(currentClassPath).getParent()
					).getParent()) + PubFunc.os + "data" + PubFunc.os +"prot";
		dict = PubFunc.getMapFromFile(dictPath, 1, 2, " +");
		//System.out.println(dict.toString());
	}
	
	
	public Map<String, String> getDict(){
		return dict;
	}
	
	public static String codeProt(String prot, String entry){
		assert (prot.length() < 21): ("the length of HTH shoubld be no more"
				+ " than 20 : " +entry);
		CodeProt cp = new CodeProt();
		Map<String, String> dict = cp.getDict();
		
		String vector ="";
		char c;
		 for(int i = 0; i < prot.length(); i++){
			 c = prot.charAt(i);
			// System.out.println(dict.get(c + ""));
			 vector = vector + (i+1) + ":" + dict.get(c + "") + "  "; 
		 }  
		 return vector;
	}
	public static void main(String args[]) {
		CodeProt cp = new CodeProt();
		
	}
}
