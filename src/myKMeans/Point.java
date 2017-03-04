package myKMeans;

public class Point {  
    private int x;  //x is the certain index ,eg. 1,2 ,3 ..., so two point compose the sequence pair
    private int y;  // init is 0
    private int id;  // thought two point may contain some x ,but the id can be same
    private boolean beyond;//identify the data whether beyond to the original sample datas 
  
    public Point(int id, int x, int y) {  
        this.id = id;  
        this.x = x;  
        this.y = y;  
        this.beyond = true;  
    }  
  
     public  Point() { 
    	 
	}
    public Point(int id, int x, int y, boolean beyond) {  
        this.id = id;  
        this.x = x;  
        this.y = y;  
        this.beyond = beyond;  
    }  
  
    public int getX() {  
        return x;  
    }  
  
    public double getY() {  
        return y;  
    }  
  
    public int getId() {  
        return id;  
    }  
  
    public boolean isBeyond() {  
        return beyond;  
    }  
  
    @Override  
    public String toString() {  
        return "Point{" +  
                "id=" + id +  
                ", x=" + x +  
                ", y=" + y +  
                '}';  
    }  
  
    @Override  
    public boolean equals(Object o) {  
        if (this == o) return true;  
        if (o == null || getClass() != o.getClass()) return false;  
  
        Point point = (Point) o;  
  
        if (Double.compare(point.x, x) != 0) return false;  
        if (Double.compare(point.y, y) != 0) return false;  
  
        return true;  
    }  
  
    @Override  
    public int hashCode() {  
        int result;  
        long temp;  
        temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;  
        result = (int) (temp ^ (temp >>> 32));  
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;  
        result = 31 * result + (int) (temp ^ (temp >>> 32));  
        return result;  
    }  
}
