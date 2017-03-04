package runOutProgram;
  
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List; 

public class RunClustal2 {
	
	private String type = "CLUSTAL"; 
	public RunClustal2() {
		
	}
	
	public  void run(String input,String outPath){ 
		 
		//String shellPath=System.getProperty("user.dir");
		//File shellFile=new File(shellPath);
		String cmd = "clustalw2 -infile="+input + " -outfile=" + outPath + 
				" -output=" + type;
		        
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			InputStream inputStream = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			InputStream errorStream = process.getErrorStream();
			InputStreamReader esr = new InputStreamReader(errorStream);
			
//			int n1;
//			char[] c1 = new char[1024];
//			StringBuffer standardOutput = new StringBuffer();
//			while((n1 = isr.read(c1)) >0) {
//				standardOutput.append(c1, 0, n1);
//			}
//			System.out.println("Standard output:" + standardOutput.toString());
			
			int n2;
			char[] c2 = new char[1024];
			StringBuffer standardError = new StringBuffer();
			while((n2 = isr.read(c2)) >0) {
				standardError.append(c2, 0, n2);
			}
			io.PrintUtils.printCurTime("Standard error in clustal2:"
					+ "" + standardError.toString());
			isr.close();
			esr.close();
			errorStream.close();
		}catch (FileNotFoundException e) {
		 	System.err.println("the input  is not existed while runing clustal"
		 			+ e.getMessage());
		} catch (Exception e) { 
			System.err.println("some error happened when run clustal" + e.getMessage());
		}
		
	}
	public static void main(String[] args) throws FileNotFoundException {
		RunClustal2 clustal2 = new RunClustal2();
		String input =  args[0];
	 
		String out = args[1];
		clustal2.run(input, out);
//		List<AlignResult> ars = AlignResult.getAlignReslt(output, 1);
//		System.out.println(ars.size());
//		PrintWriter writer = new PrintWriter(out);
//		for(AlignResult ar : ars) {
//			System.out.println();
//			writer.write(ar.toString()+ io.PubFunc.lineSeparator);
//		}
//		writer.flush();
//		writer.close();
//		 
	}
}
 
