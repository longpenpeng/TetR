package myKMeans;
/*
 *  this is the main function of the k-means algorithm, the data files contain two files, the distance file and the index file
 *  
 */
import java.io.BufferedReader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import io.PubFunc;
import runOutProgram.AlignResult;
import runOutProgram.RunClustal2;
import svm.DnaPortPair;

public class Main {
	/**
	 * the data format in distance file :
	 * 1,2,0.3
	 * 1,3.0.4
	 * the data format in point file:
	 *first column :1,2,3 ....L, L+1, L+2,.....2L
	 *second column : 1,2,3 ....L,1 ,2 ,3 ,....L
	 */
	public static void  main(String[] args) throws FileNotFoundException {
		String distancePath  = args[0];  
		String curDir =  new File(distancePath).getParent(); 
		String protAddress = curDir + File.separator + args[1];
		String acc2IntAdress = curDir + File.separator + args[2];
		String similarPattern = curDir + PubFunc.os + args[3];
		String pointPath =  curDir + File.separator + "point";
		String detailDir =  curDir + File.separator + "details"; 
		String clusterDir = curDir + File.separator + "clusterOut"; 
		String summary = curDir + File.separator + "summery" ; 
		String prot2Nucl = curDir + File.separator + "merge";
		String msa = curDir + io.PubFunc.os + "msa";
		String extractMAS = curDir + io.PubFunc.os + "MSAExtract";
		PubFunc.creatDir(detailDir); 
		PubFunc.creatDir(clusterDir); 
		PubFunc.creatDir(prot2Nucl);
		PubFunc.creatDir(extractMAS);
		PubFunc.creatDir(msa);
		PubFunc.creatDir(curDir + File.separator + "sequence");
		double[][] distMatrix = getDistMatrix(distancePath); 
		generatePointFile(pointPath);
		MyCluster kmc = new MyCluster(distMatrix, getPoints(pointPath));
		Sequence seq = new Sequence(protAddress, acc2IntAdress);
		double cutoff = 0.3;
		List<Cluster> clusters = kmc.runCluster(cutoff); 
		Map<Cluster, Integer> clusterMap= new HashMap<Cluster, Integer>();
		Map<Cluster, Integer> sortedMap = new LinkedHashMap<Cluster, Integer>();
		for(Cluster c:clusters) { 
			clusterMap.put(c, c.getMembers().size());
		}   
		sortedMap = MyCluster.sortByValue(clusterMap);
		writeToFile(sortedMap, summary, clusterDir, detailDir, distMatrix); 
		seq.getSeq(clusterDir, curDir + File.separator + "sequence" );
		String seqFile;
		String mergeFile;
		String out; 
		RunClustal2 clustal2 = new RunClustal2();
		for(String file: (new File(curDir + File.separator + "sequence").list())) {
			out = msa + PubFunc.os + file; 
			seqFile = curDir + File.separator + "sequence" + File.separator +file;
			/*multiply sequence align */
			clustal2.run(seqFile, out);
			/*write the align result into other file with the fasta format*/
			AlignResult.write2File(out, 1, extractMAS + PubFunc.os + file);
			mergeFile = prot2Nucl +   PubFunc.os + file;
			/*combine protein sequence with corresponding binding rna*/
			DnaPortPair.mergeProtAndNucl(extractMAS + PubFunc.os + file, similarPattern, mergeFile);
		}
		
	}
	
	public static void writeToFile(Map<Cluster,  Integer> sortedMap, String summary,
					 String clusterDir, String detailDir, double[][] distMatrix 
								 ) throws FileNotFoundException { 
		PrintWriter summWriter = new PrintWriter(summary);
		PrintWriter writer ;
		String str;
		int counter = 0;
		for(Cluster c:sortedMap.keySet()) {
			writer = new PrintWriter(clusterDir + File.separator + c.getId());
			str = "ClusterId"+c.getId()+ ":  center:" +
		                  c.getCenter().getX() + " ,Element:" + sortedMap.get(c);
			System .out.println(str);
		 
			summWriter.write(str+ "\n"); 
			writer.write(c.toString() + "\n");
			getEachIdent(detailDir, c, distMatrix);
			counter = counter + sortedMap.get(c);
			writer.flush();
			writer.close();
		} 
		System.out.println(counter);
		summWriter.flush();
		summWriter.close();
	}
	public static void getEachIdent(String detailDir, Cluster c, double[][] distMatrix) 
			throws FileNotFoundException {
		 
		PrintWriter writer = new PrintWriter(detailDir + File.separator + c.getId()) ;
		MyCluster kmc = new MyCluster(distMatrix);
		List<Point> member = c.getMembers(); 
		Point center =c.getCenter();
		int centerX = center.getX();
		double ident; 
		TreeSet<Distance> distSet = new TreeSet<Distance>();
	    for(Point mem :member) { //get the identity between center and each member
			ident = kmc.getDistance(centerX, mem.getX()); 
			distSet.add(new Distance(mem, center,ident)); 
		} 
		for(Distance dist : distSet) { //TreeSet will sort the distance automatically
			writer.write(dist.getDest().getX() + "  " + dist.getSource().getX() + "  " +dist.getDis() + "\n");
			writer.flush();
		} 
		distSet.clear();
		for(Point p1:member) { // get the identity  of the member each other
			for(Point p2:member) {
				if(p1.equals(p2)) {
					continue;
				}
				ident = kmc.getDistance(p1.getX(), p2.getX()); 
				distSet.add(new Distance(p1,	p2,ident));  
			}
		}
		for(Distance dist : distSet) { //TreeSet will sort the distance automatically
			writer.write(dist.getDest().getX() + "  " + dist.getSource().getX() + "  " +dist.getDis() + "\n");
			writer.flush();
		} 
		
		writer.close();
	}
	public static void  generatePointFile(String pointPath) throws FileNotFoundException {
		int size = 4086;
		PrintWriter writer = new PrintWriter(pointPath);
		for(int i = 1; i<size +1 ; i++){
			 writer.write( i + "\n");
			 writer.flush(); 
		} 
		writer.close();
	}
	public static List<Integer> getPoints(String pointPath) {
		String line = 	"";
		List<Integer> datas = new ArrayList<Integer>();
		try{
			BufferedReader in = new BufferedReader(new FileReader(pointPath));
			while((line = in.readLine()) != null) {
				String[] spt = line.split(" +");
			 
				int x = Integer.parseInt(spt[0]);
				 datas.add(x);
			}
			in.close();
		}catch (IOException e) {
				e.printStackTrace();
			}
		return datas;
	}
	public static double[][] getDistMatrix(String distancePath) {
		double[][] distMatrix = new double[5000][5000];
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(distancePath)); 
			while((line = in.readLine()) != null) {
				if(line.trim().length() == 0) {
					continue;
				}
				String[] spt = line.split(" +");
				int index1 = Integer.parseInt(spt[0]);
				int index2 =Integer.parseInt(spt[1]);
				double ident = Double.parseDouble(spt[2]);
				distMatrix[index1][index2] = ident; 
			}  
			in.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return distMatrix; 
	}

}
