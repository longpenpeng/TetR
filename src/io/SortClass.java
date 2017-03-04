package io;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p>
 *the  {@code SortClass} is used to sort a certain object  
 * @see SortClass
 * this class can be used to sort the other class via HashSet or List,
 * parameter intVal & doubleVal  these two various may be only one  used once 
 * here these two variables are set for different data type
 * @param <T> the type of objects that this object may be compared to
 * @author pengpeng long 
 */
public class SortClass <T> implements Comparable<T> {
	/**this value is used for caching the object to be sorted*/
	private  T obj;
	/** cache the type of the value measured the order of the object*/
	private String type;
	/** the value used to compare directly*/
	private  int intVal;
	/** the value used to compare directly**/
	private double doubleVal;
	
	/**Initializes a newly created {@code SortClass} object, 
	 *  @param  obj
	 *  */
	public SortClass(T obj) {
		this.obj =obj; 
	}
	/**Initializes a newly created {@code SortClass} object,
	 * 
	 * @param obj
	 * @param intVal
	 */
	public SortClass(T obj, int intVal) {
		this.obj = obj;
		this.intVal = intVal;
		this.type = "int";
	}
	/**Allocates  a  new {@code SortClass} contains an object and a value
	 * whose data type is double
	 * @param obj
	 * @param doubleVal
	 */
	public SortClass (T obj, double doubleVal) {
		this.obj = obj;
		this.doubleVal = doubleVal;
		this.type = "double";
	} 
 	 /**
	  * get the object from a SortClass instance
	  * @return object
	  */
	public T getObj() {
		return this.obj;
	}
	/**
	 *@return intVal 
	 */
	public int getIntVal() {
		return this.intVal;
	}
	/**
	 * Replaces the element at the specified SortClass instance 
	 * @param obj
	 * 
	 */
	
	public void setObj(T obj) {
		  this.obj =obj;
	}
	
	/**
	 * reset the value of intVal in a specified SortClass instance
	 * @param intVal
	 */
	public void  setIntVal( int intVal) {
		this.intVal  = intVal;
	}
	/**
	 * 
	 * @param doubleVal
	 */
	public void setDoubleVal(double doubleVal) {
		this.doubleVal = doubleVal;
	}
	
	public double getDoubleVal() {
		return doubleVal;
	}
 	 
	 @SuppressWarnings("unchecked")
	 @Override 
	 public int compareTo(Object o) {
		SortClass<T> stc = (SortClass<T>)o;
//		if(this.intVal - stc.intVal > 0) {
//			return 1;
//		}else {
//			return -1;
//		}
		if(this.type.equals("int")) {
			 if(this.intVal - stc.intVal >= 0) { 
				 return 1;
			 }else  { 
				 return -1;
			 }
		} else{
			if(this.doubleVal - stc.doubleVal >= 0) {
				return 1;
			}else {
				return -1;
			}
		} 
	}  
	public static void main(String[] args) {
		 TreeSet<SortClass<String>> sortClasses = new TreeSet<SortClass<String>>();
		 sortClasses.add(new SortClass<String>("string", 1.2));
		 sortClasses.add(new SortClass<String>("strin", 3.1));
		 sortClasses.add(new SortClass<String>("strg", 7.3));
		 sortClasses.add(new SortClass<String>("st ", 0.0));
		 Iterator<SortClass<String>> iter = sortClasses.iterator();
		 SortClass<String> sr;
		 while(iter.hasNext()) {
			 sr =iter.next();
			 System.out.println(sr.getObj() + " 	 " + sr.getDoubleVal());
		 }
	 }
}
