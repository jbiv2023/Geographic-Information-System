public class ConvertUtil {
    
    /*
     * Converting our directions given to seconds 
     * This helps because the half values are given 
     * in seconds
     */

    public static long convertDirectionToSeconds(String direction) {
        /*
         *  Negate if W or S
         */
        int negate = 1;
        char dir = direction.charAt(direction.length() - 1);
        if ( dir == 'W' || dir == 'S') {
            negate = -1;
        }
        
        direction = GISUtil.removeLast(direction);

        long degree = 0;
        long minutes = 0;
        long seconds = 0;

        long degreeToSeconds = 0;
        long minutesToSeconds = 0;

        if (direction.length() == 6) {

            degree = Long.parseLong(direction.substring(0, 2));
            minutes = Long.parseLong(direction.substring(2, 4));
            seconds = Long.parseLong(direction.substring(4));

        }

        else {
            degree = Long.parseLong(direction.substring(0, 3));
            minutes = Long.parseLong(direction.substring(3, 5));
            seconds = Long.parseLong(direction.substring(5));
        }

        // 60 minutes = 1 degree
        // 60 seconds = 1 minute

        degreeToSeconds = degree * 60 * 60;
        minutesToSeconds = minutes * 60;
        
        return negate * (degreeToSeconds + minutesToSeconds + seconds);
    }

}
