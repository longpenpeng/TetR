package temp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import genome.Genome;
import io.PubFunc;
import search.PalSeq;
import statistic_test.PalSearch;

public class Gemo {
	public static String cutGenoSeq(String genoPath,int start, int end) throws IOException{
		Genome geno = new Genome(genoPath);
		System.out.println(geno.getGenoSeq().length());
		String seq = geno.getGenoSeq().substring(start, end);
		
		return seq;
				
	}
	public static void cut() throws IOException {
		String genoPath = "/home/longpengpeng/refine_TETR/final_test/"
				+ "2017_2_14/genoDir/AB000385.gb";
		int start = 1; //8300480..8301142   8300585..8301142
		int end = 1340;
		
		String seq = cutGenoSeq(genoPath, start, end);
		System.out.print(seq);
	}
	public static double checkGC(String seq){
		 
		char c;
		int counter = 0;
		int seqLenCounter = 0;
		for(int i = 0; i < seq.length(); i++){
			c = seq.charAt(i);
			if((c == 'C')||(c == 'c')){
				counter ++;
			} 
			if((c == 'G')||(c == 'g')){
				counter ++;
			} 
			if(c != 'n'){
				seqLenCounter ++;
			}
		}
	 
		return (double)counter/seqLenCounter;
 
	}
	public static double checkGC(PalSeq ps){
		String seq = ps.getseq();
		char c;
		int counter = 0;
		for(int i = 0; i < seq.length(); i++){
			c = seq.charAt(i);
			if((c == 'C')||(c == 'c')){
				counter ++;
			} 
			if((c == 'G')||(c == 'g')){
				counter ++;
			} 
		}
		return (double)counter/seq.length();  
	}
	public static List<Double> calc_GC(String file, String out){
		List<String> seqList = PubFunc.getListFromFile(file, 2, " +");
		List<Double> GCs = new ArrayList<Double>();
		for(String seq:seqList){
			if(seq.length() < 3){
				continue;
			}
			PalSeq ps = new PalSeq(seq);
			GCs.add(checkGC(ps));
		}
		try {
			PrintWriter writer = new PrintWriter(out);
			for(double gc:GCs){
				writer.write(gc + PubFunc.lineSeparator);
				writer.flush();
			}
			writer.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return GCs;
	}
	public static void statistic_gc (List<Double> gc_content, String out) {
		int bin0_10 = 0;
		int bin10_20 =0;
		int bin20_30 = 0;
		int bin30_40 = 0;
		int bin40_50 = 0;
		int bin50_60 = 0;
		int bin60_70 = 0;
		int bin70_80 = 0;
		int bin80_90 = 0;
		int bin90_100 =0;
		int total = gc_content.size();
		System.out.println(total);
		for(double gc:gc_content){
			if(gc*100 <= 10.0){
				bin0_10 ++;
			}else if(gc*100 <= 20.0){
				bin10_20 ++;
			}else if(gc*100 <= 30.0){
				bin20_30 ++;
			}else if(gc*100 <= 40.0){
				bin30_40 ++;
			}else if(gc*100 <= 50.0){
				bin40_50 ++;
			}else if(gc*100 <= 60.0){
				bin50_60 ++;
			}else if(gc*100 <= 70.0){
				bin60_70 ++;
			}else if(gc*100 <= 80.0){
				bin70_80 ++;
			}else if(gc*100 <= 90.0){
				bin80_90 ++;
			}else {
				//System.out.println(gc);
				bin90_100++;
			}
		}
		try {
			PrintWriter writer = new PrintWriter(out);
			writer.write("0_10  " + bin0_10 + "  " +(double)bin0_10/total +PubFunc.lineSeparator + 
					     "10_20  " + bin10_20  + " " +(double) bin10_20/total+ PubFunc.lineSeparator +
					     "20_30  " + bin20_30 + "  " +(double)bin20_30/total + PubFunc.lineSeparator + 
					     "30_40  " + bin30_40 + "  " + (double)bin30_40/total +  PubFunc.lineSeparator + 
					     "40_50  " + bin40_50 + "  " + (double)bin40_50/total + PubFunc.lineSeparator + 
					     "50_60 " + bin50_60 + "   " +(double)bin50_60/total + PubFunc.lineSeparator +
					     "60_70  " + bin60_70 + "  " +(double)bin60_70/total + PubFunc.lineSeparator +
					     "70_80  " + bin70_80 + "  " + (double)bin70_80/total + PubFunc.lineSeparator +
					     "80_90  " + bin80_90 + "   " + (double)bin80_90/total + PubFunc.lineSeparator+
					     "90_100  " + bin90_100 + "  " + (double)bin90_100/total + PubFunc.lineSeparator
					      
					     
					     );
			writer.flush();
			writer.close();
					      
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void main(String args[]) throws IOException { 
		String file ="/home/longpengpeng/refine_TETR/batch_prediction/merge/merge/maxOut";
		String out = "/home/longpengpeng/refine_TETR/batch_prediction/merge/merge/gc_content";
		String stat_gc = "/home/longpengpeng/refine_TETR/batch_prediction/merge/merge/gc_stat";
		List<Double> gc = calc_GC(file, out);
		statistic_gc(gc, stat_gc);
		
		 
		//System.out.println(gc);
 	}
}
