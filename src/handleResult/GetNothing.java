package handleResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.PrintUtils;
import io.PubFunc;
import search.PalSeq;

public class GetNothing { 
	/**
	 * here the criterion that identified a accession doesn't get a  operator
	 * is 5.Or, if the maximum is less than 5, the accession's result is unsafely
	 * @param maxOut
	 */
	public static void getNoResultAcc(String maxOut,List<String>
			notResAccList, List<String> hasResAccList){
		//Map<String, Integer> acc2MaxOut = new HashMap<String, Integer>();
		String line ="";
		String[] spt;
		PalSeq ps;
		int max ;
		String acc;
		try {
			BufferedReader in  = new BufferedReader(new FileReader(maxOut));
			
			while((line = in.readLine())!=null){
				spt = line.split(" +");
				max = Integer.parseInt(spt[0]);
				ps = new PalSeq(spt[1]);
				acc = ps.getAcc();
				if(max < 5){
					notResAccList.add(acc);
				}else {
					hasResAccList.add(acc);
				}
			}
			in.close();
		} catch (Exception e) {
			 System.err.println(e.getMessage());
		} 
	}
	public static void getNoResult(String maxOut, Map<String, Integer> hasReslut,
			Map<String, Integer> noResult){
		String line ="";
		String[] spt;
		PalSeq ps;
		int max ;
		String acc;
		try {
			BufferedReader in  = new BufferedReader(new FileReader(maxOut));
			
			while((line = in.readLine())!=null){
				spt = line.split(" +");
				max = Integer.parseInt(spt[0]);
				ps = new PalSeq(spt[1]);
				acc = ps.getAcc();
				if(max < 5){
					noResult.put(acc, max);
				}else {
					hasReslut.put(acc, max);
				}
			}
			in.close();
		} catch (Exception e) {
			 System.err.println(e.getMessage());
		} 
	} 
	public static void main(String[] args){
		String maxOut ="/home/longpengpeng/refine_TETR/merge/merge/maxOut";
		String noResultAcc = "/home/longpengpeng/refine_TETR/merge/merge/statistic/noResult";
		String hasResultAcc = "/home/longpengpeng/refine_TETR/merge/merge/statistic/hasResult";
		
		Map<String, Integer> hasReslut = new HashMap<String, Integer>();
		Map<String, Integer> noResult = new HashMap<String, Integer>();
		getNoResult(maxOut, hasReslut, noResult);
		try {
			PrintWriter noResultWriter = new PrintWriter(noResultAcc);
			PrintWriter hasResultWriter = new PrintWriter(hasResultAcc);
			for(String acc:noResult.keySet()){
				noResultWriter.write(acc + "  " +noResult.get(acc) + 
						PubFunc.lineSeparator);
				noResultWriter.flush();
			}
			for(String acc: hasReslut.keySet()){
				hasResultWriter.write(acc + "  " + hasReslut.get(acc ) +
						PubFunc.lineSeparator);
				hasResultWriter.flush();
			}
			 noResultWriter.close();
			 hasResultWriter.close();
		
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

}
