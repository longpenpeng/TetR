package myKMeans; 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cluster {  
    private int id;//id 
    private Point center;//  
    private List<Point> members = new ArrayList<Point>();//
    private List<Integer > xcoords = new ArrayList<Integer>(); 
  
    public Cluster(int id, Point center) {  
        this.id = id;  
        this.center = center;  
        this.xcoords.add(center.getX());
        this.addPoint(center);
        if(!this.members.contains(center)){
        	try{
        		throw new Exception();
        	}catch(Exception e) {
        		System.out.println("the members do not contain the center point!  "
        				+ e.getMessage());
        	}
        }
    }
  
    public Cluster(int id, Point center, List<Point> members ) {  
        this.id = id;  
        this.center = center;  
        this.members = members;  
        this.xcoords.add(center.getX()); 
    }  
  
    public void addPoint(Point newPoint ) {  
        if (!members.contains(newPoint))  {
        	members.add(newPoint);  
        	xcoords.add(newPoint.getX());  
        } 
        else  {
            System.out.println("********new Point  is :"+newPoint.getX()+
            		"  "+this.center.getX()); 
            throw new IllegalStateException("don't try to handle the same data point!");  
        } 
    }  
  
    public double calAverDist(List<Point> members, Point center,MyCluster kmc) 
    { 
    	double sum = 0;
    	int index1;
    	int index2;
    	for(Point p1:members) { 
    		index1 = p1.getX();
    		for(Point p2 :members) { 
    			index2 = p2.getX();
    			if(index1 == index2) {
    				continue;
    			}
    			sum  = sum + kmc.getDistance(index1, index2);
    		}
    	}
    	for(Point p1:members) {
    		sum = sum + kmc.getDistance(p1, center);
    	}
    	double aver = 2*sum/(members.size()*(members.size() + 1));
    	return aver;
    	
    }
    public int getId() {  
        return id;  
    }  
     
    public Point getCenter() {  
        return center;  
    }  
    
    public List<Integer> getXCoord()	{
    	return this.xcoords;
    }
  
    public void setCenter(Point center) {  
        this.center = center;  
    }  
    
  
    public List<Point> getMembers() {  
        return members;  
    }  
  
//    @Override  
//    public String toString() {  
//        return "Cluster{" +  
//                "id=" + id +  
//                ", center=" + center +  
//                ", members=" + members +  
//                "}";  
//    }  
    @Override
    public String	 toString() {
    	String out = "";
    	out = out +  "###Cluster" + "id=" + id + 
    			" member number(include center):" + (members.size() + 1) + " , center=" + center + "\n"; 
    	Iterator<Point> iter = members.iterator();
    	Point p;
    	while(iter.hasNext()) {
    		p = iter.next();
    		out = out + p.getX() + "\n";
    	}
    	return out;
    }
    
}
