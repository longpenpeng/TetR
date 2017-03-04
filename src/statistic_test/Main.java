package statistic_test;
 
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import genome.MyFasta;
import io.PubFunc;
import runOutProgram.AlignResult;

public class Main {
	
	public static void main(String[] args){
		String wholeUpstream = args[0];
		String alignOutDir = args[1];
		//String query = args[2];
		String upstreamOutDir = args[2];
		int setSize = Integer.parseInt(args[3]);
		String setSizeError = args[4];
		
		int brEnd = 42;
		int brStart = 20;
		int cutoff = 10;
		//int cutoff =10;
		List<MyFasta> upstreamList = MyFasta.getFastasFromFile(wholeUpstream, ">");
		Map<String, MyFasta> acc2Upstream = new HashMap<String, MyFasta>();
		for(MyFasta myFasta:upstreamList){
			acc2Upstream.put(myFasta.getAcc(), myFasta);
		}
		String alignOut;
		String upstreamOut;
		String query;
		for(String fileName:new File(alignOutDir).list()){
			alignOut = alignOutDir + PubFunc.os + fileName;
			upstreamOut = upstreamOutDir + PubFunc.os + fileName;
			query = fileName.substring(0, fileName.indexOf("_"));
			selectDissimiar(alignOut, brStart, brEnd, cutoff, query, setSize, 
					acc2Upstream, upstreamOut, setSizeError);
		} 
	}
	public static void selectDissimiar(String alignOut,int brStart, int brEnd, 
			int cutoff, String query, int setSize,Map<String, MyFasta> acc2Upstream
			,String upstreamOut, String setSizeError) {
		List<AlignResult> ars = AlignResult.getAlignReslt(alignOut, 1);
		//	System.out.println(obtainedAcc.size()+"----------------"+ ars.size());
		 List<String> obtainedAcc = new ArrayList<String>();
		statistic_test.SelectDBDSet.checkSimilar(query, ars, obtainedAcc,
				brStart, brEnd, cutoff,setSize,setSizeError);
		
		statistic_test.SelectDBDSet.palSet(obtainedAcc, acc2Upstream,upstreamOut
				);
	}
}
		
