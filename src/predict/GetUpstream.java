package predict;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Finishings;

import genome.Gene;
import genome.Genome;
import io.Module;
import io.PubFunc;
import search.UpStream;
/**
 * it is to get the upstream nucleotide sequence of query genes and store it into a file
 * 
 * @author longpengpeng
 *
 */
public class GetUpstream {
	/**
	 * inputPath : the genome file downloaded from genbank,which may compressed many genome files
	 *    so, it is necessary to split the genomes. 
	 *  outDir: the directory which will store the split genome files
	 * @param inputPath
	 * @param outDir
	 */
	public static void splitGenome(String inputPath, String outDir)   {
		try {
			Genome.splitGeno(inputPath, outDir); 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * protPath: the file which contains  the queries fasta protein sequences 
	 * @param protPath
	 * @return map: accession of protein to relative protein sequence
	 */
	public static Map<String, String> acc2seq(String protPath){
		Map<String, String> acc2seq = new HashMap<String, String>();
		List<Module> modules =Module.getModules(protPath, ">");
		String acc;
		String seq;
		for(Module m:modules){
			acc = "";
			seq = "";
			String firstLine = m.readLine(0);
			if(firstLine.contains("|")){
				acc = firstLine.split("\\|")[1];
			}else {
				acc = firstLine.replace(">", "");
			} 
			for(int i = 1; i< m.getNum(); i++){
				seq = seq + m.readLine(i);
			}
			acc2seq.put(acc, seq);
		}
		
		return acc2seq;
	}
	/**
	 * acc2geoPath: the map table of protein accession to genome id,
	 * 	this file is the queries accession and corresponding genome, not subject proteins
	 * @param acc2genoPath
	 * @return
	 */
	public  static Map<String, String> acc2genoId(String acc2genoPath) {
		Map<String,String> acc2genoId = new HashMap<String, String>();
		String line ="";
		try {
			BufferedReader in = new BufferedReader(new FileReader(acc2genoPath));
			String acc = "";
			String genoId;
			while((line = in.readLine())!= null){
				if(line.trim().length() <1){
					continue;
				}
				acc = line.split(" +")[0];
				genoId = line.split(" +")[1];
				acc2genoId.put(acc, genoId);
			}
			in.close();
			return acc2genoId;
			
		} catch (IOException e) {
			System.err.println("the file acc2genome is empty" + e.getMessage());
		}
		return null;
	}
	 /**
	  * to get the upstream of certain accession 
	  * @param genoDir
	  * @param acc2seq
	  * @param acc2genoPath
	  * @param acc
	  * @param bpNum the length of upstream sequence
	  * @throws IOException
	  */
	
	public static String getUpstream(String genoDir, Map<String,String> acc2seq, 
			String acc2genoPath,String acc, int bpNum) throws IOException{
		
		 
		Map<String,String> acc2genoId= acc2genoId(acc2genoPath);
		Genome geno;
		String location;
		String seq = acc2seq.get(acc);
		String genoId = acc2genoId.get(acc);
		String genoFileName;
		if(genoId.contains(".")){
			int index = genoId.indexOf(".");
			genoFileName = genoId.substring(0,index); 
		}else {
			genoFileName = genoId;
		}
		String orientation;
		String genoSeq;
		int genoLenth;
		geno = new Genome(genoDir + PubFunc.os + genoFileName + ".gb"); 
		System.out.println(geno.getGenes().size() + " " + genoDir + PubFunc.os + genoFileName + ".gb");
		for(Gene gene : geno.getGenes()){
			if(gene.getProtSeq().equals(seq)){
				
				location = gene.getLocation();
				orientation = gene.getOrientation(); 
				genoLenth = geno.getLength();
				genoSeq = geno.getGenoSeq();
				 String upstreamLocation=upstreamLocation(location, orientation, bpNum,genoLenth);
					//  System.out.println(genoLenth+"  "+genomePath+"  "+genoSeq.length()+"  "+getEnd(upstreamLocation));
					  String upstreamSeq=genoSeq.substring(getStart(upstreamLocation),getEnd(upstreamLocation));
					  if(upstreamSeq.length()<100){
						  continue; 
					  }
					  String str = ">"+acc+"|"+location+"|"+orientation+"|"+genoLenth+"|"+
							  genoFileName+"|"+0+"|"+0+"|"+0+PubFunc.lineSeparator+upstreamSeq ;
					  return str;
//					  upstreamWriter.write(">"+location+"|"+orientation+"|"+genoLenth+"|"+
//					  genoFileName+"|"+ident+"|"+id+PubFunc.lineSeparator+upstreamSeq+
//					  PubFunc.lineSeparator);
//					  writer.write(id+"|"+genoFileName+"  "+location+"  "+gene.getOrientation()
//					  +"  "+genome.getLength()+PubFunc.lineSeparator); 
				}
			} 
		 return null;
	}   
	  public static String upstreamLocation(String location,String orientation,int bpNum,int genoLength) {
			int  start=0;
			int end=0;
			int startInTf=getStart(location);
			int endInTf=getEnd(location); 
			if(orientation.equals("forward")){
				end=startInTf;
				start=end-bpNum; 
			}
			if(orientation.equals("backward")){
				start=endInTf;
				end=start+bpNum;  
			}
			if(start<0){
				start=1;
			}
			if(end>genoLength){
				end=genoLength;
			}
			String upstreamLocation=start+".."+end;
			return upstreamLocation;
			
		}
	  public static int getStart(String location){
		  int startIndex=location.indexOf(".");
		  String startStr=location.substring(0,startIndex);
		  int start=Integer.parseInt(startStr);
		 // System.out.println(start);
		  return  start;
	  }
	  public static int getEnd(String location) {
		  int endIndex=location.lastIndexOf(".");
		  String endStr =location.substring(endIndex+1,location.length());
		  int end=Integer.parseInt(endStr); 
		  return end; 
	}
	  
	  
	  public static void main(String[] args) throws IOException {
		  String inputPath = "/home/longpengpeng/refine_TETR/final_test/2017_2_14/geno.gb";
		  String outDir = "/home/longpengpeng/refine_TETR/final_test/2017_2_14/genoDir";
		 // splitGenome(inputPath, outDir);
		  String protPath = "/home/longpengpeng/refine_TETR/final_test/2017_2_14/"
		  		+ "from_windows/prot_seq.fasta";
		  int bpNum = 500;
		  String acc2genoPath = "/home/longpengpeng/refine_TETR/final_test/2017_2_14/"
		  		+ "from_windows/prot2genome_17_2_14.txt"; 
		  String upstreamOut = 	"/home/longpengpeng/refine_TETR/final_test/2017_2_14/"
		  		+ "queriesUpstream";
		  PrintWriter writer = new PrintWriter(upstreamOut);
		  
		  //getUpstream(inputPath, outDir, protPath, acc2genoPath,acc, bpNum);
		  /**
		   * protPath : fasta file which contains the whole query protein
		   * 	
		   */
		  Map<String, String> acc2seq = acc2seq(protPath);
//		  String accession = "Q9K463";
//		  String str =getUpstream(outDir, acc2seq, acc2genoPath, accession, bpNum);
//		  System.out.println(str);
		  for(String accession:acc2seq.keySet()){
			 System.out.println(accession);
			 String str =getUpstream(outDir, acc2seq, acc2genoPath, accession, bpNum);
			 System.out.println(str);
			 writer.write(str + PubFunc.lineSeparator);
			 writer.flush();
		  }
		  writer.close();
	  }
}
	  
	 // public static void locateTFR(String protPath,String tablePath,String genoDirPath,int bpNum,String identPath) throws IOException{
//			 // HashMap<String , Integer> protnumCounterInEachEntry=new  HashMap<String, Integer>();
//			  Map<String, String> entryToIdentMap=new HashMap<String, String>();
//			  entryToIdentMap=PubFunc.getMapFromFile(identPath, 1, 7, " +");
//			  HashMap<String, String>uniId2seq=id2ProtSeq(protPath);
//			  
//			  File protFile=new File(protPath);
//			  PubFunc.creatDir(protFile.getParent()+PubFunc.os+"out");
//			  File locateFile =new File(protFile.getParent()+PubFunc.os+"out"+PubFunc.os+"location");
//			  PrintWriter upstreamWriter=new PrintWriter(protFile.getParent()+PubFunc.os+"out"+PubFunc.os+"wholeUpstream");
//			  PrintWriter writer=new PrintWriter(locateFile);
//			  HashMap<String , String> map_table=new HashMap<String, String>();
//			  map_table=fileToMap(tablePath, 1, 2);
//			  for(String id:map_table.keySet()){
//				  
//			      String ident=entryToIdentMap.get(id);
//				  String genoFileName="";
//				  if(map_table.get(id).contains(".")){
//					  genoFileName=map_table.get(id).substring(0,map_table.get(id).length()-2);
//				  }else{
//					  genoFileName=map_table.get(id);
//				  }
//				  String genomePath=genoDirPath+PubFunc.os+genoFileName+".gb";
//				 // System.out.println(genomePath);
//				  if(!new File(genomePath).exists()){
//					  continue;
//				  } 
//				  Genome genome=new Genome(genomePath);
//				  ArrayList<Gene>genes=new ArrayList<Gene>(); 
//				  genes=genome.getGenes(); 
//				  System.out.print(genes.size());
//				  String protseq=uniId2seq.get(id); 
//				  for(Gene gene:genes){
//					  genes=genome.getGenes();
//					  String seq=gene.getProtSeq();
//					  System.out.println(seq);
//					  if(seq.contains(protseq)){
//						  String location=gene.getLocation();
//						  String orientation=gene.getOrientation();
//						  int genoLenth=genome.getLength();
//						  String genoSeq=genome.getGenoSeq(); 
//						  
//						  
//						  String upstreamLocation=upstreamLocation(location, orientation, bpNum,genoLenth);
//						//  System.out.println(genoLenth+"  "+genomePath+"  "+genoSeq.length()+"  "+getEnd(upstreamLocation));
//						  String upstreamSeq=genoSeq.substring(getStart(upstreamLocation),getEnd(upstreamLocation));
//						  if(upstreamSeq.length()<100){
//							  continue; 
//						  }
//						  upstreamWriter.write(">"+location+"|"+orientation+"|"+genoLenth+"|"+
//						  genoFileName+"|"+ident+"|"+id+PubFunc.lineSeparator+upstreamSeq+
//						  PubFunc.lineSeparator);
//							 
//						  writer.write(id+"|"+genoFileName+"  "+location+"  "+gene.getOrientation()
//						  +"  "+genome.getLength()+PubFunc.lineSeparator);
//					  }
//				  }
//				 
//			  }
//			  upstreamWriter.flush();
//			  upstreamWriter.close();
//			  writer.flush();
//			  writer.close(); 
//			  } 
 
 
 
