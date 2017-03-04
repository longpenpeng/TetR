package genome;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import io.Module;
import io.PubFunc;

public class Gene implements Serializable {  
	private static final long serialVersionUID = -8303288042208157835L;
	
	String  orientation;
	String location;
	String protSeq;
    String product;
	  
	public String setLocation(String location)
	{
		return this.location=location;
	}
	public String setOrientation(String orientation)
	{
		return this.orientation=orientation;
	} 
	public String setProtSeq(String protSeq){
		return this.protSeq=protSeq;
	} 
	public String  getProtSeq() {
		 return this.protSeq; 
	}
	public String getLocation(){
		return this.location;
	}
	public String getOrientation() {
		return this.orientation; 
	}
	public String getProduct() {
		return this.product;
	}
	public String  toString() 
	{
		int n=(int)Math.ceil((double)(this.protSeq.length()/60));
	 
		String line=this.location+"|"+this.orientation+"|"+this.product+"|"+PubFunc.lineSeparator ;
		for(int i=0;i<n;i++){
			line=line+this.protSeq.substring(i*60,(i+1)*60)+PubFunc.lineSeparator; 
		}
		line=line+this.protSeq.substring(n*60,this.protSeq.length())+PubFunc.lineSeparator; 
		return line;
	}
	public static String getLocationFromLine(String line) 
	{
	    boolean bo=false;
		String location=null;
		if(line.contains("("))
		{
			bo=true;
			int start=line.indexOf("(");
			int stop=line.indexOf(")");
			location=line.substring(start+1,stop);
		}
		else {
			String[] spt=line.split(" +");
			location=spt[2];
		}
		if(location.contains("<")){
			location=location.replace("<", "");
		}
		if(location.contains(">")){
			location=location.replace(">", "");
		}
		if(bo==true){
			return location+" "+"backward";
		}else {
			return location+" "+"forward";
		} 
	}
	public static String getProductFromLine(String line) {
		String product="";
		int begin=line.indexOf("\"");
		int end=line.lastIndexOf("\"");
		if(begin==end){
			product=line.substring(begin+1,line.length());
		}else {
			 product=line.substring(begin+1, end);
		} 
		return product;
		
	}
	public static String check_line(String line) 
	{  
			if( line.matches("((\\s*)(gene).*)"))
			{ 
				line="";
			}
			if(!(line.startsWith(">"))&&!Character.isUpperCase(line.charAt(0)))
			{
			     line="";
			}
			  return line; 
	} 
	public static ArrayList<Gene>  getGenes(String path) throws IOException
	{
		File file =new File(path); 
		@SuppressWarnings("resource")
		BufferedReader in=new BufferedReader(new FileReader(file));
		String line="";  
		ArrayList<Module> modules = new ArrayList<Module>();
		Module module=new Module();  
			boolean b=false;
			boolean boSecond=false;//boSecond is the second switch of the gene
			while((line=in.readLine())!=null){ 
				if(line.length() == 0)
					continue;
				if(line.startsWith("ORIGIN")){
					b=false;
					break;
				}
				if(line.startsWith("LOCUS")){ 
					continue;
					}
					if(line.startsWith("DEFINITION")) { 
					    continue;
					}
					if(line.startsWith("     PFAM_domain"))
					{
						b= false;
						continue;
					} 
					if(line.startsWith("     aSDomain"))
					{
						b= false;
						continue;
					}
					if(line.startsWith("     CDS_motif"))
					{
						b= false;
						continue;
					} 
					if(line.length()>13) {
						// System.out.println(line);
						if(line.startsWith("     gene   "))
						{
							 // System.out.println("1"+" "+line);
							b=true;
							module = new Module();
							modules.add(module);
						 	module.add(line);
							continue;
						}
						if((b==true)&&((line.matches("(\\s+)(/translation=\")(\\S+)([A-Z]+)\"")))) {
							 
							//System.out.print("22222"+" "+line);
							// gene.add(line);
							boSecond=true; 
						}		
						if((b==true)&&(boSecond==true)&&( line.matches("(\\s+)([A-Z]+)\""))||line.matches("(\\s+)\""))
						{
							//System.out.println(b);
							//System.out.print("33333"+" "+line);
							 module.add(line);
							boSecond=false;
							b=false;
						}
					} 
					if(b)
					{
						module.add(line);
						continue;
					}
				}
				ArrayList<Gene> genes=new ArrayList<Gene>();
			//	System.out.println(modules.size());
				for(Module m : modules) {	
					 
					if(m.readLine(0).contains("join")||m.readLine(0).contains("order")){
						//System.out.println(m.readLine(0)+"dddddddddddddd");
						continue;
					}
				    Gene gene=new Gene();
				    genes.add(gene);
					String location="";
					String orientation="";
					String seq=""; 
					String product="";
					boolean bo=false;
					//System.out.println(m.readLine(0));
					
				//	System.out.print(bo);
					for(int i=0; i<m.getNum(); i++)
					{					
						String newline=m.readLine(i); 
					 //   System.out.println(newline); 
						
						if(newline.length()>13)	{
							if(newline.substring(5,9).equals("gene"))
							{
								 
								location=Gene.getLocationFromLine(newline); 
								String[] spt=location.split(" +");
								location=spt[0];
								 
								orientation=spt[1];
							//	System.out.println(newline + "  location: "+gene.location+"  orientation  "+gene.orientation);
															
							}
						}  
						if(newline.length()>34&&newline.substring(21,29).equals("/product")){
							product=genome.Gene.getProductFromLine(newline); 
						}
						if(newline.length()>34&&newline.substring(21,33).equals("/translation"))
						{
							String shortsequence="";
							bo=true;
							if(newline.matches("(\\s+)(\\S+)(\")([A-Z]+)"))
							{
								int start=newline.indexOf("\"");
								shortsequence=newline.substring(start+1);
							}
							if(newline.matches("(\\s+)(\\S+)(\")([A-Z]+)(\")"))
							{
								int start=newline.indexOf("\"");
								int stop = newline.lastIndexOf("\"");
								shortsequence=newline.substring(start+1,stop);
							}
							seq+=shortsequence;
							seq=check_line(seq); ;
						}
						if(bo==true&&!(newline.contains("/")))
						{
							if(newline.matches("(\\s+)([A-Z]+)"))
							{ 
								seq+=newline.trim(); 
							}
							if (newline.matches("(\\s+)([A-Z]+)(\")")) 
							{
								newline=newline.replace('\"', ' '); 
								seq+=newline.trim();
							} 
						}
					}
					gene.protSeq=seq;
					gene.location=location;
					gene.orientation=orientation;
					gene.product=product;
					//System.out.println( "  location: "+gene.location+"  orientation  "+gene.orientation+ gene.product+FinalClass.lineSeparator+ gene.protSeq);
//					linesString=">"+filename+"|"+ location+"|"+accession+ "|"+speciesName+FinalClass.lineSeparator;

 			}
				 
			return genes;
	}
}
	

 
