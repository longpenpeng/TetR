package genome;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import io.*;

public class Genome implements Serializable { 
	private static final long serialVersionUID = -8675972743978679002L; 
	 
	private String fileName;
	private String accession;
	private String speciesName;
	private int length;
	private ArrayList<Gene> genes;
	private String genoSeq;
	
	public Genome(){
		
	}
	public Genome(String path) throws IOException{
		
		File genoFile=new File(path); 
		String fileName=genoFile.getName();
		this.fileName=fileName;
		ArrayList<Gene> genes=new ArrayList<Gene>();
		genes=genome.Gene.getGenes(path);
		this.genes=genes;
		String accession="";
		String length=""; 
		String speciesName=""; 
		StringBuilder stb=new StringBuilder();
		 
		BufferedReader in =new BufferedReader(new FileReader(genoFile));
		String line=""; 
		boolean bo=false;
		while((line=in.readLine())!=null){
			
			if(line.length() == 0)
				continue;
			if(line.startsWith("LOCUS")){ 
				String[] spt=line.trim().split(" +"); 
				length=spt[2];   
				this.length=Integer.parseInt(length);
				continue;
			}
			if(line.startsWith("VERSION")){
				String[] spt=line.trim().split(" +");
				    accession=spt[1]; 
					this.accession=accession;
			}
			if(line.startsWith("DEFINITION")) {
				speciesName=genome.Genome.getSpeciesNameFromLine(line);
				 this.speciesName=speciesName;
			}  
			if(line.startsWith("ORIGIN"	)){
				bo=true;
				//System.out.println(" before genoSeq");
				continue; 
			}
			
			if(bo==true){ 
				stb=stb.append(splitLine(line)); 
			} 
		}
		in.close();
		this.genoSeq=stb.toString();
	}
	public static StringBuilder  splitLine(String line) {
		
		int counter=0;
		StringBuilder seq=new StringBuilder();
		StringTokenizer str=new StringTokenizer(line," +");
		while(str.hasMoreElements()){
			counter++;
			if(counter==1){ 
				str.nextToken();
				continue;
			}
			seq.append(str.nextToken());
		}
		return seq; 
	}
  
	public   String  species(String line)
	{
		int start=line.indexOf("TION");
		String specie="";
		if(line.contains(","))
		{
			int stop=line.indexOf(",");
			specie=line.substring(start+6,stop);
		}
		specie=line.substring(start+6,line.length());
		return specie;
	}
	public  String toString() {
		
		String genome=this.fileName+"|"+this.length+"|"+this.accession+"|"+this.speciesName+PubFunc.lineSeparator;
		for(Gene gene:this.genes){
			genome=genome+gene.toString()+PubFunc.lineSeparator;
		}
//		int n=(int)Math.ceil((double)(this.genoSeq.length()/60));
//		System.out.println(n+ "  "+this.genoSeq.length());
//	 
//		for(int i=0;i<n;i++){
//			genome=genome+this.genoSeq.substring(i*60,(i+1)*60)+PubFunc.lineSeparator;
//		//	System.out.println((i+1)*60);
//		}
//		genome=genome+this.genoSeq.substring(n*60,this.genoSeq.length())+PubFunc.lineSeparator; 
		return genome;
		
	}
	public static String getSpeciesNameFromLine(String line)
	{
		int start=line.indexOf("TION");
		String specie="";
		if(line.contains(","))
		{
			int stop=line.indexOf(",");
			specie=line.substring(start+6,stop);
		}
		specie=line.substring(start+6,line.length());
		return specie;
	}
   public static void splitGeno(String inputPath,String outputDir) throws IOException{
	   File file=new File(inputPath);
	   File outFile=null;
	   PrintWriter writer=null;
	   String line="";
	   String fileName="";
	   @SuppressWarnings("resource")
	   BufferedReader reader=new BufferedReader(new FileReader(file));
	   boolean bo=false;
	   while((line=reader.readLine())!=null){
		   if(line.startsWith("LOCUS")){ 
			   String[] spt=line.split(" +");
			   fileName=spt[1]; 
			   outFile=new File(outputDir+PubFunc.os+fileName+".gb");
			   if(!outFile.exists()){
				   outFile.createNewFile();
			   } 
			   writer=new PrintWriter(outFile); 
		   }
		   if(bo){
			   System.out.println(line);
		   }
		   if(line.startsWith("VERSION")){
			   String[] spt=line.split(" +");
			   fileName=spt[1];  
			   File f=new File(outputDir+PubFunc.os+
					   fileName.substring(0,fileName.length()-2)+".gb");
			   outFile.renameTo(f);
			  // outFile.delete();
			   }
//	    
		 writer.write(line+PubFunc.lineSeparator);  
		 writer.flush();
	   } 
     writer.close();
	}
	
	
	
	//***********************setter************************
	public String setAccession(String accession) {
		return this.accession = accession;
	}
	
	public String  setFileName(String fileName) {
		return this.fileName=fileName; 
	}
	 
	
	
	//***********************getter************************
	public String getAccession(){
		return this.accession;
	}
	public List<Gene> getGene() {
		return this.genes;
	}
	public ArrayList<Gene>  getGenes() {
		return this.genes; 
	}
	public int getLength(){
		return this.length;
	} 

	public String getFileName(){
		return this.fileName;
	}
	public String getSpecies(){
		return this.speciesName;
	}
	public String getGenoSeq(){
		return this.genoSeq;
	}
	//***********************test *************************
	public static void main(String[] args) throws IOException {
		String genoPath = args[0];
		Genome  genome = new Genome(genoPath);
		
	}

 }
