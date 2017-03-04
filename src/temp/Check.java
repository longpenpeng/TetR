package temp;

public class Check {
	public static void add(String str){
		String[] spt = str.split(" +");
		int counter =0;
		for(String s:spt){
			counter = counter + Integer.parseInt(s.trim());
		}
		System.out.println(counter);
	}
	public static void main(String[] args) {
		String str ="0 0 0 0 0 0 0 0 0 33 66 0 0 0 0 99 165 0 0 0 0 0 0 0 0 0 0 33 0 33 33 33 0 0 66 0 165 0 0 0 ";
		add(str);
		String[] spt = str.split(" +");
		int[][]	array = new int[spt.length][1];
		for(int i = 0; i < spt.length; i++){
			array[i][0] = Integer.parseInt(spt[i]);
		}
		
	}
}
