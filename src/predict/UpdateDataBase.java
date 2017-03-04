package predict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import io.Module;
import io.PubFunc;
import search.UpStream;

public class UpdateDataBase {
	public static List<String> getAccInSeqDB(String seqDB){
		String line;
		List<String> accs = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(seqDB));
			while((line = in.readLine()) != null){
				if(line.startsWith(">")){
					accs.add(line.replace(">", ""));
				}else {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accs; 
	}
	public static void updateSeqDB(String seqDB, String queriesProt){
		List<Module> modules = Module.getModules(queriesProt, ">");
		String acc;
		String seq = "";
		List<String> accsInSeqDB = getAccInSeqDB(seqDB);
		for(Module m: modules){
			String firstLine = m.readLine(0);
			if(firstLine.contains("|")){
				acc = firstLine.split("\\|")[1];
			}else {
				acc = firstLine.replace(">", "");
			}  
			for(int i = 1; i < m.getNum(); i++){
				seq = seq + m.readLine(i);
			}
			String str =  ">"+acc + PubFunc.lineSeparator + seq + PubFunc.lineSeparator ;
			if(accsInSeqDB.contains(acc)){
				continue;
			}else {
				io.WriteStreamAppend.appendBufferWriter(seqDB, str);
				System.out.print("update sequence database: " + acc);
			}
		} 
	}
	public static List<String> getAccInUpstream(String upstreamPath) {
		String line;
		String acc;
		List<String> accs = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(upstreamPath));
			while((line = in.readLine()) != null){
				if(line.startsWith(">")){
					if(line.contains("|")){
						acc = line.split(">|\\|")[1];
						 
					}else {
						acc = line.replace(">", "");
					}  
					accs.add(acc);
				}else {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accs; 
	}
	public static void updateWholeUpstream(String wholeUpstream,String queriesUpstream) {
 
		List<String> accsInWholeUpstream = getAccInUpstream(wholeUpstream);
		System.out.println(accsInWholeUpstream.size());
		List<Module> modules = Module.getModules(queriesUpstream, ">");
		String acc;
		int counter = accsInWholeUpstream.size();
		for(Module m: modules){
			String seq = "";
			String firstLine = m.readLine(0);
			if(firstLine.contains("|")){
				acc = firstLine.split(">|\\|")[1];
				//System.out.println(acc);
			}else {
				acc = firstLine.replace(">", "");
			}  
			for(int i = 1; i < m.getNum(); i++){
				seq = seq + m.readLine(i);
			}
			String str =  firstLine+ PubFunc.lineSeparator + seq + PubFunc.lineSeparator;
			if(accsInWholeUpstream.contains(acc)){
				//System.out.println(acc);
				continue;
			}else {
				System.out.println("update UpStream :"+acc);
				counter ++;
				io.WriteStreamAppend.appendBufferWriter(wholeUpstream, str);
			}
		}  
		System.out.println("UpStream size: "  + counter );
	}
	public static void updateWholeMapTable(String wholeMapTable, String queriesMapTable){
		List<String> accsInWholeMapTable = PubFunc.getListFromFile(wholeMapTable, 1, " +");
		Map<String, String> acc2genoIdInQueriesMapTable = PubFunc.getMapFromFile(
				queriesMapTable, 1, 2, " +");
		for(String acc: acc2genoIdInQueriesMapTable.keySet()){
			if(accsInWholeMapTable.contains(acc)){
				continue;
			}else {
				String str =  acc + "  " + acc2genoIdInQueriesMapTable.get(acc) +PubFunc.lineSeparator;
				io.WriteStreamAppend.appendBufferWriter(wholeMapTable, str);
				System.out.println("update map table :"+acc);
			} 
		} 
	}
	public static void main(String args[]) {
		String queriesUpstream  = "/home/longpengpeng/refine_TETR/test/test_update/queriesUpstream";
		String wholeUpstream = "/home/longpengpeng/refine_TETR/test/test_update/wholeUpstream";
		//updateWholeUpstream(wholeUpstream, queriesUpstream);
		String queriesProt = "/home/longpengpeng/refine_TETR/test/test_update/prot.fasta";
		String seqDB = "/home/longpengpeng/refine_TETR/test/test_update/newBacterSeq.txt";
		//updateSeqDB(seqDB, queriesProt);
		String wholeMapTable = "/home/longpengpeng/refine_TETR/test/test_update/wholeMapTable";
		String queriesMapTable = "/home/longpengpeng/refine_TETR/test/test_update/prot2genome.txt";
		updateWholeMapTable(wholeMapTable, queriesMapTable);
	}
}







