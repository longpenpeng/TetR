package io;

import java.lang.reflect.Array; 

public class GenericsArray<T> {
	
	private T[] array;
	private int size;
	
	/**
	 * initialize a {@code GenericsArray}, and set the array size, and class type
	 * 
	 * @param type
	 * @param size
	 */ 
	@SuppressWarnings("unchecked")
	public GenericsArray(Class<T> type , int size){
		array = (T[]) Array.newInstance(type, size);
		this.size = size;
	}
	
	/**
	 * add the element {@code T} in to the peculiar index  
	 * @param index
	 * @param item
	 */
	public void put(int index, T item) {
		if(index >=0 && index < size) {
			array[index] = item;
		}
	}
	
	/**
	 * get the element of  peculiar index 
	 * @param index
	 * @return T
	 */
	public T get(int index) {
		if( index >= 0 && index < size) {
			return array[index];
		}
		return null;
	}
	/**
	 * print the generic array to console
	 * @param t
	 */
	public String toString() {
		String str ="";
		for(T t:this.array){
			str += t.toString();
		}
		return str;
	}
}
