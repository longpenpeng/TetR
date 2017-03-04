package util;

import java.io.File;
import java.io.PrintWriter; 
import java.util.Iterator;
import java.util.List;

import genome.HmmResult; 
import io.PubFunc;

public class HandleHmmOut {
	public static void main(String[] args) {
		String hmmOut = args[0]; 
		String DBDSeq = new File(hmmOut).getParent()+ PubFunc.os + "DBDSeq";
	
		try {
			PrintWriter writer = new PrintWriter(DBDSeq);
			List<HmmResult> hmmResults = HmmResult.getHmmResults(hmmOut);
			Iterator<HmmResult> iter = hmmResults.iterator();
			while(iter.hasNext()) {
				writer.write(iter.next().toString() + PubFunc.lineSeparator);
				writer.flush();
			} 
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		} 
	}

}
