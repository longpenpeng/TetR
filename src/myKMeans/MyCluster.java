/*Copyright(c), 2016/11/24*/
package myKMeans;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map; 
import java.util.Set;
import java.util.TreeSet;

import io.PrintUtils;
/**
 * the {@code MyCluster} class contain the majority method of cluster process
 * <p>
 * the clustering strategy here is very simply.Initially, the first point set
 * as a center, and the subsequent point compare the distance with the first 
 * center.If distance is larger then cutoff, the latter point serve as the second
 *  center, otherwise, take the latter as one member of the former center.
 * And then ,repeat the above process and go through whole point contained in
 * data.For the second clustering, at the beginning, remove the cluster whose
 * member is quite litter(I set the 10 as a default), and calculate the new center 
 * of each cluster.Then, each data stored in the member variable data compare
 * with the remained center, and allocate the point into near cluster.this is the
 * basic strategy.
 * <blockquote><pre>
 *    the input data format : 1 2  0.2
 *    first two number is the x coordinate( or the index of the data), the last number 
 *    is the distance
 * @author ppLong 
 */

public class MyCluster {
	/**iteration number*/
	private int num = 50;
	/**cache the original datas */
	private List<Integer> datas;
	/**cache the point translate from the original datas*/
	private List<Point> data = new ArrayList<Point>();
	/**file address of the input data file*/
	private  String address ;
	/**cache the distance matrix*/
	private double[][] distMatrix;
	/** define the distance calculate method, here the distance is simply get
	 * from the distance matrix
	 */
	private AbstractDistance distance = new AbstractDistance() {  
        @Override  
        public double getDis(Point p1, Point p2, double[][]  distMatrix) {   
        	return getDistance(p1, p2);
			} 
        } ; 
        
        /**Initialize  a {@code MyCluster} instance*/
        public MyCluster( List<Point> data) { 
        	this.data = data;
        }
        
        public MyCluster() {
        	
        }
        
        public MyCluster(int num, List<Integer> datas) {
        	this.num = num;
        	this.datas = datas;
        }
        
        public MyCluster(double[][] distMatrix, List<Integer> datas) {
        	this.datas = datas;
        	this.distMatrix = distMatrix;
        } 
        
        public MyCluster(double[][] distMatrix) {
        	this.distMatrix = distMatrix;
        }
        
        private void check() {  
//          if (k == 0)  
//              throw new IllegalArgumentException("k must be the number > 0");   
          if (address == null && datas == null)  
              throw new IllegalArgumentException("program can't get real data");  
      }  
        
        public List<Cluster> runCluster(double cutoff) throws FileNotFoundException{
        	init();
        	List<Cluster>  firstCluster= firstCluster(cutoff);
        	List<Cluster> cluster = cluster(firstCluster, cutoff);
        	return cluster;  
        }
        /** 
         *init the data
         * 
         * @throws java.io.FileNotFoundException 
         */  
        public void init() throws FileNotFoundException {  
            check();  
            //init data  
            //handle original sample  
            for (int i = 1, j = datas.size(); i < j; i++)  
                data.add(new Point(i, datas.get(i), 0));  
            System.out.println(data.get(0).toString());
            System.out.println(data.get(data.size()-2).toString());
        }  
        /** 
         * @param center 
         * @return 
         */  
        public List<Cluster> prepare(Set<Point> center) {  

            List<Cluster> cluster = new ArrayList<Cluster>();  
            Iterator<Point> it = center.iterator();  
            int id = 0;  
            while (it.hasNext()) {  
                Point p = it.next();  
                if (p.isBeyond()) {  
                    Cluster c = new Cluster(id++, p);   
                    cluster.add(c);    
                } else  
                    cluster.add(new Cluster(id++, p));  
            }  
            return cluster;  
        }
        /**
         * 
         * @param cutoff
         * @return
         */
        
