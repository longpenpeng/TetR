package temp;

public class Format {
	public static void main(String[] args){
		String str = "adsdffa";
		double n = 8.09;
		
		System.out.println(str.format("%-20s%7.2f", str,n));
	}

}
