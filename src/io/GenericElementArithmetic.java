package io;

/**
 * this {@code GenericElementArthmetic} class try to use generic programming 
 * to achieve element arithmetic operation
 * @author longpengpeng
 *
 */
public   class  GenericElementArithmetic {
	
	public <T extends Number> double add( T t1, T t2) {
		double sum = 0.0;
		sum = t1.doubleValue() + t2.doubleValue();
		return sum;
	}
	public <T extends Number> double minus( T t1, T t2) {
		double sum = 0.0;
		sum = t1.doubleValue() - t2.doubleValue();
		return sum;
	}
	public <T extends Number> double multiply( T t1, T t2) {
		double sum = 0.0;
		sum = t1.doubleValue() * t2.doubleValue();
		return sum;
	}
	public <T extends Number> double divid( T t1, T t2) {
		double sum = 0.0;
		sum = t1.doubleValue()/t2.doubleValue();
		return sum;
	}
}
