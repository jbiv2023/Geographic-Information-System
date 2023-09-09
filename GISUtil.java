
public class GISUtil {
    
    /*
     * Cool trick you can do with formatting specific value
     */
    public static String removeLast(String s) {
        return s.substring(0, s.length() - 1);
    }

    public static final String COMMAND_SEP = "--------------------------------------------------------------------------------\n";

    // Template strings
    public static final String COMMAND_NUMBER = "Command %d: %s %s";
        // World command
    public static final String WORLD_TITLE = "world   %s %s %s %s";
    public static final String WORLD_INFO = "GIS Program\n" + "\n"
        + "dbFile:     %s\n" + "script:     %s\n"
        + "log:        %s\n"
        + "Start time: %s\n"
        + "Quadtree children are printed in the order SW  SE  NE  NW\n"
        + "--------------------------------------------------------------------------------\n"
        + "\n"
        + "Latitude/longitude values in index entries are shown as signed integers, in total seconds.\n"
        + "\n" + "World boundaries are set to:\n" + "              %s\n"
        + "   %s                %s\n" + "              %s\n";
   
}