        public List<Cluster> firstCluster( double cutoff){
    	  List<Cluster> clusters = new ArrayList<Cluster>(); 
    	  List<Point> centers = new ArrayList<Point>();
    	  TreeSet<Distance> distances = new TreeSet<Distance>();
    	  Cluster c;
    	  Point source;
    	  Point dest;
    	  int id = 0;
    	  boolean flag = false;
    	  boolean bo ;
    	  PrintUtils.printInfoToStdOut("*****************first clustering !****************");
    	  for(int i = 0, n = data.size(); i < n; i++){
    		  distances.clear();  
    		  if(i == 0) {
    			  centers.add(data.get(i));    
    			  c = new Cluster( id ++, data.get(i));
    			  clusters.add(c);
    			  continue;
    		  } 
    		  for(int j = 0; j < centers.size(); j++) {
    			  if(centers.contains(data.get(i))) {
    				  break;
    			  }
    			  flag = true;
    			  source = data.get(i);
    			  dest = centers.get(j);
    			  distances.add(new Distance(source, dest, distance, distMatrix));
    		  }
    		  if(flag == true)  {
    			  Distance max = distances.last();
    			  bo = true;
    			  for(int m = 0; m<clusters.size(); m ++)  {
    				  if(clusters.get(m).getCenter().equals(max.getDest()))  {
    					  if(filter(max.getSource(), clusters.get(m), cutoff)) {
    						  clusters.get(m).addPoint(max.getSource()); 
    					  }else  {
    						  bo = false;
    					  }
    				  }
    			  }
    			  if(!bo) { 
					  centers.add(data.get(i));  
					  c = new Cluster( id ++, data.get(i));
					  clusters.add(c);
    			  }
    			  flag = false; 
    		  }
    	  }                         
    	  PrintUtils.printInfoToStdOut("***************first clustering finished !**************");
    	//  PrintUtils.printInfoToStdOut(String.format("cluster number: " +clusters.size())); 
    	  return clusters;
      } 
        public List<Cluster> clustering(Set<Point> center, List<Cluster> cluster
    		  ,double cutoff) {  
          Point[] p = center.toArray(new Point[0]);   
          TreeSet<Distance> distence = new TreeSet<Distance>();//
          Point source;  
          Point dest;  
          Cluster c;
          int id = 0;
          boolean bo = true;
          boolean flag = false;  
          PrintUtils.printCurTime("length of the center set:  " +p.length + 
    			  "  " +center.size());
          for (int i = 0, n = data.size(); i < n; i++) {  
              distence.clear();   
              for (int j = 0; j <center.size() ; j++) {   
                  if (center.contains(data.get(i)))  
                      break;   
                  flag = true;  
                  // calculate the distance  
                  source = data.get(i);  
                  dest = center.toArray(new Point[0])[j];  
                  distence.add(new Distance(source, dest, distance, distMatrix));  
              }  
              if (flag == true) {  
                  Distance max = distence.last();  //Distance min = distance.first();  
                  for (int m = 0, k = cluster.size(); m < k; m++) {  
                      if (cluster.get(m).getCenter().equals(max.getDest()))  {
                      	if(filter(max.getSource(), cluster.get(m), cutoff)) {  
                      		cluster.get(m).addPoint(max.getSource());
                      	}else { 
                      		 bo = false;
                      		break;
  						}    
                  }  
                } 
                  if(!bo) { 
					  center.add(data.get(i));  
					  c = new Cluster( id ++, data.get(i));
					  cluster.add(c);
    			  }
                  bo = true;
                  flag = false;  
          }  
          }
          PrintUtils.printStar("the total cluster member :" + totalPoint(cluster)+ "data size:"+ data.size());
          return cluster;  
      }  
  
