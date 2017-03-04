package genome;

import search.PalSeq;

public class ProtDNAPair {
	private String entry;
	private String prot;
	private String dna;
	private PalSeq ps;
	
	public ProtDNAPair(){
		
	}
 
	public ProtDNAPair(String entry, String prot,String dna){
		this.entry = entry;
		this.prot = prot;
		this.dna = dna;
	}
	public ProtDNAPair(String entry, String prot, PalSeq ps) {
		this.entry = entry;
		this.prot = prot;
		this.ps = ps;
	}
	public ProtDNAPair(String maxOutLine){
		String[] spt = maxOutLine.split("\\s");
		if(spt.length >1){
			this.ps = new PalSeq(spt[1]);
			this.entry = ps.getAcc();
			this.dna = ps.getWholeSeq();
		}else if(spt.length ==1) {
			this.ps = new PalSeq(spt[0]);
			this.entry = ps.getAcc();
			this.dna = ps.getWholeSeq();
		}else {
			throw new IllegalArgumentException();
		}
	}
	public void setProt(String prot){
		this.prot = prot;
	}
	public void  setDNA(String dna) {
		this.dna = dna;
	}
	public void setPs(PalSeq ps){
		this.ps = ps;
	}
	
}
