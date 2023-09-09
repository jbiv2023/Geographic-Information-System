// Represents a point in the xy-plane with integer-valued coordinates.
// Supplies comparison functions specified in the Compare2D interface.
//

//    On my honor:
//
//    - I have not discussed the Java language code in my program with
//      anyone other than my instructor or the teaching assistants
//      assigned to this course.
//
//    - I have not used Java language code obtained from another student,
//      or any other unauthorized source, including the Internet, either
//      modified or unmodified.
//
//    - If any Java language code or documentation used in my program
//      was obtained from another source, such as a text book or course
//      notes, that has been clearly noted with a proper citation in
//      the comments of my program.
//
//    - I have not designed this program in such a way as to defeat or
//      interfere with the normal operation of the grading code.
//
//    <Joey Bush>
//    <joeybushiv>

public class Point implements Compare2D<Point> {

    public long xcoord;
    public long ycoord;
    public long offset;
    
    public Point() {
       xcoord = 0;
       ycoord = 0;
    }
    public Point(long x, long y, long offset) {
        xcoord = x;
        ycoord = y;
        this.offset = offset;
     }
    
    public Point(long x, long y) {
       xcoord = x;
       ycoord = y;
      
    }
    public long getX() { // getting the dataObject x value
       return xcoord;
    }
    public long getY() {    // getting the dataObject y value
       return ycoord;
    }
    public long getOffset() {
        return offset;
    }
    
    public Direction directionFrom(long X, long Y) {
       // Implement this method...
      
      long centerX = X;     // center x
      long centerY = Y;     // center y
      
      long dataObjectX = getX();    // dataObject x
      long dataObjectY = getY();    // dataObject y
      
      // test cases
      if(  dataObjectX >= centerX  && dataObjectY >= centerY) {
          return Direction.NE;
      }
      else if(dataObjectX <= centerX && dataObjectY >= centerY) {
          return Direction.NW;
      }
     /* else if(dataObjectX == centerX && dataObjectY == centerY) {
          return Direction.NE; 
      }
      else if(dataObjectX < centerX && dataObjectY > centerY) {
          return Direction.NW; 
      }
      else if(dataObjectX < centerX && dataObjectY > centerY) {
          return Direction.NW;
      }*/
      else if(dataObjectX <= centerX && dataObjectY <= centerY) {
          return Direction.SW;
      }
      else if(dataObjectX >= centerX && dataObjectY <= centerY) {
          return Direction.SE;
      }
      return Direction.NOQUADRANT;

   
        
    }
    
    public Direction inQuadrant(double xLo, double xHi, double yLo, double yHi) {
       
       // Implement this method...
     
      if(inBox(xLo,xHi,yLo,yHi)){   // if the data object is inside the rectangle than do this
                                            // imagine a rectangle
         double centerX = (xLo + xHi)/2;    // center x 
         double centerY = (yLo+yHi)/2;      // center y
         
        return directionFrom((long)centerX,(long)centerY);
         
      }

       return Direction.NOQUADRANT;
    }
    
    public boolean   inBox(double xLo, double xHi, double yLo, double yHi) {
       
       
      // Implement this method...
        
      //System.out.println(String.format("%f, %f, %f, %f", xLo, xHi, yLo, yHi));
      long x = getX();  // gets the X component of the data object
      long y = getY();  // gets the Y  component of the data object
      //System.out.println(x + ", " + y); 

      
      if((x >= xLo && x <= xHi) && (y >= yLo && y <= yHi)){  // if the x an y values are inside or on the rectangle line than return true
         return true;
      }

      return false;
       
    }
    
    public String toString() {
        
       // Do not change...
       return new String("(XCord: " + xcoord + ", YCord: " + ycoord + ", Offset: " + offset + ")");
    }
    
    public boolean equals(Object o) {
       
      // Implement this method...
        if (o== this) {
            return true;
        }
        if ((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }
        
        return getX() == (((Point)o).getX()) && getY() == (((Point)o).getY()) ;
         
    }
}
