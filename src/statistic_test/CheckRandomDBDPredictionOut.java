package statistic_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.List; 
import java.util.TreeMap; 

import io.PubFunc;

public class CheckRandomDBDPredictionOut {
	public static void main(String[] args){
		String path = "/home/longpengpeng/refine_TETR/hypothesis";
		//check(path);
		String outDir = "/home/longpengpeng/refine_TETR";
		TreeMap<Integer, List<Integer>> scoreMap = new TreeMap<Integer, List<Integer>>();
		getScoreList(path, scoreMap);
		System.out.println(scoreMap.size());
		double confidence = 0.999;
		List<Integer> list = new ArrayList<Integer>();
		getCertainNumWithConfidence(confidence, scoreMap, list, outDir);
	}
	public static void check(String path){ 
		
		List<String> sizeList = new ArrayList<String>()	; 
		String maxOut = ""; 
		int counter = 0;
		for(String dir: new File(path).list()){ 
			for(String sizeRange:new File(path + PubFunc.os+ dir).list()){ 
				for(String size:new File(path + PubFunc.os+ dir + PubFunc.os +
						sizeRange ).list()){
					
					if(size.equals("maxOut")){
						maxOut = path + PubFunc.os+ dir + PubFunc.os+sizeRange+ 
								PubFunc.os + size;
						System.out.println(maxOut);
						counter ++;
						if(checkLines(maxOut)){
							continue;
						}else {
							sizeList.add(sizeRange); 
							
					    }
					}else {
						continue;
					}  
				} 
			 } 
		}
		System.out.println(sizeList.size() );
		System.out.println(sizeList);
		System.out.println(counter);
	} 
	public  static boolean checkLines(String path) {
		String line = "";
		int size = 1000;
		int counter = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			
			while((line = in.readLine())!= null){
				if(line.trim().length() != 0){
					counter ++;
				}else {
					continue;
				}
			}
			in.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		if(counter != size){
			return false;
		}else {
			return true;
		}
	}
	public static void getScoreList(String path, TreeMap<Integer, List<Integer>>
			scoreMap){
	 	String maxOut = "";
	 	List<Integer> scoreList;
	 	List<String> list; 
	 	
		for(String dir: new File(path).list()){ 
			for(String sizeRange:new File(path + PubFunc.os+ dir).list()){ 
				for(String size:new File(path + PubFunc.os+ dir + PubFunc.os +
						sizeRange ).list()){
					
					if(size.equals("maxOut")){
						scoreList = new ArrayList<Integer>();
						maxOut = path + PubFunc.os+ dir + PubFunc.os+sizeRange+ 
								PubFunc.os + size;
						list = PubFunc.getListFromFile(maxOut, 1, " +");
						for(String score: list){
							scoreList.add(Integer.parseInt(score));
						}
						scoreMap.put(Integer.parseInt(sizeRange), scoreList);
					}
				}
			}
		}
		
	}
	public static void getCertainNumWithConfidence(double confidence, 
			TreeMap<Integer, List<Integer>>scoreMap, List<Integer> list,
			String outDir){
		List<Integer> scoreList;
		int counter = 0;
		int num = (int)(confidence *1000); 
		int upper;
		try {
			PrintWriter writer = new PrintWriter(outDir + PubFunc.os + confidence);
			for(int size: scoreMap.keySet()){
				counter ++;
				scoreList = scoreMap.get(size);
				Collections.sort(scoreList);  
				if(scoreList.size() != 1000){
					throw new IllegalArgumentException("the size of scoreList "
							+ "should be 1000" + "  " + size);
				}
				upper = scoreList.get(num-1);
				writer.write(size + "  " +upper + PubFunc.lineSeparator);
				writer.flush();
				list.add(upper);  
			} 
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println(list);
		System.out.println(list.size());
	}

}
