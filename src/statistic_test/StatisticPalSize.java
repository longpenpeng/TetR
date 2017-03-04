package statistic_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.Module;
import io.PubFunc; 



public class StatisticPalSize {
	
	public static void main(String[] args){
		String dir = args[0];
		//"/home/longpengpeng/refine_TETR/prediction/15000_3";
					//"/home/longpengpeng/refine_TETR/prediction"; //args[0]
		String size2ScoreTable = //"/home/longpengpeng/refine_TETR/0.9(10%)";//;
									args[1];
		String outDir = //"/home/longpengpeng/refine_TETR/prediction/out";//
						args[2];
		String unRunOut = outDir + PubFunc.os +"unRunout";
		String unSearchedSizeOut = outDir + PubFunc.os + "unSearchedSizeOut";
		String maxOutPath = outDir + PubFunc.os + "maxOut";
		String out = outDir + PubFunc.os + "out";
		HashMap<Integer, Integer> size2Score = getMap(size2ScoreTable);
		List<String> unRunedAcc = new ArrayList<String>();
		List<Integer> sizeList = new ArrayList<Integer>(); 
		int scoreInMap; 
		int counter = 0;
		//System.out.println(size2Score.size());
		try {
			String maxOut = "";
			String maxLine;
			String palDir = "";
			int palSize = 0;
			String testDir = ""; 
			PrintWriter writer = new PrintWriter(out);
			PrintWriter unRunWriter = new PrintWriter(unRunOut);
			PrintWriter maxWriter = new PrintWriter(maxOutPath);
			PrintWriter unSearchedSizeWriter = new PrintWriter(unSearchedSizeOut);
			for(String dirName:new File(dir).list()){
				System.out.println(dirName);
				//testDir = dir + PubFunc.os + "test_program";
				testDir = dir + PubFunc.os +dirName+ PubFunc.os + "test_program";
				for(String acc:new File(testDir).list()){
					counter ++;
//					System.out.println(acc +"  " + new File(testDir).list().length +" "
//							+new File(dir).list().length);
					palDir = testDir + PubFunc.os + acc + PubFunc.os + "out" + 
							PubFunc.os + "Palindrome" + PubFunc.os + "pal.pal";
					maxOut = testDir + PubFunc.os + acc + PubFunc.os + "out" + 
							PubFunc.os + "Palindrome" + PubFunc.os + "counter";
					//System.out.println(maxOut);// + palDir);
					if(new File(palDir).exists() && new File(maxOut).exists()){ 
						
						palSize = palsize(palDir); 
						//System.out.println(acc +  " " + palDir + "  "  + palSize);
						if(size2Score.containsKey(palSize -1)){ 
							
							scoreInMap = size2Score.get(palSize -1); 
							//System.out.println("QQQQQQQQQQQQQQQQQQQQQQ " + scoreInMap);
							maxLine = checkMaxOut(maxOut, scoreInMap);
							if(maxLine.equals("null")){
								continue;
							}else {
								maxWriter.write(maxLine + PubFunc.lineSeparator); 
								maxWriter.flush();
							}
							
						}else {
							unSearchedSizeWriter.write(palSize + "  " + acc+ PubFunc.lineSeparator);
							unSearchedSizeWriter.flush();
						}
						
						writer.write(acc + "  " + palSize + PubFunc.lineSeparator); 
						writer.flush();
					}else {
						unRunedAcc.add(acc);
						unRunWriter.write(acc + PubFunc.lineSeparator);
						unRunWriter.flush();
						//continue;
					}
				}  
			}
			writer.write(counter);
			writer.flush();
			writer.close();
			maxWriter.close();
			unSearchedSizeWriter.close();
			unRunWriter.flush();
			unRunWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//System.out.println(sizeList.size()); 
	}
	  
	public static int palsize(String path){
		List<Module> modules = Module.getModules(path, "Palindromes of:");
		return modules.size();
	}
	
	public static String checkMaxOut (String maxOut, int num){
		String line ="";
		int counter = 0;
		String max ="";
		List<String> list = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(maxOut)); 
			while((line = in.readLine()) != null && line.trim().length() >0){
				counter ++; 
				max = line.split(" +")[0];
				list.add(line);
			}
			
			in.close();
		} catch (IOException e) {
			 e.printStackTrace();
		}
		 
		//System.out.println(line + "  ////////// " +max + "   " + num + "  " +counter);
		if(counter >0 && Integer.parseInt(max) >= num){
			line = list.get(list.size() -1);
		//	System.out.println(num + " /// " + line);
			return line;
		}else {
			return "null";
		}
		
	} 
	public static HashMap<Integer, Integer>  getMap(String tablePath) {
		String line = "";
		String[] spt;
		HashMap<Integer, Integer> scoreMap = new HashMap<Integer, Integer>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(tablePath));
			while((line = in.readLine())!= null){
				spt = line.split(" +");
				scoreMap.put(Integer.parseInt(spt[0]), Integer.parseInt(spt[1]));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scoreMap; 
	}
}
