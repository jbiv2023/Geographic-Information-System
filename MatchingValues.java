import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class MatchingValues {

    private String value1;
    private String value2;
    File file;
    private long offset;
 
    MatchingValues(String value1, String value2){
        this.value1 = value1;
        this.value2 = value2;
    }

    MatchingValues(String value1, String value2, File file){
        this.value1 = value1;
        this.value2 = value2;
        this.file = file;
    }
    
    /*
     * More getters and setters
     */
  

    public String getValue1() {
        return value1;
    }


    public long getOffset() {
        return offset;
    }


    public void setOffset(long offset) {
        this.offset = offset;
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
    
    /*
     * Parsing through the file and obtaining the offset 
     * based on the coordinates or the name
     */
    
    public String getValues(String one, String two) throws IOException {
        
        String GeoCorLat = one;
        String GeoCorLong = two;
        
        String name = one;
        String state = two;
       
        Scanner scannerr = new Scanner(file);
        RandomAccessFile asd = new RandomAccessFile(file, "r");

         while (scannerr.hasNextLine()) {
            
          String whatIsAtLine = asd.readLine();
      
            if(whatIsAtLine == null) {
                break;
            }

           String[] whatIsAtArray = whatIsAtLine.split("\\|");

             
             if(GeoCorLat.equals(whatIsAtArray[7]) && GeoCorLong.equals(whatIsAtArray[8])) {

                offset = asd.getFilePointer();
                
                return whatIsAtLine;

             }
             if(name.equals(whatIsAtArray[1]) && state.equals(whatIsAtArray[3])) {
                 
                 offset = asd.getFilePointer();
                 return whatIsAtLine;
                 
             }
             
         }
        return "Something went wrong";
        
        
        
        //return "nothing was found";
        
    }

}
