import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TimeZone;

public class GIS {

    public static <T> void main(String[] args) throws IOException {
        
        /*
         * Here we get the files from args
         * Declare and initialize some variables and objects
         */
        System.out.println("testing");

        File dataBaseFile = new File(args[0]);
        File scriptFile = new File(args[1]);
        String importFilepath = "";

        Scanner scriptScanner = new Scanner(scriptFile);
        String[] scriptLineArray;
      
        BufferedWriter logWriter = new BufferedWriter(new FileWriter(args[2]));
        

        int commandCounter = 0;

        ZonedDateTime zoneNow = ZonedDateTime.now(TimeZone.getTimeZone("EST")
            .toZoneId());
        String dateTime = zoneNow.toString();

        long westSeconds = 0;
        long eastSeconds = 0;
        long southSeconds = 0;
        long northSeconds = 0;

        hashTable<NameEntry> nameEntryTable = new hashTable<NameEntry>(null,
            null);
        CoordinateIndex coordinateIndex = null;
        bufferPool pool = new bufferPool();
        ;
        
        /*
         * We start by entering the first we find specific commands 
         */

        while (scriptScanner.hasNextLine()) {

            String currentLine = scriptScanner.nextLine();
            scriptLineArray = currentLine.split("\t");

            if (currentLine.length() == 0 || currentLine.charAt(0) == ';') {
                logWriter.write(currentLine);
                logWriter.newLine();
                continue;
            }

            else {

                String command = scriptLineArray[0];
                
                /*
                 * The world section is where we get our bounds to dictate the size of the world
                 */

                if (command.equals("world")) {

                    String west = scriptLineArray[1];
                    String east = scriptLineArray[2];
                    String south = scriptLineArray[3];
                    String north = scriptLineArray[4];

                    westSeconds = ConvertUtil.convertDirectionToSeconds(west);
                    eastSeconds = ConvertUtil.convertDirectionToSeconds(east);
                    southSeconds = ConvertUtil.convertDirectionToSeconds(south);
                    northSeconds = ConvertUtil.convertDirectionToSeconds(north);

                    coordinateIndex = new CoordinateIndex(westSeconds,
                        eastSeconds, southSeconds, northSeconds);

                    logWriter.write(String.format(GISUtil.WORLD_TITLE, west,
                        east, south, north));
                    logWriter.newLine();
                    logWriter.newLine();
                    logWriter.write(String.format(GISUtil.WORLD_INFO, args[0],
                        args[1], args[2], dateTime, northSeconds, westSeconds,
                        eastSeconds, southSeconds));
                    logWriter.write(GISUtil.COMMAND_SEP);
                    logWriter.newLine();

                }
                
                /*
                 * Once we find the import statement then go into another file to collect the data 
                 * manipulates the collected data turning it into a point and checking if said point is in bounds
                 */

                else if (command.equals("import")) {
                    coordinateIndex = new CoordinateIndex(westSeconds,
                        eastSeconds, southSeconds, northSeconds);
                    RandomAccessFile dataBaseRAF = new RandomAccessFile(dataBaseFile, "r");
                    BufferedWriter dataBaseWriter = new BufferedWriter(new FileWriter(
                        args[0]));
                    commandCounter += 1;
                    importFilepath = scriptLineArray[1];
                    logWriter.write(String.format(GISUtil.COMMAND_NUMBER,
                        commandCounter, command, importFilepath));
                    logWriter.newLine();

                    File importFile = new File(importFilepath);
                    RandomAccessFile importRandom = new RandomAccessFile(
                        importFile, "r");
                    importRandom.readLine();

                    long offset = importRandom.getFilePointer();
                    String curLine;
            
                    while ((curLine = importRandom.readLine()) != null) {
                        String[] tokens = curLine.split("\\|");

                        String longitude = tokens[8];
                        String latitude = tokens[7];

                        long logInSeconds = ConvertUtil
                            .convertDirectionToSeconds(longitude);
                        long latInSeconds = ConvertUtil
                            .convertDirectionToSeconds(latitude);

                        Point obj = new Point(logInSeconds, latInSeconds);

                        // in box?
                        if (obj.inBox(westSeconds, eastSeconds, southSeconds,
                            northSeconds)) {
                            //System.out.println("curLline: " + curLine);
                            dataBaseWriter.write(curLine);
                            dataBaseWriter.newLine();
                            NameEntry nameEntry = new NameEntry(tokens[1] + ", "
                                + tokens[3], offset);

                            if (nameEntryTable.insert(nameEntry) != false) {
                                nameEntryTable.insert(nameEntry);

                            }

                            coordinateIndex.addToTree(logInSeconds,
                                latInSeconds, offset);

                        }

                        else {

                            dataBaseWriter.write("OOPS YOUR OUT OF BOUNDS \n");
                        }
                        offset = importRandom.getFilePointer();

                    }

                    dataBaseWriter.close();
                    logWriter.write(GISUtil.COMMAND_SEP);
                    importRandom.close();
                }
                
                /*
                 * Displays the data collected in a particular format based on what 
                 * the second parameter is on this line
                 */

                else if (command.equals("show")) {
                    commandCounter += 1;
                    String type = scriptLineArray[1];
                    logWriter.write(String.format(GISUtil.COMMAND_NUMBER,
                        commandCounter, command, type));
                    logWriter.newLine();
                    if (type.equals("hash")) {
                        nameEntryTable.display(logWriter);
                    }
                    else if (type.equals("pool")) {
                        pool.poolDisplay(logWriter);

                    }
                    else {
                        ArrayList<Point> NorthWest = coordinateIndex
                            .getNorthWestObjs(westSeconds, eastSeconds,
                                southSeconds, northSeconds);
                        for (Point o : NorthWest) {
                            logWriter.write(o.toString() + "\n");

                        }
                    }
                    logWriter.write(GISUtil.COMMAND_SEP);
                }
                /*
                 * Collects data based on what the command is
                 * Verifies if said data is in the pool or not 
                 * If the data is not in the pool then go to memory and get it
                 * other wise collect it from the pool
                 */

                else if (command.equals("what_is_at") || command.equals(
                    "what_is")) { // -----------------------------------------

                    String[] formatTheCord = null;
                    FormatUtil getTheSplit = null;

                    commandCounter += 1;
                    String key = scriptLineArray[1] + scriptLineArray[2];
                    logWriter.write(String.format(GISUtil.COMMAND_NUMBER,
                        commandCounter, command, key));

                    if (pool.containsEquivalentKey(key)) {
                        key = pool.getEquivalentKey(key);
                    }

                    if (pool.containsKey(key)) { // check if its in the pool
                        String[] foundInPoolArray = pool.getRecord(key).split(
                            "\\|");
                        formatTheCord = FormatUtil.split(foundInPoolArray[7],
                            foundInPoolArray[8]);

                        if (command.equals("what_is")) {
                            logWriter.write("what is: " + scriptLineArray[1]
                                + " " + scriptLineArray[2] + "\n" + "\n     "
                                + pool.getOffset(key) + ": "
                                + foundInPoolArray[1] + " "
                                + foundInPoolArray[5] + " "
                                + foundInPoolArray[3]);
                            logWriter.write(formatTheCord[0] + ", "
                                + formatTheCord[1] + "\n");

                            logWriter.write(
                                "--------------------------------------------------------------------------------");
                            logWriter.write("\n");
                        }

                        else {
                            formatTheCord = FormatUtil.split(
                                foundInPoolArray[7], foundInPoolArray[8]);
                            logWriter.write("what is at: " + scriptLineArray[1]
                                + " " + scriptLineArray[2] + "\n" + "\n     "
                                + pool.getOffset(key) + ": "
                                + foundInPoolArray[1] + " "
                                + foundInPoolArray[5] + " "
                                + foundInPoolArray[3] + "\n");
                            logWriter.write(
                                "     The above features were found at: "
                                    + formatTheCord[0] + ", " + formatTheCord[1]
                                    + "\n");
                            logWriter.write(
                                "--------------------------------------------------------------------------------");
                            logWriter.write("\n");
                        }
                      
                    }
                    else { 
                        MatchingValues thisObject = new MatchingValues(
                            scriptLineArray[1], scriptLineArray[2],
                            dataBaseFile);
                        String value = thisObject.getValues(scriptLineArray[1],
                            scriptLineArray[2]);
                        String[] valueArray = value.split("\\|");
 
                        formatTheCord = FormatUtil.split(valueArray[7],
                            valueArray[8]);

                        if (command.equals("what_is")) {

                            logWriter.write("what is: " + scriptLineArray[1]
                                + " " + scriptLineArray[2] + "\n" + "\n     "
                                + thisObject.getOffset() + ": " + valueArray[1]
                                + " " + valueArray[5] + " " + valueArray[3]);
                            logWriter.write(formatTheCord[0] + ", "
                                + formatTheCord[1] + "\n");
                            logWriter.write(
                                "--------------------------------------------------------------------------------");
                            logWriter.write("\n");
                        }
                        else {
                            logWriter.write("what is at: " + scriptLineArray[1]
                                + " " + scriptLineArray[2] + "\n" + "\n     "
                                + thisObject.getOffset() + ": " + valueArray[1]
                                + " " + valueArray[5] + " " + valueArray[3]
                                + "\n");
                            logWriter.write(
                                "     The above features were found at: "
                                    + formatTheCord[0] + ", " + formatTheCord[1]
                                    + "\n");
                            logWriter.write(
                                "--------------------------------------------------------------------------------");
                            logWriter.write("\n");
                        }

                        pool.addRecord(key, value);
                        pool.addOffset(key, thisObject.getOffset()); 
                        
                        
                        String longLatKey = valueArray[7] + valueArray[8];
                        String cityStateKey = valueArray[1] + valueArray[3];

                        String equivalentKey;

                        if (key.equals(longLatKey)) {
                            equivalentKey = cityStateKey;
                        }

                        else {
                            equivalentKey = longLatKey;
                        }

                        pool.addEquivalentKey(equivalentKey, key);
                    }
                    logWriter.write(GISUtil.COMMAND_SEP);

                }
                
                /*
                 * Does the same thing as above but checks what's inside a given rectangle 
                 * based on the values given
                 */

                else if (command.equals("what_is_in")) {
                    commandCounter += 1;
                    String arg = scriptLineArray[1] + " " + scriptLineArray[2]
                        + " " + scriptLineArray[3] + " " + scriptLineArray[4];
                    logWriter.write(String.format(GISUtil.COMMAND_NUMBER,
                        commandCounter, command, arg));
                    logWriter.newLine();
                    logWriter.newLine();

                    long givenLatitude = ConvertUtil.convertDirectionToSeconds(
                        scriptLineArray[1]);
                    long givenLongitude = ConvertUtil.convertDirectionToSeconds(
                        scriptLineArray[2]);

                    long halfHeight = Long.parseLong(scriptLineArray[3]);
                    long halfWidth = Long.parseLong(scriptLineArray[4]);

                    
                    /*
                     *  getting the direction unit in terms of seconds plus or minus the
                     *   half value... depending on the direction
                     */

                    long newEast = givenLongitude + halfWidth;
                    long newNorth = givenLatitude + halfHeight;
                    long newWest = givenLongitude - halfWidth;
                    long newSouth = givenLatitude - halfHeight;

                 

                    ArrayList<Point> bbb = coordinateIndex.findInTree(newWest,
                        newEast, newSouth, newNorth);

                    RandomAccessFile importRandom = new RandomAccessFile(
                        importFilepath, "r");

                    for (Point z : bbb) {
                        String key = String.valueOf(z.getOffset());
                        String record;
                        if (pool.containsKey(key)) {
                            System.out.println("found in pool");
                            record = pool.getRecord(key);
                        }
                        else {
                            importRandom.seek(z.getOffset());
                            String line = importRandom.readLine();
                            String[] lineArray = line.split("\\|");
                        
                            String[] formatArray = FormatUtil.split(
                                lineArray[7], lineArray[8]);
                            record = z.offset + ": " + lineArray[1] + " "
                                + lineArray[3] + " " + formatArray[0] + ", "
                                + formatArray[1] + ") \n";
                            pool.addRecord(key, record);
                            pool.addOffset(key, z.getOffset()); // add the
                            pool.addEquivalentKey(key, key);
                            logWriter.write(record);
                        
                        }
                    }

                    logWriter.write("\n");
                    logWriter.write(GISUtil.COMMAND_SEP);
                    importRandom.close();

                }

                else if (command.equals("quit")) {
                    System.out.println(command);
                    logWriter.close();
                    return;
                }
                else {
                    continue;
                }
            }
        }
        logWriter.close();
    }
}

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
//    Joey Bush
//    joeybushiv

