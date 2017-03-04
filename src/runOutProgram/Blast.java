package runOutProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import io.Module;
import io.PubFunc;

/**
 * this class be used to blast the query proteins with TFRs in database 
 * to achieve the blast, one shell script and one Java script are used
 * @author longpengpeng
 *
 */
public class Blast {
	public static String checkFasta(String protPath){
		List<Module> modules = Module.getModules(protPath, ">"); 
		String acc; 
		String newProtPath = new File(protPath).getParent() + PubFunc.os + "checked" +
				new File(protPath).getName();
		try {
			PrintWriter writer = new PrintWriter(newProtPath);
			for(Module m: modules){
				String firstLine = m.readLine(0);
				if(firstLine.contains("|")){
					acc = firstLine.split("\\|")[1];
				}else {
					acc = firstLine.replace(">", "");
				} 
				writer.write(">"+acc + PubFunc.lineSeparator);
				for(int i = 1; i < m.getNum(); i++){
					writer.write(m.readLine(i)+ PubFunc.lineSeparator);
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newProtPath;
	}

	public static void runBlast(String shellScriptPath,  String wholeMapTable, String
			seqbase,String outDir, String queryProtPath,String blastOut, String
			javaClassPath, String database) {
		String cmd = shellScriptPath + " " + wholeMapTable + " " + seqbase + " " + outDir +
				 "  " + queryProtPath + " " + blastOut + " " + javaClassPath + " " +database;

		System.out.println(cmd);
		InputStream in=null;
		try{
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			InputStream inputStream = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream);
			InputStream errorStream = process.getErrorStream();
			InputStreamReader esr = new InputStreamReader(errorStream); 
			int n2;
			char[] c2 = new char[1024];
			StringBuffer standardError = new StringBuffer();
			while((n2 = esr.read(c2)) >0) {
				standardError.append(c2, 0, n2);
			}
			io.PrintUtils.printCurTime("Standard error in blast:"
					+ "" + standardError.toString());
			isr.close();
			esr.close();
			errorStream.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}  
		
	}
	public static void main(String args[]) {
		String protPath ="/home/longpengpeng/work/NCBI_Blast/test_blast_2_10/input/prot.fasta";
		String shellScriptPath ="/home/longpengpeng/work/NCBI_Blast/blast.sh";
		      
		String wholeMapTable = "/home/longpengpeng/work/NCBI_Blast/wholeMapTable";
		String seqbase = "/home/longpengpeng/work/NCBI_Blast/newBacterSeq.txt";
		String outDir = "/home/longpengpeng/work/NCBI_Blast/test_blast_2_10/out";
		String queryProtPath  =	"/home/longpengpeng/work/NCBI_Blast/test_blast_2_10/input";
		String blastOut = "/home/longpengpeng/work/NCBI_Blast/test_blast_2_10/out"; 
		String javaClassPath = "/home/longpengpeng/work/NCBI_Blast/program/class";
		String database = "/home/longpengpeng/work/NCBI_Blast/db/TFRdb";
		//String checkedProtPath = checkFasta(protPath);
		runBlast(shellScriptPath, wholeMapTable, seqbase, outDir, queryProtPath,
				blastOut, javaClassPath, database);
			
	}

}
