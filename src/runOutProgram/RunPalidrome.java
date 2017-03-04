package runOutProgram;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunPalidrome {
	public static void runPal(String infile,String outfile){
//		String infile="/home/longpengpeng/work/tse/_new/upstream";
//		String outfile="/home/longpengpeng/work/tse/_new/out";
		String cmd="palindrome "+infile+" -minpallen=5 -maxpallen=20 -gaplimit=6"
				+ " -nummismatches=2 -outfile="+outfile +" -overlap=n";
		System.out.println(cmd);
		InputStream in=null;
		try{
			Process process=Runtime.getRuntime().exec(cmd);
			process.waitFor();
			in=process.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			String result=reader.readLine();
			System.out.println("info:"+result);
		}catch(Exception e){
			e.printStackTrace();
		}  
	}
	public static void runPal(String infile,String outfile,int minpallen,int maxpallen,int gaplimit,
			 int nummismatches,boolean overlap) {
		String cmd="palindrome "+infile+" -minpallen="+minpallen+" -maxpallen="+maxpallen+" -gaplimit="
		   +gaplimit+" -nummismatches="+nummismatches+ " -outfile="+outfile +" -overlap="+overlap;
		System.out.println(cmd);
		InputStream in=null;
		try{
			Process process=Runtime.getRuntime().exec(cmd);
			process.waitFor();
			in=process.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			String result=reader.readLine();
			System.out.println("info:"+result);
		}catch(Exception e){
			e.printStackTrace();
		}  
		
	}
	public static void main(String[] args){
//		String infile="/home/longpengpeng/work/tse/_new/upstream";
//		String outfile="/home/longpengpeng/work/tse/_new/out";
		if(args.length==2){
			System.out.println("minpallen=5,maxpallen=20,gaplimit=2,nummismatches=2,-overlap=n");
			String infile=args[0];
			String outfile=args[1];
			runPal(infile, outfile);
		}
		else{
			System.out.println("please input infilePath,outfilePath,minpallen,maxpallen,"
					+ "gaplimit,nummismatches, overlap(true or false)");
			String infile=args[0];
			String outfile=args[1];
		    int minpallen=Integer.parseInt(args[2]);
		    int maxpallen=Integer.parseInt(args[3]);
		    int gaplimit=Integer.parseInt(args[4]);
		    int nummismatches=Integer.parseInt(args[5]);
		    boolean overlap=Boolean.parseBoolean(args[6]);
		    runPal(infile, outfile, minpallen, maxpallen, gaplimit, nummismatches, overlap);
		}
		 
	}

}
