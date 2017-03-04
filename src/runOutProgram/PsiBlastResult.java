package runOutProgram;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.Module;
import search.PalSeq;

public class PsiBlastResult {
	private String query;
	private String querySeq;
	private int queryLen;
	private String subject;
	private double ident;
	private double Evalue;
	private String subjectSeq;
	private int queryLength;
	private PalSeq ps;
	
	
	public PsiBlastResult(){
		
	}
	public PsiBlastResult(Module module){
		String line = "";
		for(int i = 0; i< module.getNum(); i++){
			line = module.readLine(i); 
			if(line.trim().length() <1){
				continue;
			}
			if(line.startsWith(">")){
				subject = line.split("\\s")[0].replace(">", "").trim();
				continue;
			}	
			if(line.startsWith("Length=")){
				queryLength = Integer.parseInt(line.replace("Length=", "").trim());
				continue;
			} 
			if(line.startsWith(" Identities")){
				String[] spt =line.split("\\s");
				
				if(spt[4].contains("(")){
					ident = Double.parseDouble(spt[4].substring(1,spt[4].length()-3));
					//System.out.println(ident);
				}
				continue;
			}
			if(line.startsWith("Query ")){
				querySeq = line.split("\\s")[4].trim();
				continue;
			}  
			if(line.startsWith("Sbjct ")){
				subjectSeq = line.split("\\s")[4].trim();
				continue;
			}  
		}
	}
	public static List<PsiBlastResult> getpsiBR(Module m, double identCriteria){
		String line = "";
		String query ="";
		int queryLen =0;
		for(int i =0; i< m.getNum(); i++){
			line = m.readLine(i);
			if(line.startsWith("Query=")){
				query = line.replace("Query=", ""); 
			}
			if(line.startsWith("Length=")){
				queryLen = Integer.parseInt(line.replace("Length=", ""));
			}
			if(line.startsWith(">")){
				break;
			}
		}
		List<Module> modules = Module.getModules(m, ">");
		List<PsiBlastResult> psrs = new ArrayList<PsiBlastResult>();
		PsiBlastResult psr;
		for(Module module : modules){
			psr = new PsiBlastResult(module);
			if(psr.getIdent() < identCriteria){
				continue;
			}
			psr.setQuery(query);
			psr.setQueryLen(queryLen);
			psrs.add(psr);
		}
		return psrs;
	}
	public static Map<String, List<PsiBlastResult>> getManyPsiBRInFile(
			String path, double identCriteria){
		List<Module> modules = Module.getModules(path, "Query=");
		List<PsiBlastResult> pbrs = null;
		Map<String, List<PsiBlastResult>> query2SubjectPBR = new HashMap<String, List<PsiBlastResult>>();
		for(Module m:modules){
			
			pbrs = getpsiBR(m, identCriteria);
			String query = pbrs.get(0).getQuery();
			query2SubjectPBR.put(query, pbrs); 
		}
		return query2SubjectPBR;
	}
	public static List<PsiBlastResult>  nonRdund(List<PsiBlastResult>pbrs,String query){
		String querySeq = pbrs.get(0).getQuerySeq();
		List<PsiBlastResult> unRdundPBRs = new ArrayList<PsiBlastResult>();
		List<Double> identList = new ArrayList<Double>();
		List<String> seqList = new ArrayList<String>();
		double ident = 0;
		String HTH = "";
		for(PsiBlastResult pbr:pbrs){
			 ident = pbr.getIdent();
			 HTH = pbr.getSubjectSeq();
			 if(HTH.length() < pbr.getQueryLen() -3){
				 continue;
			 }
			 if(ident == 100){
				 continue;
			 }
			 if(!identList.contains(ident)){
				 identList.add(ident); 
				 unRdundPBRs.add(pbr);
				 seqList.add(HTH); 
				 
			 }else if(!seqList.contains(HTH)) {
				 unRdundPBRs.add(pbr);
				 seqList.add(HTH);
			}else {
				System.out.println(pbr.getSubject());
				continue;
			}
		}
		return unRdundPBRs;
		
	}
	public void setQuery(String query){
		 this.query = query;
	}
	public void setPalSeq(PalSeq ps){
		this.ps = ps;
	}
	public void setQueryLen(int queryLen){
		this.queryLen = queryLen;
	}
	public int getQueryLen(){
		return this.queryLen;
	}
	public String getSubject(){
		return this.subject;
	}
	public String getSubjectSeq() {
		return this.subjectSeq;
	}
	public double getIdent(){
		return this.ident;
	}
	public String getQuery() {
		return query;
	}
	public String getQuerySeq(){
		return querySeq;
	}
	public PalSeq getPalSeq(){
		return ps;
	}
	public static boolean isContain(List<Double> set, double ident){
		for(Double n:set){
			if(n == ident){
				return true;
			}
		}
		return false;
	}
	public static void main(String[] args) {
		String path = "/home/longpengpeng/refine_TETR/metarial/DBD_database/output2";
		List<Module> modules = Module.getModules(path, "Query=");
		List<PsiBlastResult> psrs = null;
		double identCriteria = 50.0;
		for(Module m:modules){
			
			psrs = getpsiBR(m, identCriteria);
			for(PsiBlastResult pbr:psrs){
				System.out.println(pbr.getQuery() +" " +pbr.getSubject() + " " + pbr.getIdent() + " " + pbr.getSubjectSeq());
			}
			
		}
	}
	
	
	

}
