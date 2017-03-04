package myKMeans;

public class Distance implements Comparable<Distance> {  
    private Point source;  
    private Point dest;  
    private double dis;  // pre-calucate the distance of two sequence and store into a L*L matrix
    private AbstractDistance distance;  
  
    public Distance(Point source, Point dest, AbstractDistance distance, double[][] distMatrix) {  
        this.source = source;  
        this.dest = dest;  
        this.distance = distance;  
        this.dis = distance.getDis(source, dest, distMatrix);  
    }  
    
    public Distance(Point source, Point dest, double dis) {
		this.source = source;
		this.dest = dest;
		this.dis = dis;
	}
  
    public Point getSource() {  
        return source;  
    }  
    
  
    public Point getDest() {  
        return dest;  
    }  
  
    public double getDis() {  
        return dis;  
    }  
  
    @Override  
    public int compareTo(Distance o) {  
        if (o.getDis() > dis)  
            return -1;  
        else  
            return 1;  
    }  
}