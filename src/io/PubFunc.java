package io;

import java.io.BufferedReader;

import java.io.File; 
import java.io.FileReader;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set; 

/**
 *  this {@code PubFunc} contains  some useful static  methods which can 
 *  be used by whole class
 *  <p>
 *  the {@code PubFunc} is very useful , contains two methods whose function 
 *  is to get peculiar column and store in a list or map from a file.Also ,the method 
 *  "sortByValue"  can be used to sort a map by value, and you can store the result 
 *  by a LinkedHashMap. Finally, method creatDir and creatFile can creat a 
 *  directory or file for a certain path.   
 * @author pengpengLong
 *
 */
public class PubFunc {
	/** it equals to character "/", but it can be used both in windows  and Unix*/
	public final static String os=File.separator;
	/** it equals to the line separator ,but it can be used both in windows and Linux */
	public final  static String lineSeparator = System.getProperty("line.separator", "\n");
	
	/** 
	 * @param filePath
	 * 				input file path
	 * @param k
	 * 				the column of data which will be obtained 
	 * @param regex
	 * 				a {@code String} which define the separator of the split() method
	 * @return a List which caches the data of the certain column in the input file
	 */
	@SuppressWarnings("unchecked")
	public static <E> List<E> getListFromFile(String filePath, int k, String regex) {
	 
		List<E> list = new ArrayList<E>(); 
		String line = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			while((line = reader.readLine())!= null) {
				if(line.startsWith("#")) {
					continue;
				}
				if(line.trim().length() <1) {
					continue;
				}
			    String[] spt =line.split(regex);
			    list.add((E) spt[k-1].trim()); 
			} 
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return list; 
	}
	/** 
	 * @param filePath
	 * 				input file path
	 * @param k
	 * 				the column of key which will be obtained 
	 * @param j
	 * 				the column of the value 
	 * @param regex
	 * 				a {@code String} which define the separator of the split() method
	 * @return a Map which caches the data of the certain column in the input file
	 */
 
	public static   Map<String, String> getMapFromFile(String filePath, 
			int k, int j, String regex) {
		Map<String, String> map  = new HashMap<String, String>();
		String line = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			while((line = reader.readLine())!= null) {
				if(line.startsWith("#")) {
					continue;
				}
				if(line.trim().length() <1) {
					continue;
				}
				String[] spt = line.split(regex);
				//System.out.println((spt[k-1].trim())+"  " + (spt[j-1]).trim());
				map.put((spt[k-1].trim()), (spt[j-1]).trim());
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * use the generic programming to sort the Map by value
	 * @param map
	 * @return  a LinkedHashMap cached the key and value  from the bottom to the big order
	 */
    public static <K,V extends Comparable<? super V>>  Map<K,V> sortByValue(Map<K,V> map) {
    	List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
    	Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
    		@Override
    		public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
    			return (o1.getValue()).compareTo((o2.getValue()));
    		}
		});
    	Iterator<Map.Entry<K, V>> iter = list.iterator();
    	Map<K, V> sortedMap  = new LinkedHashMap<K,V>();
    	while(iter.hasNext()) {
    		Map.Entry< K, V> entry = iter.next();
    		sortedMap.put(entry.getKey(), entry.getValue()); 
    	}
    	return sortedMap;
	}
    /**
     * create a directory for the peculiar file path if the directory is not existed
     * @param dirPath
     */
    public static void creatDir(String dirPath) {
    	File f = new File(dirPath);
    	if(!f.exists()) {
    		f.mkdir();
    	}
    }
    /**
     * create a file for  the peculiar file path if the file is not existed
     * @param filePath
     */
    public static void creatFile(String filePath) {
    	File f = new File(filePath);
    	if(!f.exists()){
    		try{
    			f.createNewFile();
    		}catch ( IOException e) {
    			System.out.println(e.getMessage());
    	} 
    	}
    }
    /**
     * check the input number is odd or even
     * @param num
     * @return  boolean 
     */
    public static boolean isOdd(int num){
    	if(num%2 == 0){
    		return false;
    	}else {
			return true;
    	}
    }
    public static  <T extends Number> T max(T t1, T t2){
    	if(t1.doubleValue() > t2.doubleValue()){
    		return t1;
    	}else {
			return t2;
		}
    }
   public static <T extends Number> T min( T t1, T t2) {
	   if(t1.doubleValue() < t2.doubleValue()){
   		return t1;
   	}else {
			return t2;
		}
   }
   public static <T> T getCertainElement(Set<T> set, int k){
	   Iterator<T> iter = set.iterator();
	   T t;
	   int counter = 0;
	   while(iter.hasNext()) {
		   t = iter.next();
		   counter ++;
		   if(counter == k){
			   return t;
		   }
	   }
	   return null;
   }
   
}
