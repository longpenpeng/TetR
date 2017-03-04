package handleResult;

import java.io.PrintWriter;
import java.util.ArrayList; 
import java.util.List;
import java.util.Map;
import java.util.TreeMap; 
import io.Module;
import io.PubFunc;
import search.PalSeq;

public class SimilarPattern {
	
	
	public static void main(String[] args) {
		String similarPattern = "/home/longpengpeng/refine_TETR/merge/merge/similarPatten";
		String outDir = "/home/longpengpeng/refine_TETR/merge/merge/statistic/similar";
		List<Integer> lenList = new ArrayList<Integer>();
		try {
			PrintWriter detailWriter = new PrintWriter(outDir +PubFunc.os+ "similardetail");
			PrintWriter lenWriter = new PrintWriter(outDir +PubFunc.os+ "sepLen");
			getSepNum(similarPattern,lenList, detailWriter);
			for(int gapLen : lenList){
				lenWriter.write(gapLen + PubFunc.lineSeparator);
				lenWriter.flush();
			}
			detailWriter.flush();
			detailWriter.close();
			lenWriter.flush();
			lenWriter.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void getSepNum(String similarPattern,List<Integer> 
												lenList, PrintWriter writer){
		
		List<Module> modules = Module.getModules(similarPattern, ">>>"); 
		for(Module m:modules){
			getSeqNum(m, lenList, writer);
		}
	}
	
	public static void getSeqNum(Module m, List<Integer> lenList, PrintWriter writer){ 
		
		String firstLine = m.readLine(0);
		String secLine = m.readLine(1);
		PalSeq ps1 = new PalSeq(firstLine.split(" +")[1]);
		PalSeq ps2 = new PalSeq(secLine.split(" +")[1]);
		PalSeq ps3;
		String thrLine;
		if(m.getNum() == 2){ 
			getNum(ps1, ps2, lenList, writer);
			
		}else {
			thrLine = m.readLine(2);
			ps3 = new PalSeq(thrLine.split(" +")[1]);
			getNum(ps1, ps2, ps3, lenList, writer); 
		}
	}
	
	public static void getNum(PalSeq ps1, PalSeq ps2, List<Integer> lenList,
														PrintWriter writer){
		int length = 0;
		int end1 =0;
		int start2 = 0;
		Map<Integer, Integer> start2end = new TreeMap<Integer, Integer>();
		start2end.put(ps1.getStart(), ps1.getEnd());
		start2end.put(ps2.getStart(), ps2.getEnd());
		System.out.println(start2end);
		int counter =0 ;
		for(int start:start2end.keySet()){
			counter ++;
			if(counter ==1){
				end1 = start2end.get(start);
				writer.write(start + "  " + end1 + "  ");
				continue;
			}
			if(counter ==2){
				start2 = start;
				writer.write(start2 + "  " + start2end.get(start) + "  ");
				continue;
			}
			
		}
		length = start2 - end1;
		writer.write(length +"  "+ ps1.getAcc()+ PubFunc.lineSeparator);
		writer.flush();
		lenList.add(length);
		 
	}
	public static void getNum(PalSeq ps1, PalSeq ps2, PalSeq ps3,
							List<Integer> lenList, PrintWriter writer){
		int length1 = 0;
		int length2 = 0;
		int end1 =0;
		int start2 = 0;
		int end2 = 0;
		int start3 = 0;
		Map<Integer, Integer> start2end = new TreeMap<Integer, Integer>();
		start2end.put(ps1.getStart(), ps1.getEnd());
		start2end.put(ps2.getStart(), ps2.getEnd());
		start2end.put(ps3.getStart(), ps3.getEnd());
		System.out.println(start2end);
		int counter =0 ;
		for(int start:start2end.keySet()){
			counter ++;
			if(counter ==1){
				end1 = start2end.get(start);
				writer.write(start + "  " + end1 + "  ");
				continue;
			}
			if(counter ==2){
				start2 = start;
				end2 = start2end.get(start2);
				writer.write(start2 + "  " + end2 + "  ");
				continue;
			}
			if(counter == 3){
//				if(start3<end2){
//					continue;
//				}
				start3 = start;
				writer.write(start3 + "  " + start2end.get(start3) + "  ");
				continue;
			}
			
		}
		length1 = start2 - end1;
		length2 = start3 - end2; 
		if(length1 < 0 && length2 <0){
			
		}else if(length1 > 0 && length2 <0) {
			writer.write(length1 +"  "+ps1.getAcc()+ PubFunc.lineSeparator);
			lenList.add(length1);
		}else if (length1 < 0 && length2 >0) {
			writer.write( length2+"  " +ps1.getAcc()+ PubFunc.lineSeparator); 
			lenList.add(length2);
		}else {
			writer.write(length1 +"  "+ length2+"  " +ps1.getAcc()+ PubFunc.lineSeparator);
			lenList.add(length1);
			lenList.add(length2);
		}
		
		 
	}
}
