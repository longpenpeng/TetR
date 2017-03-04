package myKMeans;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import io.*; 

public class Sequence { 
	private String protAddress;
	private String acc2IntAddress;
	private Map<String, String> int2Acc;
	private Map<String, String> acc2prot;
	
	public Sequence(){
		
	}
	
	public Sequence(String protAddress, String acc2IntAdress) {
		this.protAddress = protAddress;
		this.acc2IntAddress = acc2IntAdress;
		this.int2Acc =PubFunc.getMapFromFile(acc2IntAddress, 1, 2, " +");
		this.acc2prot = getAccToProt(protAddress);
	}
	
	public Sequence(String protAddress) {
		this.protAddress = protAddress;
	}
	
	public void getSeq(String clusterDir, String outDir) {
		Map<String, String> int2Prot = int2ProtMap();
		writeToFile(clusterDir, outDir, int2Prot); 
		
	} 
	
	public Map<String, String> int2ProtMap(){
		Map<String, String> map = new HashMap<String, String>();
		String acc ;
		String protSeq;
		for(String index: int2Acc.keySet()){
			acc = int2Acc.get(index);
			if(acc2prot.containsKey(acc)) {
				protSeq = acc2prot.get(acc);
				map.put(index, acc+"  "+ protSeq);
			}else{  
					try {
						throw new Exception("the acc should be contain in "
								+ "the acc2prot map!");
					} catch (Exception e) {
						System.err.println("some errors occurs when converting the map" +
								e.getMessage()); 
					}  
			}
		}
		return map; 
	}
	/*
	 * @param  protAddress  the path of file which stores  sequences of 
	 *   				dna-binding domain as the fasta format
	 */
	public Map<String, String> getAccToProt(String protAddress){
		Map<String, String> acc2Prot = new HashMap<String, String>();
		String acc;
		String seq;
		try{ 
			List<Module> modules = Module.getModules(protAddress, ">");
			for(Module m: modules) {
				String firstLine = m.readLine(0);
				acc = firstLine.split("\\|")[0].replace(">", "").trim();
				seq = m.readLine(1).trim();
				acc2Prot.put(acc, seq);
			}
		}catch(Exception e){
			System.out.println("the DBD protein sequence is not existed!  " +
					e.getMessage());
		}
		return acc2Prot;
		
	}
	/*
	 * @parameter outDir  the directory path of the output sequence files
	 * write the sequence of one cluster member to the out sequence file ,
	 * the name of the sequence file is same with corresponding cluster file name
	 */
	
	public void writeToFile(String clusterDir, String outDir, Map<String, String> int2Prot) {
		PrintWriter writer ;
		List<String> list;
		String clusterFile ;
		String protSeq;
		String index;
		String[] spt;
		for(String file : new File(clusterDir).list()){
			//System.out.println(file);
			try{ 
				clusterFile = clusterDir + File.separator + file;
				writer = new PrintWriter(outDir + File.separator + file);
				list= PubFunc.getListFromFile(clusterFile, 1, " +");   
				for(int i = 0; i< list.size(); i ++) {
					index = list.get(i);  
					if(int2Prot.keySet().contains(index)) {
						protSeq = int2Prot.get(index);
						spt = protSeq.split(" +");
						writer.write(">"+spt[0	] + PubFunc.lineSeparator + spt[1]
								+ PubFunc.lineSeparator );
						writer.flush();
					}
				} 
			}catch (IOException e) {
				System.err.println(" the seqence output file is not existed!   "
							+ e.getMessage());
			} 
		}
	} 
	/*
	 * get the relevant binding dan from the dna file 
	 */
	 
 
	public static void main(String[] args) {
		String protAddress = "D:\\Softtool\\eclipse_java\\workspace_java\\ClusterAlg\\src\\myKMeans\\wholeDBD";
		String acc2Intaddress ="D:\\Softtool\\eclipse_java\\workspace_java\\ClusterAlg\\src\\myKMeans\\acc2int";
		String clusterDir ="D:\\Softtool\\eclipse_java\\workspace_java\\ClusterAlg\\src\\myKMeans\\clusterout";
		String outDir = "D:\\Softtool\\eclipse_java\\workspace_java\\ClusterAlg\\src\\myKMeans\\seq";
		Sequence seq = new Sequence(protAddress, acc2Intaddress);
		seq.getSeq(clusterDir, outDir);
	}

}
