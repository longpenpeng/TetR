package genome;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.*;

/**
 *the{@code MyFasta} extends the {@code Fasta}, the different is header;
 *<p>
 *the {@code MyFasta} header should like this:
 *		">Accession|location|orientation|fileName|genoLength|identity|entropy"
 *	this header is more details than the super class {@code Fasta};
 *	the sequence is same with super class
 * @author Administrator 
 *
 */
public class MyFasta extends Fasta {
 
	private static final long serialVersionUID = 7535848177943974555L;
	private String acc;
	private String location;
	private String orientation; 
	private String genoLength; 
	private String fileName ; 
	private int dissimilar;
	private String ident;
	private double entropy;
	
	
	private String  seq;  
	 
	/**empty constructor */ 
    public MyFasta() { 
    	super();
	  }
    /**initialize an instance using super constructor*/
    public MyFasta(String acc) {
    	super(acc);
    }
    /**initialize an instance using super constructor*/
    public MyFasta(String acc, String seq) {
    	super(acc, seq);
    }
    
	public MyFasta(Module module)
	{ 
		 String firstLine=module.readLine(0);
		 String[] spt=firstLine.split(">|\\|");
		 acc = spt[1];
		 location = spt[2];
		 orientation = spt[3];
		 fileName = spt[4];
		 genoLength = spt[5]; 
		 dissimilar = Integer.parseInt(spt[6]);
		 ident = spt[7];
		 entropy = Double.parseDouble(spt[8]); 
		 seq ="";
		 for(int i = 1; i < module.getNum(); i++) {
//			 System.out.println(module.readLine(1));
			 this.seq += module.readLine(i);
			 
		 } 
		// System.out.println(seq);
		 
	} 
	//***********************getter************************
	@Override
	public String getAcc(){
		return this.acc;
	}
	@Override
	public String getSeq(){
		return this.seq;
	}
	public double getEntropy(){
		return this.entropy;
	}
	public String getOrientation(){
		return this.orientation;
	}
	public  String getGenoLength() {
		return this.genoLength; 
	}
	public String getIdent(){
		return this.ident;
	}
	public String getLocation()
	{
		return this.location;
	} 
	public   String getFileName()
	{
		return this.fileName;
	} 
	public int getDissimilar(){
		return this.dissimilar;
	}
	
	public String getHeader(){
		return ">"+acc+"|" + location +"|"+orientation +"|" + fileName +"|"
				+genoLength+"|"+ dissimilar +"|" + ident+"|"+entropy;
	}
	//?? which getHeader is right??, be careful here
//	public String getHeader(){
//	return ">" + location +"|"+orientation +"|" + fileName +"|"
//			+genoLength+"|"+ dissimilar +"|"+acc;
//}
	//1107815..1108471|forward|1110312|NZ_LDWN01000003|A0A0J6SUK9

	//***********************setter************************
	public String setFileName(String fileName)
	{
		return this.fileName=fileName;
	} 
	public  String  setOrientation( String  orientation)
	{
		return this.orientation=orientation; 
	}
	public String setIdent(String ident){
		return this.ident=ident;
	}
	public int setDissimilar(int dissimilar){
		return  this.dissimilar = dissimilar;
	}
	
	 @Override
	 public String toString() 
	{
		 String header = getHeader();
		return  header +PubFunc.lineSeparator+ seq;
		
	}
	public String toUpstreamFile(){
		String header = getHeader() + "|"+ acc;
		return header + PubFunc.lineSeparator + seq;
	}
	 
	public static ArrayList<MyFasta> getFastasFromFile(String upstreamPath,
			String regex)  {
		
		ArrayList<MyFasta> fastas=new ArrayList<MyFasta>(); 
		try {
			ArrayList<Module>modules= Module.getModules(upstreamPath, regex);
			for(Module module:modules){
				 	 //System.out.println(module.toString()+FinalClass.lineSeparator); 
				MyFasta fasta=new MyFasta(module);
				fastas.add(fasta);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fastas; 
	}
	/**
	 * return a Map whose key is accession ,and the value is {@code Fasta}
	 * @param upstream
	 * @param regex
	 * @return
	 */
	public static Map<String, Fasta> acc2FastaMap(String upstream, String regex) { 
	  
		ArrayList<Module>modules= Module.getModules(upstream, regex);
		Map<String,  Fasta> acc2Fasta = new HashMap<String, Fasta>();
		String acc;
		Fasta fasta;
		for(Module module:modules){
			 	 //System.out.println(module.toString()+FinalClass.lineSeparator); 
			fasta =new Fasta(module);
			acc = fasta.getAcc(); 
			acc2Fasta.put(acc, fasta);
		}
		return acc2Fasta;
	}
	
	public static MyFasta getMyFasta(String acc, String file) {
		List<MyFasta> myFastas = getFastasFromFile(file, ">");
		return getMyFasta(myFastas, acc);
		
	}
	public static MyFasta getMyFasta(List<MyFasta> myFastas, String acc){
		Iterator<MyFasta> iter = myFastas.iterator();
		MyFasta fasta = null;
		MyFasta myFasta = null;
		String accession;
		while(iter.hasNext()) {
			
			fasta = iter.next();
			accession = fasta.getAcc();
			if(accession.equals(acc)){
				myFasta = fasta;
			}
		}
		return myFasta;
	}
	public  static void main(String[] args) throws Exception
	{
		String path="/home/longpengpeng/work/material/wholeUpstream";
		String newpath="/home/longpengpeng/work/material/wholeUpstream_new";
		//String path = "/home/longpengpeng/refine_TETR/metarial/wholeUpstream";
		 PrintWriter writer = new PrintWriter(newpath);
		ArrayList<MyFasta>proteinSeqs =new ArrayList<MyFasta>();
		proteinSeqs=getFastasFromFile(path, ">");
		 
		for(MyFasta fasta:proteinSeqs)
		{
			System.out.println(fasta.getAcc() + fasta.getLocation());
		 writer.write(">"+fasta.genoLength  + "|" +  fasta.acc+ "|" + fasta.location + "|" + fasta.orientation+
				 "|"+fasta.fileName +"|"+0+"|"+0+"|"+0+
			 		PubFunc.lineSeparator + fasta.seq + PubFunc.lineSeparator);
		}
		writer.flush();
		writer.close();
		
	}
	
	

}
