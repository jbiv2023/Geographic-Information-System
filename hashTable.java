import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class hashTable<T extends Hashable<T> > {
        private ArrayList< LinkedList<T> > table;
        private Integer numElements = 0;
        private Double loadLimit = 0.7;
       
        private final Integer defaultTableSize = 256; 
        //old size was 256
        private int maxSlotLength;
        
       
        // rehash when .7 * defaultTableSize == number of elements
        
      
        
        // default number of table slots
        /** Constructs an empty hash table with the following properties:
         *  Pre:
         *    - size is the user's desired number of lots; null for default
         *    - ldLimit is user's desired load factor limit for resizing the table;
         *      null for the default
         *  Post:
         *    - table is an ArrayList of size LinkedList objects, 256 slots if
        * size == null
         *    - loadLimit is set to default (0.7) if ldLimit == null
         */
        //elements/tablesize
        
        public hashTable(Integer size, Double ldLimit) {        
            
            if(size == null) {                      // if the size is null use defaultsize
                size = defaultTableSize;
                table = new ArrayList<LinkedList<T>>(size);
                
               // size = defaultTableSize;
            }   
            if(size!=null) {                        // if its not null just use the size given
                
                table = new ArrayList<LinkedList<T>>(size);
            }
             
          
               

            if(ldLimit != null) {                       // when the ldLimit isnt null then make it load limit
                loadLimit = ldLimit;    
            }
           
            
            //do for loop set values to new linked list
           
            for(int i = 0; i < size; i++) {             // want to add dummy nodes to our hash table of size
           
                table.add(new LinkedList<T>());
            }
            
            
        }
        
        
private void rehash (ArrayList<LinkedList<T>> resize) {
            
            ArrayList<LinkedList<T>> old = resize;
            
            table = new ArrayList< LinkedList<T> >(table.size()*2); //double the size of the hassh
            numElements = 0;
            maxSlotLength = 0;
            for(int i = 0; i < old.size()*2; i++) {             // putting old values in the new bigger table
                
                table.add(new LinkedList<T>());
            }
           
            for (LinkedList<T> linkedList : old) {              // inserting in the linked list
                for (T joey : linkedList) {               
                    insert(joey);       
                }       
            }  
        }
        /** Inserts elem at the front of the elem's home slot, unless that
        * * * * * * * * * *
        slot already contains a matching element (according to the equals()
        method for the user's data type.
        Pre:
          - elem is a valid user data object
        Post:
          - elem is inserted unless it is a duplicate
          - if the resulting load factor exceeds the load limit, the
            table is rehashed with the size doubled
        Returns:
          true iff elem has been inserted
*/
public boolean insert(T elem) { 
      
        if(elem == null ) {
            return false;
        } 
        
        
        
       int homeslot = elem.Hash() % table.size(); // this is my home slot
       if(table.get(homeslot).contains(elem)) {     // duplicate
           return false;
       }
       table.get(homeslot).add(elem);
       numElements = numElements + 1 ;
       
       if(table.get(homeslot).size() > maxSlotLength) {     // changing thhe maxSlotlength 
           maxSlotLength = table.get(homeslot).size();
       }
      
       if(numElements > loadLimit*table.size()) {       // rehashing the table
           rehash(table);
       }
       
       return true;

    }

    
    public T find(T elem) { 
       
        int hash = elem.Hash() % table.size();      //getttng the homeslot
        LinkedList<T> link = table.get(hash);
       
        for (T joey : link) {                       // goes through the list and if it finds it, returns it
          
            if(joey.equals(elem)) {
                return joey;
            }
        }
        return null;
    }
    
        
//public T remove(T elem) { 
    
      
  //} // Not necessary for this assignment
       /**  Writes a formatted display of the hash table contents.
        *   Pre:
        *     - fw is open on an output file
        */
public void display(BufferedWriter fw) throws IOException {     // i was given this
          fw.write("       show         hash \n");
          fw.write("Number of elements: " + numElements + "\n");
          fw.write("Number of slots: " + table.size() + "\n");
          fw.write("Maximum elements in a slot: " + maxSlotLength + "\n");
          fw.write("Load limit: " + loadLimit + "\n");
          fw.write("\n");
          fw.write("Slot    Contents\n");
          for (int idx = 0; idx < table.size(); idx++) { 
              
              LinkedList<T> curr = table.get(idx);
              
              if ( curr != null && !curr.isEmpty() ) {
                      //System.out.println(curr.size());
                       fw.write(String.format("%5d:  %s\n", idx, curr.toString()));
          } 
              }
          fw.write("\n");
         //  Implementation above conforms to the suggested hash table design; if
         //  you make changes to the class design above, you must supply a display
         //  function that writes the same information as this one.
         
      
       
    } 
}
