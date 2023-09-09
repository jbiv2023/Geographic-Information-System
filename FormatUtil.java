
public class FormatUtil {
    

    /*
     * Formatting the first and second arguments the long way
     */
    
    public static String[] split(String first, String second) {
        
        String[] array = new String[2];
        String split1 = "";
        String split2 = "";
        if(second.charAt(0) == '0') {
            second = second.substring(1, second.length());
           
            
        }
        
        if(first.charAt(first.length()-1) == 'N') {
           
            split1 = " ("+first.substring(0, 2) + "d " + first.substring(2, 4) + "m " + first.substring(4, first.length()-1)+ "s " + "North";
        }
        if(first.charAt(first.length()-1) == 'S') {
            
            split1 = " ("+first.substring(0, 2) + "d " + first.substring(2, 4) + "m " + first.substring(4, first.length()-1)+ "s " + "South";
        }
        if(second.charAt(second.length()-1) == 'W') {
            
            split2 = " "+second.substring(0, 2) + "d " + second.substring(2, 4) + "m " + second.substring(4, second.length()-1)+ "s " + "West";
        }
        if(second.charAt(second.length()-1) == 'E') {
            
            split2 = " "+second.substring(0, 2) + "d " + second.substring(2, 4) + "m " + second.substring(4, second.length()-1)+ "s " + "East";
        }
        
        
        array[0] = split1;
        array[1] = split2;
        
        
        return  array;
    }

}
