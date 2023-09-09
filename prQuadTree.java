import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// To support testing, we will make certain elements of the generic.
//
// You may safely add data members and function members as needed, but
// you must not modify any of the public members that are shown.
//

// On my honor:
//
// - I have not discussed the Java language code in my program with
// anyone other than my instructor or the teaching assistants
// assigned to this course.
//
// - I have not used Java language code obtained from another student,
// or any other unauthorized source, including the Internet, either
// modified or unmodified.
//
// - If any Java language code or documentation used in my program
// was obtained from another source, such as a text book or course
// notes, that has been clearly noted with a proper citation in
// the comments of my program.
//
// - I have not designed this program in such a way as to defeat or
// interfere with the normal operation of the grading code.
//
// <Joey Bush>
// <joeybushiv>
public class prQuadTree<T extends Compare2D<? super T>> {

    // Inner classes for nodes (public so test harness has access)
    public final int BUCKET_SZ = 4;

    public abstract class prQuadNode {

    }


    public class prQuadLeaf extends prQuadNode {
        // Use an ArrayList to support a bucketed implementation later.
        ArrayList<T> Elements;

        public prQuadLeaf() {
            Elements = new ArrayList<>();
        }
    }


    public class prQuadInternal extends prQuadNode {
        // Use base-type pointers since children can be either leaf nodes
        // or internal nodes.
        prQuadNode NW, NE, SE, SW;
    }

    // prQuadTree elements (public so test harness has access)
    public prQuadNode root;
    public long xMin, xMax, yMin, yMax;

    // Add private data members as needed...

    // Initialize quadtree to empty state, representing the specified region.
    // Pre: xMin < xMax and yMin < yMax
    public prQuadTree(long xMin, long xMax, long yMin, long yMax) {
        this.root = null;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

    }


    // Pre: elem != null
    // Post: If elem lies within the tree's region, and elem is not already
    // present in the tree, elem has been inserted into the tree.
    // Return true iff elem is inserted into the tree.
    public boolean insert(T elem) {
        // Implement this method...

        boolean inTree = elem.inBox(xMin, xMax, yMin, yMax);
        //T duplicate = find(elem);

        //prQuadLeaf checking = new prQuadLeaf();
       // boolean duplicate = checking.Elements.contains(elem);
        
        //System.out.println(checking.Elements.size() + " this is the size");
        
        // use the find method here
        if (inTree != true || elem == null ) { // element is in the range and the
                                              // element is null

            return false;
        }

        root = helperInsert(root, elem, xMin, xMax, yMin, yMax);
        
        

        return true;
    }


    @SuppressWarnings("unchecked")
    private prQuadNode helperInsert(
        prQuadNode ugh,
        T element,
        long xMin,
        long xMax,
        long yMin,
        long yMax) {

        if (ugh == null) { // Check to see if the root is null

            prQuadLeaf leaf = new prQuadLeaf(); // creating a new leaf

            // just check for doubles

            if (leaf.Elements.size() <= BUCKET_SZ) {
                leaf.Elements.add(element); // adds elements
            }
            return leaf; // return leaf

        }

        else if (ugh.getClass().equals(prQuadLeaf.class)) { // checks to see if
                                                            // the classes are
                                                            // equal for when
                                                            // the root is the
                                                            // leaf

            prQuadLeaf lf = (prQuadLeaf)ugh;

            prQuadInternal internal = new prQuadInternal();

            if (element.equals(lf.Elements.get(0))) { // if the element is equal
                                                      // to the data object

                return ugh;
            }

            helperInsert(internal, lf.Elements.get(0), xMin, xMax, yMin, yMax); // first
                                                                                // iteration
                                                                                // call

            helperInsert(internal, element, xMin, xMax, yMin, yMax); // second
                                                                     // iteration
                                                                     // call

            return internal;
        }

        else { // this was the tricky part in seeing where the element was in
               // regard to the parameters

            @SuppressWarnings("unchecked")
            prQuadInternal internal = (prQuadInternal)ugh;
            Direction where = element.inQuadrant(xMin, xMax, yMin, yMax); // where
                                                                          // the
                                                                          // element
                                                                          // is
            // checking the directions
            if (where == Direction.NE) {

                internal.NE = helperInsert(internal.NE, element, (xMin + xMax)
                    / 2, xMax, (yMin + yMax) / 2, yMax);
            }
            if (where == Direction.NW) {

                internal.NW = helperInsert(internal.NW, element, xMin, (xMin
                    + xMax) / 2, (yMin + yMax) / 2, yMax);
            }
            if (where == Direction.SE) {

                internal.SE = helperInsert(internal.SE, element, (xMin + xMax)
                    / 2, xMax, yMin, (yMin + yMax) / 2);
            }
            if (where == Direction.SW) {

                internal.SW = helperInsert(internal.SW, element, xMin, (xMin
                    + xMax) / 2, yMin, (yMin + yMax) / 2);

            }
            return internal;
        }

    }


    // Pre: elem != null
    // Returns reference to an element x within the tree such that
    // elem.equals(x)is true, provided such a matching element occurs within
    // the tree; returns null otherwise.
    public T find(T elem) {

        if (elem == null || !elem.inBox(xMin, xMax, yMin, yMax)) {
            return null;
        }

        return (T)findHelper(root, elem, xMin, xMax, yMin, yMax);

    }


