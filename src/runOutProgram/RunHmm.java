package runOutProgram;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunHmm {
	
	public RunHmm(){
		
	}
	
	public  void runWithMinScore(String proteinPath ,String outPath ,String dataBasePath ,
			String minScore){ 
		 
		//String shellPath=System.getProperty("user.dir");
		//File shellFile=new File(shellPath);
		String cmd = "hmmsearch" + "--domT" + "minScore+" + "-o" + outPath+ 
				dataBasePath + proteinPath ; 
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			InputStream inputStream = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			InputStream errorStream = process.getErrorStream();
			InputStreamReader esr = new InputStreamReader(errorStream); 
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
	
	public  void runWithoutMinScore(String proteinPath ,String outPath ,String dataBasePath
			){ 
		 
		//String shellPath=System.getProperty("user.dir");
		//File shellFile=new File(shellPath);
		String cmd = "hmmsearch"  + "-o" + outPath+ dataBasePath + proteinPath ; 
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			InputStream inputStream = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			InputStream errorStream = process.getErrorStream();
			InputStreamReader esr = new InputStreamReader(errorStream); 
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
		

}
