import java.io.IOException;
import java.util.ArrayList;

public class CoordinateIndex {

    long westLong;
    long eastLong;
    long southLat;
    long northLat;

    long x;
    long y;
    long offset;
    prQuadTree<Point> tree = null;

    private String value1;
    private String value2;
    
    /*
     * using getters and setters for our values
     */

    public String getValue1() {
        return value1;
    }


    public void setValue1(String value1) {
        this.value1 = value1;
    }


    public String getValue2() {
        return value2;
    }


    public void setValue2(String value2) {
        this.value2 = value2;
    }


    CoordinateIndex(String value1, String value2) {

        this.value1 = value1;
        this.value2 = value2;
    }


    CoordinateIndex(long x, long y, long offset) {

        this.x = x;
        this.y = y;
        this.offset = offset;
    }


    CoordinateIndex(
        long westLong,
        long eastLong,
        long southLat,
        long northLat) {

        this.westLong = westLong;
        this.eastLong = eastLong;
        this.southLat = southLat;
        this.northLat = northLat;
        tree = new prQuadTree<Point>(westLong, eastLong, southLat, northLat);
    }
/*
 * adding data points to our tree
 */

    public void addToTree(long x, long y, long offset) {

        Point obj = new Point(x, y, offset);

        tree.insert(obj);
       

    }
    /*
     * checks if its in our tree
     */
    
    public ArrayList<Point> findInTree(long xLo, long xHi, long yLo, long yHi) {
       // Point obj = new Point(first, second);
        
        ArrayList<Point> list = tree.find(xLo, xHi, yLo, yHi);
        
        return list;
    }

/*
 * If our data points are inside, then return them
 */
    public ArrayList<Point> stringDisplay() throws IOException {

        ArrayList<Point> toStringTreeObj = tree.display(westLong, eastLong,
            southLat, northLat);

        return toStringTreeObj;
    }
/*
 * gets the specific values in the North west Region 
 * 
 */

    public ArrayList<Point> getNorthWestObjs(
        long westLong,
        long eastLong,
        long southLat,
        long northLat)
        throws IOException {
        ArrayList<Point> toStringTreeObj = tree.display(westLong, eastLong,
            southLat, northLat);

        return toStringTreeObj;
    }

    /*
     * finding a specific object
     */

    public Point findObj(Point elem) {
        long xx = elem.getX();
        long yy = elem.getY();
        return tree.findCoordinate(xx, yy);

    }

}
