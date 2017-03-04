package statistic;
 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import io.PubFunc;
import io.WriteStreamAppend;;

public class Distribute {
	
	/**
	 * calculate the frequency of each bins in the datas, and append to the file
	 * @param bins
	 *  		bins = 1 or bins = 10
	 * @param datas
	 * @param out
	 */
	public static <T extends Number>  void distr(int bins, List<T > datas, 
			String out , String description) {
		
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>()	;
		for(int i = 1; i < 11; i++) {
			map.put(i*bins, 0);
		} 
		double t; 
		int k;
		Iterator<T> iter = datas.iterator();
		while(iter.hasNext()){

			T tt = iter.next();
			t = tt.doubleValue();
			for(int i = 0; i < 10; i++) {
				 
				if( t > (i*bins) && t <= (i+1)*bins) {
					k = map.get((i + 1)*bins);
					map.put((i + 1)*bins, k+1);
				} 
			} 
		}
		
		Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
		int key;
		int value;
		String str = "";
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next(); 
			key = entry.getKey();
			value = +entry.getValue();
			str += key + ":"+ value+"|"; 
		} 
		if(description.equals("NONE")){
			
		}else {
			WriteStreamAppend.appendBufferWriter(out,"#"+description
					+ PubFunc.lineSeparator);
		} 
		
		WriteStreamAppend.appendBufferWriter(out, str+ PubFunc.lineSeparator);
	}
	public static <T extends Number>  void distrLBD(int bins, List<T > datas, 
			String out , String description) {
		
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>()	;
		for(int i = 0; i < 11; i++) {
			map.put(i*bins, 0);
		} 
		double t; 
		int k;
		Iterator<T> iter = datas.iterator();
		while(iter.hasNext()){

			T tt = iter.next();
			t = tt.doubleValue();
			for(int i = 0; i < 11; i++) {
				 
				if( t > ((i-1)*bins) && t <= (i)*bins) {
					k = map.get((i)*bins);
					map.put((i)*bins, k+1);
				} 
			} 
		}
		
		Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
		int key;
		int value;
		String str = "";
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next(); 
			key = entry.getKey();
			value = +entry.getValue();
			str += key + ":"+ value+"|"; 
		} 
		if(description.equals("NONE")){
			
		}else {
			WriteStreamAppend.appendBufferWriter(out,"#"+description
					+ PubFunc.lineSeparator);
		} 
		WriteStreamAppend.appendBufferWriter(out, str+ PubFunc.lineSeparator);
	}  
	public static <T extends Number>  void hitDistr(List<T> datas, 
			String out , String description) {
		
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>()	;
		for(T t:datas) {
			map.put(t.intValue(), 0);
		} 
		int t;
		Iterator<T> iter = datas.iterator();
		while(iter.hasNext()){

			T tt = iter.next();
			t = tt.intValue();
			map.put(t, map.get(t) +1);
		} 
		Iterator<Map.Entry<Integer, Integer>> it = map.entrySet().iterator();
		int key;
		int value;
		String str = "";
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next(); 
			key = entry.getKey();
			value = entry.getValue();
			str +=key + ":" + value +"|"; 
		} 
		if(description.equals("NONE")){
			
		}else {
			WriteStreamAppend.appendBufferWriter(out,"#"+description
					+ PubFunc.lineSeparator);
		} 
		WriteStreamAppend.appendBufferWriter(out, str+ PubFunc.lineSeparator);
	}  
	
	
	public static double format(double n){
		 return Double.parseDouble(String.format("%.2f",n));
	 }
	
	public static void  main(String[] args) {
		String out ="";
		//String descr = "";
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(4);
		list.add(5);
		list.add(1);
		list.add(3);
		list.add(3);
		list.add(2);
		//distr(1, list, out);
	}
	
	 
	 
}
