import java.util.ArrayList;

public class NameEntry implements Hashable<NameEntry> {
    String nameEntry;                 // GIS feature name
   
    ArrayList<Object>locations;   // file offsets of matching records
    int MaxSize = 0;
    /**
     * Initialize a new nameEntry object with the given feature name
     * and a single file offset.
     */
  
    public NameEntry(String nameEntry, Long offset) {
        this.nameEntry = nameEntry;                 // key is the name
      
        
        locations = new ArrayList<Object>();  // just declaring the locations
        locations.add(offset);              // adding the offset to the list
        
    }


    /**
     * Return feature name.
     */
    public String key() {
        return nameEntry;         // returns the key
    }


    /**
     * Return list of file offsets.
     
    public ArrayList<Long> locations() {
        return locations;           // returns the list of offsets
    }*/

 
    /**
     * Append a file offset to the existing list.
     */
    public boolean addLocation(Long offset) {   // adding the offset(s) to the list
        if(offset!=null) {
            locations.add(offset);
            return true;
        }
        return false;
    }
    //equals function
    // return true if same name entries and the same key, ignores offsets
    
    public boolean equals(Object o) {
        
      
          if (o == this) {
              return true;
          }
          if ((o == null) || (o.getClass() != this.getClass())) {
              return false;
          }
          NameEntry c = (NameEntry) o;
          
          if(c.nameEntry.equals(nameEntry)) {       // making sure that the keys equal to be true
              return true;
          }
          
          return false;
           
      }
    public String toString() {      // i was given this 
        return ( "[" + this.nameEntry  + ", " + this.locations.toString() + "]" );
        }


    /** Fowler/Noll/Vo hash function is mandatory for this assignment. **/
    public int Hash() {
        final int fnvPrime = 0x01000193; // Constant values for FNV 
        final int fnvBasis = 0x811c9dc5; // hash
                                         // algorithm
        int hashValue = fnvBasis;
        for (int i = 0; i < nameEntry.length(); i++) {
            hashValue ^= nameEntry.charAt(i);
            hashValue *= fnvPrime;
        }
        return Math.abs(hashValue);
    }
}
