package predict;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import io.Module;
import io.PubFunc;

public class PreProcess {
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
	public static void main(String args[]){
		String protPath = "/home/longpengpeng/refine_TETR/final_test/2017_2_14/"
				+ "from_windows/prot_seq.fasta";
		checkFasta(protPath);
	}
}
