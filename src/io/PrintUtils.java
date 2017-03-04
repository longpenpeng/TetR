package io;
  
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
/**
 * Contains several utility methods for printing the results of a search result
 */
public final class PrintUtils {
	public static<T> void printStart(T t) {
		Object obj = (Object) t;
		System.out.printf("START: =========== %s ==========%n", obj.toString());
	}
	
	public static<T> void printStar(T t) {
		Object obj = (Object) t;
		System.out.printf("************** %s **************%n", obj.toString());
	} 
	
	public static <T> void printEnd(T t) {
		Object obj = (Object) t;
		System.out.printf("END:=========== %s ==========%n", obj.toString());
	} 
	public static void printSearchResults(Map<String, List<String>> results) {
		System.out.println(String.format("Found %d entries:", results.size()));

		Set<String> entries = results.keySet();

		for (String entry : entries) {
			System.out.println("entry: " + entry);
			printListComponents(results.get(entry));
		}
	}

	public static <T> void printListComponents(List<T> objectComponents) {
		for (Object t : objectComponents) {
			System.out.println("   " + t.toString());
		}
	}
	public static <T> void printInfoToStdOut(T t) {
		Object obj = (Object) t;
		System.out.println("[INFO]: " + obj.toString());
	}
	public static <T> void printCurTime(T t) {
		Date dNow = new Date()  ;
		SimpleDateFormat ft = 
			      new SimpleDateFormat ("MM-dd hh:mm:ss");
		Object obj = (Object) t;
		 
	      System.out.println("["+ft.format(dNow) + " INFO]: " + obj.toString());
	} 
	public static void main(String[] args){
		printCurTime("sss");
		printInfoToStdOut("ssss");
	}
	
	
	
	
	 
}
