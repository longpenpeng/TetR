package handleResult;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.Arith;
import io.PubFunc; 
public class Temp {
	public static void main(String[] args){
		String path = "/home/longpengpeng/refine_TETR/merge/merge/statistic/similar/sepLen";
		List<Integer> list = new ArrayList<Integer>();
		List<String> strList = PubFunc.getListFromFile(path,1 , " +");
		for(String str: strList){
			list.add(Integer.parseInt(str));
		}
		String out = "/home/longpengpeng/refine_TETR/merge/merge/statistic/similar/seq distr";
		Map<Integer, Integer> map = distr(10, list, out, "");
		try {
			PrintWriter writer = new PrintWriter(out);
			for(int index:map.keySet()){
				writer.write(index + " "+map.get(index) +"  "+ Arith.div(map.get(index), list.size(),
						3)+ PubFunc.lineSeparator);
			}
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		 
	}
	public static <T extends Number>  Map<Integer, Integer> distr(int bins, List<T > datas, 
			String out , String description) {
		
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>()	;
		for(int i = 1; i < 11; i++) {
			map.put(i*bins, 0);
		} 
		
		map.put(1000,0);
		double t; 
		int k;
		int counter = 0;
		Iterator<T> iter = datas.iterator(); 
		while(iter.hasNext()){
			
			T tt = iter.next();
			 
			t = tt.doubleValue();
			for(int i = 0; i < 10; i++) {
				 
				if( t > (i*bins) && t <= (i+1)*bins) {
					k = map.get((i + 1)*bins);
					map.put((i + 1)*bins, k+1);
					break;
				}
				if( t> 100){
					
					counter ++;
					k = map.get(1000);
					System.out.println(t+ " " + k);
					map.put(1000,k +1);
					break;
				}
			} 
		} 
		System.out.println(datas.size());
		System.out.println(counter);
		return map;
		 
	} 
}