        public List<Cluster> cluster(List<Cluster> cluster, double cutoff) {   
        	
        Set<Point> lastCenter = new HashSet<Point>();  
        List<Integer> removeId ;
        Set<Point> simplfCenter;
       // Set<Point> unremovedCenter;
        List<Cluster> unremovedCluster = new ArrayList<Cluster>();
        for (int m = 0; m < num; m++) {  
//            error = 0;  
            simplfCenter = new HashSet<Point>(); //store the centers after removing   
            removeId = new ArrayList<Integer>();
            for (int j = 0; j < cluster.size(); j++) {  
                List<Point> ps = cluster.get(j).getMembers();  
                int size = ps.size()+1;   
                Point newCenter = newCenter(ps);
               // unremovedCenter.add(newCenter); 
                if(m < num -2){
                	 if (size < 10) {   
                     	//center.add(cluster.get(j).getCenter());  
                		 removeId.add(j); 
                         continue;  
                     }  
                }   
                simplfCenter.add(newCenter);   
            }   
            if (lastCenter.containsAll(simplfCenter))//quit iteration while the center is same with the last center 
                break;  
            lastCenter =simplfCenter;   
            cluster = clustering(simplfCenter,prepare(simplfCenter), cutoff);   
            unremovedCluster = new ArrayList<Cluster>();
            unremovedCluster = cluster;
        }  
        PrintUtils.printStar("unremoved cluster:" + totalPoint(unremovedCluster));
        return unremovedCluster;  
    }  
        public Point	newCenter( List<Point> ps) {
 		 int size  = ps.size();
 		 int index1;
 		 int index2; 
 		 double sum = 0.0;
 		 double aver;  
 		 Map<Point, Double> p2Aver  = new HashMap<Point, Double>();;
 		 for(int i = 0; i< size; i ++) {   
 			 sum = 0.0;
 			 index1 = ps.get(i).getX();
 			 for(int j = 0; j < size; j++) {
 				 index2 = ps.get(j).getX();
 				 if(index1 == index2) {
 					 continue;
 				 }
 				 sum += getDistance(index1, index2); 
 			 }
 			 aver = sum/(size-1);
 			 p2Aver.put(ps.get(i), aver);  
 		 }  
 		 Point newCenter = (Point)sortByValue(p2Aver).keySet().toArray()[p2Aver.size() -1];
 		 return newCenter; 
 	}
      
      	public double getDistance(int index1, int index2) {
  		if(index1 > index2) {
  			return distMatrix[index2][index1];
  		}else if(index1 == index2) {
  			return 1.0;
  		}else {
  			return distMatrix[index1][index2];
  		} 
  	}
        
        public  double getDistance(Point p1, Point p2) {
        	int index1 = p1.getX();
        	int index2 = p2.getX();
        	if(index1 > index2) {
    			return distMatrix[index2][index1];
    		}else if(index1 == index2) {
    			return 1.0;
    		}else {
    			return distMatrix[index1][index2];
    		} 
        }
        
        public static <K,V extends Comparable<? super V>>  Map<K,V> sortByValue(Map<K,V> map) {
        	List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        	Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
        		@Override
        		public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
        			return (o1.getValue()).compareTo((o2.getValue()));
        		}
    		});
        	Iterator<Map.Entry<K, V>> iter = list.iterator();
        	Map<K, V> sortedMap  = new LinkedHashMap<K,V>();
        	while(iter.hasNext()) {
        		Map.Entry< K, V> entry = iter.next();
        		sortedMap.put(entry.getKey(), entry.getValue()); 
        	}
        	return sortedMap;
    	}
        public boolean filter(Point newPoint, Cluster cluster, double cutoff) {
        	List<Point> member = cluster.getMembers(); 
        	Point p ; 
        	for(int i = 0; i<member.size(); i++){
        		p = member.get(i);
        		if(getDistance(newPoint, p) < cutoff) {
        			return   false; 
        		}
        	}
        	return true;
        }
        public int totalPoint(List<Cluster> clusters) {
        	Iterator<Cluster> it = clusters.iterator();
        	int sum = 0;
        	while(it.hasNext()){
        		sum += it.next().getMembers().size();
        	}
        	return sum;
        }

}