    private T findHelper(
        prQuadNode root,
        T elem,
        long xMin,
        long xMax,
        long yMin,
        long yMax) {
        prQuadInternal internal = new prQuadInternal();
        prQuadLeaf leaf = new prQuadLeaf();

        Direction direction = elem.inQuadrant(xMin, xMax, yMin, yMax);

        // checks to see if the root is null
        if (root == null) {
            return null;
        }

        // when the root is not an internal node then do u keep going until you
        // find a leaf
        if (!root.getClass().equals(leaf.getClass())) {

            if (direction == Direction.NE) {
                findHelper(internal.NE, elem, (xMin + xMax) / 2, xMax, (yMin
                    + yMax) / 2, yMax);
            }
            else if (direction == Direction.NW) {
                findHelper(internal.NW, elem, xMin, (xMin + xMax) / 2, yMin,
                    (yMin + yMax) / 2);
            }
            else if (direction == Direction.SW) {
                findHelper(internal.SW, elem, xMin, (xMin + xMax) / 2, yMin,
                    (yMin + yMax) / 2);
            }
            else {
                findHelper(internal.SE, elem, (xMin + xMax) / 2, xMax, yMin,
                    (yMin + yMax) / 2);
            }

        }
        // check if the leaf equals the element found
        else {
            leaf = (prQuadLeaf)root;
            if (leaf.Elements.get(0).equals(elem)) {

                return leaf.Elements.get(0);

            }
        }

        return null;

    }


    // Pre: xLo < xHi and yLo < yHi
    // Returns a collection of (references to) all elements x such that x is
    // in the tree and x lies at coordinates within the defined rectangular
    // region, including the boundary of the region.
    public ArrayList<T> find(long xLo, long xHi, long yLo, long yHi) {

        // Implement this method...

        ArrayList<T> arrayList = new ArrayList<T>();
        long paramxMin = xLo;
        long paramxMax = xHi;
        long paramyMin = yLo;
        long paramyMax = yHi;

        return findArrayListHelper(root, paramxMin, paramxMax, paramyMin,
            paramyMax, arrayList);

    }


    public ArrayList<T> findArrayListHelper(
        prQuadNode root,
        long xLo,
        long xHi,
        long yLo,
        long yHi,
        ArrayList<T> arrayList) {

        long paramxMin = xLo;
        long paramxMax = xHi;
        long paramyMin = yLo;
        long paramyMax = yHi;
        
        if(root == null) {
            return null;
        }

        // if the classes are equal, aka, the root is the leaf then do this
        if (root.getClass().equals(prQuadLeaf.class)) {

            @SuppressWarnings("unchecked")
            prQuadLeaf leaf = (prQuadLeaf)root; // casts the root to a leaf

            boolean inBox = leaf.Elements.get(0).inBox(xLo, xHi, yLo, yHi); // checks
                                                                            // to
                                                                            // see
                                                                            // if
                                                                            // the
                                                                            // element
                                                                            // is
                                                                            // in
                                                                            // the
                                                                            // box

            if (inBox) { // if the element is in the boxx then add the element
                arrayList.add(leaf.Elements.get(0));
            }
        }

        // but if they are not, like if the root is an internal node then you
        // keep recessively calling the helper function
        if (!root.getClass().equals(prQuadLeaf.class)) {
            @SuppressWarnings("unchecked")
            prQuadInternal internal = (prQuadInternal)root;

            if (internal.NE != null) {
                findArrayListHelper(internal.NE, paramxMin, paramxMax,
                    paramyMin, paramyMax, arrayList);
            }
            if (internal.NW != null) {
                findArrayListHelper(internal.NW, paramxMin, paramxMax,
                    paramyMin, paramyMax, arrayList);
            }
            if (internal.SE != null) {
                findArrayListHelper(internal.SE, paramxMin, paramxMax,
                    paramyMin, paramyMax, arrayList);
            }
            if (internal.SW != null) {
                findArrayListHelper(internal.SW, paramxMin, paramxMax,
                    paramyMin, paramyMax, arrayList);
            }

        }
      

        return arrayList;
    }


   /* public ArrayList<String> display(
        long westLong,
        long eastLong,
        long southLat,
        long northLat)
        throws IOException {

        return searchDisplay(westLong, eastLong, southLat, northLat);

    }*/


    public ArrayList<T> display(
        long westLong,
        long eastLong,
        long southLat,
        long northLat) {
        
        return find(westLong, eastLong, southLat, northLat);
        
       

    }


    public T findCoordinate(long x, long y) {

      
        ArrayList<T> list = find(xMin, xMax, yMin, yMax);

        for (T o : list) {
            if (x == o.getX() && y == o.getY()) {
                return o;
            }
        }
        return null;

    }
    /*
     * I did not display the tree properly, I ran out of time... :(
     */
    
    public void printTreeHelper(prQuadNode sRoot, String Padding) {
     prQuadInternal Internal = new prQuadInternal();
     prQuadLeaf Leaf = new prQuadLeaf();
        // Check for empty leaf
     if ( sRoot == null ) { System.out.println(Padding + "*\n"); return;
     }
        // Check for and process SW and SE subtrees
     if ( sRoot.getClass().equals(Internal.getClass()) ) { prQuadInternal p = (prQuadInternal) sRoot; printTreeHelper(p.SW, Padding + " "); printTreeHelper(p.SE, Padding + " ");
     }
        // Display indentation padding for current node
     System.out.println(Padding);
     // Determine if at leaf or internal and display accordingly
     if ( sRoot.getClass().equals(Leaf.getClass()) ) { prQuadLeaf p = (prQuadLeaf) sRoot; System.out.println( Padding + p.Elements);
     }
     else
     System.out.println( Padding + "@\n" );
    }



}
