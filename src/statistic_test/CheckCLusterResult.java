package statistic_test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.PubFunc; 

public class CheckCLusterResult {
	
	
	public static void main(String[] args){
		String clusterPath = "/home/longpengpeng/refine_TETR/test/test_scps/test_out/result_80";
		String similMatrix = "/home/longpengpeng/refine_TETR/test/test_scps/test_out/newMatrix.sim";
		
		Map<Integer, ArrayList<String>> num2List = new HashMap<Integer, ArrayList<String>>();
		double[][] matrix = new double[4087][4087];
		readCluster(clusterPath, num2List);
		readMatrix(similMatrix, matrix);
		ArrayList<String> accList;
		String path;
		for(int num:num2List.keySet()){
			accList = num2List.get(num);
			path = "/home/longpengpeng/refine_TETR/test/test_scps/test_out/" + num;
			double min = extractSimiOfCluster(accList, matrix,path);
			System.out.println(num + "   " + min);
		}
		
		
	}
	
	public  static void readCluster(String clusterPath, Map<Integer, ArrayList<String>> num2List) {
		ArrayList<Integer> clusterNum = new ArrayList<Integer>();
		//Map<Integer, ArrayList<String>> num2List = new HashMap<Integer, ArrayList<String>>();
		ArrayList<String> list;
		String line = "";
		int cluster = 0;
		String acc = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(clusterPath));
			while((line = in.readLine())!= null){
				if(line.trim().length() <1){
					continue;
				}
				if(line.startsWith("%")){
					continue;
				}
				String[] spt = line.split("\t");
				cluster = Integer.parseInt(spt[1]);
				acc = spt[0];
				if(!clusterNum.contains(cluster)){
					clusterNum.add(cluster);
					list = new ArrayList<String>();
					list.add(acc);
					num2List.put(cluster, list);
				}else {
					num2List.get(cluster).add(acc);
				} 
			}
			in.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static void readMatrix(String similMatrix, double[][] matrix){
		//ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
		//double[][] matrix = new double[4086][4086];
		try {
			BufferedReader in = new BufferedReader(new FileReader(similMatrix));
			String line = "";
			while((line = in.readLine())!= null){
				String[] spt = line.split(" +");
				int a = Integer.parseInt(spt[0]);
				int b = Integer.parseInt(spt[1]);
				double simil = Double.parseDouble(spt[2]);
				matrix[a][b] = simil;
			}
				
		} catch (IOException e) {
			 e.printStackTrace();
		}
		
	}
	public static double extractSimiOfCluster(ArrayList<String> list, double[][] matrix,String path){
		double simi;
		ArrayList<Double> simiList = new ArrayList<Double>();
		//System.out.println(list.size());
		try {
			PrintWriter writer = new PrintWriter(path);
			for(int i = 0; i<list.size(); i++){
				int a = Integer.parseInt(list.get(i));
				for(int j = i+1; j < list.size(); j++){
					int b = Integer.parseInt(list.get(j));
					if(a > b ){
						simi = matrix[b][a];
						writer.write(b + "  " + a);
					}else if(a==b){
					//	System.out.println("?????????????????????  "+a +"  " + b);
						continue;
					}else {
						simi = matrix[a][b]; 
						writer.write(a + "  " +b);
					} 
					simiList.add(simi);
					writer.write("   " + simi + PubFunc.lineSeparator);
					writer.flush(); 
				}
			}
			Collections.sort(simiList);
			for(double s:simiList){
				writer.write(s + PubFunc.lineSeparator);
				writer.flush();
			}
			writer.close();
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		//System.out.println(simiList.size());
		if(simiList.size() ==0){
			return 0;
		}else {
			return simiList.get(0);
		}
		
	}

}
