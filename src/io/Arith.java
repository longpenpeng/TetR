package io;

import java.math.BigDecimal;

public class Arith {
	
	private static final int DIV_SCALE = 10;
	private Arith(){
		
	}
	
	/**
	 * add exactly and return the result keep "scale" decimal place
	 * @param v1
	 * @param v2
	 * @return  the addition result keep three decimal place
	 */
	public static double add(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return Double.parseDouble(String.format("%."+ scale +"f",
				b1.add(b2).doubleValue()));
	 }
	/**
	 * subtract exactly and return the result  keep "scale" decimal place
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return v1 subtract v2
	 */
	public static double sub(double v1, double v2,int scale){

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return Double.parseDouble(String.format("%."+ scale +"f",
				b1.subtract(b2).doubleValue()));
	 }
	/**
	 * multiply exactly and  return the result  keep "scale" decimal place
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double mult(double v1, double v2){

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return Double.parseDouble(String.format("%.3f",b1.multiply(b2).doubleValue()));
	 }
	
	/**
	 * divide exactly  return the result  keep default(10) decimal place
	 * @param v1
	 * @param v2
	 * @return vi divide v2
	 */
	public static double div(double v1, double v2){

		return div(v1, v2, DIV_SCALE);
				
	}
	/**
	 *  divide exactly  return the result  keep "scale" decimal place
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static double div(double v1, double v2, int scale) {
		if(scale <0) {
			throw new IllegalArgumentException("the scale should be"
					+ " a integer or zero");
		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	/**
	 * return the result of rounding keep "scale" decimal place
	 * @param v
	 * @param scale
	 * @return
	 */
	public static double round(double v, int scale) {
		if(scale <0) {
			throw new IllegalArgumentException("the scale should be"
					+ " a integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b1.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
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
	public static void  main(String[] args) {
		System.out.println(add(0.457, 0.2348154515156 ,1));
	}

}
