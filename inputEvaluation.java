import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Hayden Knox 15/06/2022
 *         This Java class is used to check the input provided by the text file.
 *         After being examined by the regex staments in the Etude7V4 java
 *         class,
 *         which provides a specification for the number of inputvalues for both
 *         longitude and latitude cordinates and location name data. The
 *         coordinate
 *         data in the input string is processed to isolate acceptable and
 *         unacceptable
 *         formats of input data. The acceptable coordinates are then used in
 *         the
 *         GEOJSON mapping application to display GEOJSON map point. Depending
 *         on
 *         which of the three formats of GPS coordinate specifications are
 *         entered,
 *         The input string is MODIFIED to produce a correct format for
 *         processing.
 *         As GEOJSON accepts only the format of Standard input.
 * 
 *         example: 89° N , 130° E
 */
public class inputEvaluation {
    // This array datafield is used in the programm between different uses of
    // conversion from arrays to ArrayLists.
    // Having these values hardcoded and accessible as an array datafiled makes
    // identifying
    // them in different string inputs easier to comprehend.
    static String[] compass = { "N", "S", "W", "E" };
    // This ArrayList hold all of the processed lines of user input as the correct
    // form of
    // output in standard form before writing the file to a geojson file.
    // This is after the process of elimination removing all the relevant indicator
    // symbols and symbols for lat and long.
    static ArrayList<String> JSONMapPoints = new ArrayList<String>();
    static Double latConstant = 90.000000;
    static Double longConstant = 180.000000;
    // This datafield is used to hold an unaltered line of input from the textfile
    // and print it out to the user. This string will print if this class and
    // methods
    // find any information which cannot be processed into the correct format.
    static String inputLineError;
    static boolean negativeFlaglat = false;
    static boolean negativeFlaglong = false;


    public static void shareLine(String inputLine) {
        inputLine = inputLineError;
    }

    /**
     * @method: conversion(ArrayList<String> input).
     *          The conversion method is called when the scanner reads input values
     *          using the regex
     *          stament for digits. If the number of digits entered exceeds 2, which
     *          occours for
     *          standard input. The values held in the arrayLists for
     *          latitudeConversion and
     *          LongitudeConversion are movdified to produce only one value each
     *          representing
     *          latitude and Longitude.
     * 
     *          2 vlaues: Standard Form:
     *          - latitudeConversion holds: 1 YES
     *          - longitudeConversion holds: 1 YES
     * 
     *          4 vlaues: Standard Form: DDM:
     *          - latitudeConversion holds: 2 NO YES
     *          - longitudeConversion holds: 2 NO YES
     * 
     *          6 vlaues: Standard Form: DMS:
     *          - latitudeConversion holds: 2 NO YES
     *          - longitudeConversion holds: 2 NO YES
     * 
     *          DDM (Decimal Degrees Minutes), or DMS(Degrees Minutes Seconds). This
     *          is dictated by the size of the input
     *          (The arrayList) Which the method recieves from the other method of
     *          either inputCheckDDM or inputCheckDMS
     * 
     *          The method returns to values of an array lists from prior methods
     *          called in the form of standard input. Before writing to a GEOJSON
     *          file.
     * 
     * @param: input This is the filtered arrayList which all irrelevant symbols for
     *               methematical calculations have been removed.
     * 
     * @return An arraylist of converted degrees values which have been transformed
     *         from either the format
     *         DDM or DMS.
     */
    public static Double conversion(ArrayList<String> input, boolean isLat) {
        ArrayList<Double> inputConvert = new ArrayList<Double>();
        //System.out.println(inputConvert);
        for (int i = 0; i < input.size(); i++) {
            inputConvert.add(Double.parseDouble(input.get(i)));
        }
        //System.out.println(inputConvert);
        if (inputConvert.size() == 2) {
            Double[] formatArr = new Double[inputConvert.size()];
            inputConvert.toArray(formatArr);
            Double degrees = formatArr[0];
            Double minutes = formatArr[1];
            //System.out.println("negativeFlaglat :"+negativeFlaglat);
            //System.out.println("input :"+ input);
            //System.out.println("isLat "+ isLat);
            if(negativeFlaglong == true && !isLat){
                negativeFlaglong = false;
                return (-1 * (degrees + (minutes / 60)));
            }
            
            if(negativeFlaglat == true && isLat){
                //System.out.println(negativeFlaglat+" :"+inputConvert + "result");
                negativeFlaglat = false;
                return (-1 * (degrees + (minutes / 60)));
            }
            return (degrees + (minutes / 60));
        } else if (inputConvert.size() == 3) {
            Double[] formatArr = new Double[inputConvert.size()];
            inputConvert.toArray(formatArr);
            Double degrees = formatArr[0];
            Double minutes = formatArr[1];
            Double seconds = formatArr[2];
            //System.out.println("negativeFlaglat :"+negativeFlaglat);
            //System.out.println("input :"+ input);
            //System.out.println("isLat "+ isLat);
            if(negativeFlaglong == true && !isLat){
                negativeFlaglong = false;
                return (-1 * (degrees + (minutes / 60)));
            }
            if(negativeFlaglat == true && isLat){
                negativeFlaglat = false;
                return (-1 * (degrees + (minutes / 60)));
            }
            return (degrees + minutes / 60 + seconds / 3600);
        } else {
            System.out.println("Unable to process: " + inputLineError + " cant convert");
        }
        return null;
    }

    /**
     * @method standardForm(String locationString, int compassMatchCounter)
     *         This method is responsible for converting all the possible cases of
     *         standard
     *         form input into the correct format to be processed into a GEOJSON
     *         Map.
     * 
     * @param: (locationString)      This is the line of input which has a regex
     *                               digit match count
     *                               of 2 from the previous class. Both digits have
     *                               been put into a string to be allocated
     *                               correctly
     *                               for standard form input processing.
     * 
     * @param: (compassMatchCounter) This parameter is the number of compass symbols
     *                               which have appearded
     *                               in the line of input read by the scanner class.
     *                               This parameter is important for determining if
     *                               and
     *                               compass symbols are missing, what order the
     *                               values for latitude and longitude in degrees
     *                               should be
     *                               organised.
     * 
     * @return: This method returns to the Etude7V4 class a sanitised array of type
     *          string to be processed.
     *          This sanitised array is then written into a GEOJSON map pointer.
     */
    public static String[] standardForm(String locationString, int compassMatchCounter) {
        // This variabl splits the input string into an array on the whitespaces added
        // post non digit character
        // removal and adding whitespce for future spliting method calls and input
        // allocation.
        String[] splitInput = locationString.split("[ ]+");
        Double[] methodInput = new Double[2];

        // A compasssMatchCounter value of 1 is a unique case of standard input where
        // the order of
        // elements is examined individually using a for loop to determine which values
        // pertain to
        // latitude and lonitude. Organising these values into array lists allowed for
        // the examinations of
        // different cases of value arrangements which could occour causing math errors.
        if (compassMatchCounter == 1) {
            for (int i = 0; i < splitInput.length; i++) {
                if (splitInput[i].contains(compass[0]) || splitInput[i].contains(compass[1])) {
                    if (splitInput[i].equals("N") || splitInput[i].equals("S")) {
                        // latitude changed into a double from the previous itterator
                        // example: 12.55 N 67.65 12.55 S 34.5
                        if(splitInput[i].equals("S")){
                            negativeFlaglat = true;
                        }
                        methodInput[0] = Double.parseDouble(splitInput[i - 1]);
                        try {
                            // longitude first attempt if latitude is the first value
                            methodInput[1] = Double.parseDouble(splitInput[i + 1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // longitude second attempt if latitude is not the first value
                            // example: 34.5 12.55 N 34.5 12.55 S
                            methodInput[1] = Double.parseDouble(splitInput[i - 2]);
                        }
                    }
                }
                // NORTH N SOUTH S
                // This above if statement concerns the input for latitude values in the
                // standard form.
                // If the compass value which follows an input numeric value is
                // N or S for North or South, this represents latitude entries for standard
                // input.

                // EAST E WEST W
                // this value is assigned to the array which will be converted into the correct
                // output if it exceeds the limit values for longitude it will be converted.
                else if (splitInput[i].contains(compass[2]) || splitInput[i].contains(compass[3])) {
                    if (splitInput[i].equals("E") || splitInput[i].equals("W")) {
                        // longitude chnaged into a double from the previous itterator
                        // example: 12.55 E 67.65 12.55 34.5 W
                        if(splitInput[i].equals("W")){
                            negativeFlaglong = true;
                        }
                        methodInput[1] = Double.parseDouble(splitInput[i - 1]);
                        try {
                            // latitude first attempt if longitude is the first value
                            methodInput[0] = Double.parseDouble(splitInput[i + 1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // latitude second attempt if longitude is not the first value
                            // 34.5 12.55 E 34.5 12.55 W
                            methodInput[0] = Double.parseDouble(splitInput[i - 2]);
                        }
                    }
                }
            }
        }
        // This if statement concerns the input for latitude values in the standard
        // form.
        // IWith a compassMatcherCounter value of 2. If the compass value which follows
        // an input numeric value is or S for Sorth or South This represents latitude
        // entries

        // NORTH N SOUTH S
        // The latitude value is assigned to the array which will be converted into the
        // correct
        // output if it exceeds the limit values for latitude it will be converted.
        if (compassMatchCounter == 2) {
            for (int i = 0; i < splitInput.length; i++) {
                if (splitInput[i].contains(compass[0]) || splitInput[i].contains(compass[1])) {
                    if (splitInput[i].equals("N") || splitInput[i].equals("S")) {
                        // latitude changed into a double from the previous itterator
                        // example: 12.55 N or 67.65 S
                        if(splitInput[i].equals("S")){
                            negativeFlaglat = true;
                        }
                        methodInput[0] = Double.parseDouble(splitInput[i - 1]);
                    }
                }

                // This if statement concerns the input for longitude values in the standard
                // form.
                // If the compass value which follows an input numeric value is
                // E or W for East or West this represents longitude data entries

                // EAST E WEST W
                // The longitude value is assigned to the array which will be converted into the
                // correct
                // output if it exceeds the limit values for longitude it will be converted.
                if (splitInput[i].contains(compass[2]) || splitInput[i].contains(compass[3])) {
                    if (splitInput[i].equals("E") || splitInput[i].equals("W")) {
                        // longitude chnaged into a double from the previous itterator
                        // example: 12.55 E or 67.65 W
                        if(splitInput[i].equals("W")){
                            negativeFlaglong = true;
                        }
                        methodInput[1] = Double.parseDouble(splitInput[i - 1]);
                    }
                }
            }

            // NOTE: NOTE the above 2 cases;
            // if the Latitude and Longitude values are reversed on input entry this should
            // be able to
            // indicate which element in the arraylist
            // is latitude and longitude relying on the compass values present in the
            // arraylist.
            // NOTE: These last cases may not matter as if there is no compass input values
            // for NSEW, it is assumed that
            // the inputs for latitude and longitude are in the correct order because there
            // is no signififcant indicator.
        }
        if (splitInput.length == 2) {
            methodInput[0] = Double.parseDouble(splitInput[0]);
            methodInput[1] = Double.parseDouble(splitInput[1]);

        }
        if(negativeFlaglat == true){
            methodInput[0] = methodInput[0] * -1.0;
        }
        if(negativeFlaglong == true){
            methodInput[1] = methodInput[1] * -1.0;
        }
        String[] returnArray = new String[methodInput.length];
        for (int i = 0; i < methodInput.length; i++) {
            returnArray[i] = methodInput[i].toString();
        }

        return returnArray;

    }

    // DDM
    /**
     * @method inputCheck2(String locationString, int compassMatchCounter)
     *         This method is responsible for assigning all longitude and latitude
     *         values entered by the user
     *         to the array which will convert them to Standard Form. Whatever
     *         possible cases of DDM input may
     *         occour due to error from user input. The correct format to be
     *         processed into a GEOJSON Map will
     *         be returned. This call of this method is dictated by the value help
     *         by numericCounter value used
     *         from the rpevious class. DDM (Decimal Degrees Minutes) which is also
     *         dictated by the size of the
     *         input of (The arrayList).
     * 
     * 
     * @param: (locationString)      This is the line of input which has a regex
     *                               digit match count
     *                               of 4 from the previous class. All digits have
     *                               been put into a string to be allocated
     *                               correctly
     *                               for standard form input processing.
     * 
     * @param: (compassMatchCounter) This parameter is the number of compass symbols
     *                               which have appearded
     *                               in the line of input read by the scanner class.
     *                               This parameter is important for determining if
     *                               and
     *                               compass symbols are missing, what order the
     *                               values for latitude and longitude in degrees
     *                               should be
     *                               organised.
     */
    public static ArrayList<String> DDMCheck(String locationString, int compassMatchCounter) {
        String[] splitInput = locationString.split("[ ]+");

        Double[] methodInput;
        // these two arrayList hold the increased number of double values needed to
        // convert DDM into
        // standard input format.
        ArrayList<String> latitudeConversion = new ArrayList<String>();
        ArrayList<String> longitudeConversion = new ArrayList<String>();

        if (compassMatchCounter == 1) {
            methodInput = new Double[4];
            for (int i = 0; i < splitInput.length; i++) {
                if (splitInput[i].contains(compass[0]) || splitInput[i].contains(compass[1])) {
                    if (splitInput[i].equals("N") || splitInput[i].equals("S")) {
                        // latitude changed into a double from the previous itterator
                        // example: 12.55d 34m N 67.65d 43m        12.55d 34m 67.65d 43m N
                        if(splitInput[i].equals("S")){
                            negativeFlaglat = true;
                        }
                        methodInput[0] = Double.parseDouble(splitInput[i - 2]); // degrees
                        methodInput[1] = Double.parseDouble(splitInput[i - 1]); // minutes
                        latitudeConversion.add(methodInput[0].toString());
                        latitudeConversion.add(methodInput[1].toString());
                        //System.out.println(latitudeConversion.toString());
                        try {
                            // longitude first attempt if latitude is the first value
                            methodInput[2] = Double.parseDouble(splitInput[i + 1]); // degrees
                            methodInput[3] = Double.parseDouble(splitInput[i + 2]); // minutes
                            longitudeConversion.add(methodInput[2].toString());
                            longitudeConversion.add(methodInput[3].toString());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // longitude second attempt if latitude is the second value
                            methodInput[2] = Double.parseDouble(splitInput[i - 4]); // degrees
                            methodInput[3] = Double.parseDouble(splitInput[i - 3]); // minutes
                            longitudeConversion.add(methodInput[2].toString());
                            longitudeConversion.add(methodInput[3].toString());
                            //System.out.println(longitudeConversion.toString());

                        }
                    }
                }

                else if (splitInput[i].contains(compass[2]) || splitInput[i].contains(compass[3])) {
                    if (splitInput[i].equals("E") || splitInput[i].equals("W")) {
                        // longitude chnaged into a double from the previous itterator
                        // example: 12.55d 34m W 67.65d 43m 12.55d 34m 67.65d 43m W
                        if(splitInput[i].equals("W")){
                            negativeFlaglong = true;
                        }
                        methodInput[2] = Double.parseDouble(splitInput[i - 2]); // degrees
                        methodInput[3] = Double.parseDouble(splitInput[i - 1]); // minutes
                        longitudeConversion.add(methodInput[2].toString());
                        longitudeConversion.add(methodInput[3].toString());
                        //System.out.println(longitudeConversion.toString());
                        try {
                            // latitude first attempt if longitude is the first value
                            methodInput[0] = Double.parseDouble(splitInput[i + 1]); // degrees
                            methodInput[1] = Double.parseDouble(splitInput[i + 2]); // minutes
                            latitudeConversion.add(methodInput[0].toString());
                            latitudeConversion.add(methodInput[1].toString());
                            //System.out.println(latitudeConversion.toString());

                        } catch (ArrayIndexOutOfBoundsException e) {
                            // latitude second attempt if longitude is the second value
                            methodInput[0] = Double.parseDouble(splitInput[i - 4]); // degrees
                            methodInput[1] = Double.parseDouble(splitInput[i - 3]); // minutes
                            latitudeConversion.add(methodInput[0].toString());
                            latitudeConversion.add(methodInput[1].toString());
                            //System.out.println(latitudeConversion.toString());

                        }
                    }
                }
            }
            //System.out.println("check ends here");
            Double latitude = conversion(latitudeConversion, true);
            Double longitude = conversion(longitudeConversion, false);

            ArrayList<String> resultsArr = new ArrayList<String>();
            resultsArr.add(String.valueOf(latitude));
            resultsArr.add(String.valueOf(longitude));
            return resultsArr;
        }

        else if (compassMatchCounter == 2) {
            methodInput = new Double[4];
            for (int i = 0; i < splitInput.length; i++) {
                if (splitInput[i].contains(compass[0]) || splitInput[i].contains(compass[1])) {
                    if (splitInput[i].equals("N") || splitInput[i].equals("S")) {
                        // latitude changed into a double from the previous itterator
                        // example: 12.55d 34m N 67.65d 34m E 12.55d 34m E 67.65d 34m N
                        if(splitInput[i].equals("S")){
                            negativeFlaglat = true;
                        }
                        methodInput[0] = Double.parseDouble(splitInput[i - 2]); // degrees
                        methodInput[1] = Double.parseDouble(splitInput[i - 1]); // minutes
                        latitudeConversion.add(methodInput[0].toString());
                        latitudeConversion.add(methodInput[1].toString());
                        //System.out.println(latitudeConversion.toString());
                    }
                }

                if (splitInput[i].contains(compass[2]) || splitInput[i].contains(compass[3])) {
                    if (splitInput[i].equals("E") || splitInput[i].equals("W")) {
                        // longitude chnaged into a double from the previous itterator
                        // example: 12.55 N 67.65 E 12.55d 34m E 67.65d 34m N
                        if(splitInput[i].equals("W")){
                            negativeFlaglong = true;
                        }
                        methodInput[2] = Double.parseDouble(splitInput[i - 2]); // degrees
                        methodInput[3] = Double.parseDouble(splitInput[i - 1]); // minutes
                        longitudeConversion.add(methodInput[2].toString());
                        longitudeConversion.add(methodInput[3].toString());
                        //System.out.println(longitudeConversion.toString());


                    }
                }
            }
            Double latitude = conversion(latitudeConversion, true);
            Double longitude = conversion(longitudeConversion, false);

            ArrayList<String> resultsArr = new ArrayList<String>();
            resultsArr.add(String.valueOf(latitude));
            resultsArr.add(String.valueOf(longitude));
            return resultsArr;

        } else if (compassMatchCounter == 0) {
            int size = splitInput.length;
            if (latitudeConversion.size() == 0) {
                for (int i = 0; i < size / 2; i++) {
                    latitudeConversion.add(splitInput[i]);
                }
            }

            Double latitude = conversion(latitudeConversion, true);
            if (longitudeConversion.size() == 0) {
                for (int i = size / 2; i < size; i++) {
                    longitudeConversion.add(splitInput[i]);
                }
            }
            Double longitude = conversion(longitudeConversion, false);

            ArrayList<String> resultsArr = new ArrayList<String>();
            resultsArr.add(String.valueOf(latitude));
            resultsArr.add(String.valueOf(longitude));
            return resultsArr;
        } else {
            System.out.println("Unable to process: " + inputLineError + "wrong method");
        }
        return null;
    }

    // DMS checker
    /**
     * @method inputCheck3(String locationString, int compassMatchCounter)
     *         This method is responsible for converting all the possible cases of
     *         DMS (Degrees d m and Seconds s)
     *         form input into the correct format to be processed into a GEOJSON
     *         Map.
     * 
     * @param: (locationString)      This is the line of input which has a regex
     *                               digit match count
     *                               of 6 from the previous class. All digits have
     *                               been put into a string to be allocated
     *                               correctly
     *                               for standard form input processing.
     * 
     * @param: (compassMatchCounter) This parameter is the number of compass symbols
     *                               which have appearded
     *                               in the line of input read by the scanner class.
     *                               This parameter is important for determining if
     *                               and
     *                               compass symbols are missing, what order the
     *                               values for latitude and longitude in degrees
     *                               should be
     *                               organised.
     * 
     * @return: This method returns to the Etude7V4 class a sanitised array of type
     *          string to be processed.
     *          This sanitised array is then written into a GEOJSON map pointer.
     */
    public static ArrayList<String> DMSCheck(String locationString, int compassMatchCounter) {
        String[] splitInput = locationString.split("[ ]+");
        Double[] methodInput;
        ArrayList<String> latitudeConversion = new ArrayList<String>();
        ArrayList<String> longitudeConversion = new ArrayList<String>();
        if (compassMatchCounter == 1) {
            methodInput = new Double[6];
            for (int i = 0; i < splitInput.length; i++) {
                if (splitInput[i].contains(compass[0]) || splitInput[i].contains(compass[1])) {
                    if (splitInput[i].equals("N") || splitInput[i].equals("S")) {
                        // latitude changed into a double from the previous itterator
                        // example: 12.55d 34m 12s N 67.65d 43m 12s                 12.55d 34m 12s 67.65d 43m 12s N
                        if(splitInput[i].equals("S")){
                            negativeFlaglat = true;
                        }
                        methodInput[0] = Double.parseDouble(splitInput[i - 3]); // degrees
                        methodInput[1] = Double.parseDouble(splitInput[i - 2]); // minutes
                        methodInput[2] = Double.parseDouble(splitInput[i - 1]); // seconds
                        latitudeConversion.add(methodInput[0].toString());
                        latitudeConversion.add(methodInput[1].toString());
                        latitudeConversion.add(methodInput[2].toString());
                        try {
                            // longitude first attempt if latitude is the first value
                            methodInput[3] = Double.parseDouble(splitInput[i + 1]); // degrees
                            methodInput[4] = Double.parseDouble(splitInput[i + 2]); // minutes
                            methodInput[5] = Double.parseDouble(splitInput[i + 3]); // seconds
                            longitudeConversion.add(methodInput[3].toString());
                            longitudeConversion.add(methodInput[4].toString());
                            longitudeConversion.add(methodInput[5].toString());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // longitude second attempt if latitude is the second value
                            methodInput[3] = Double.parseDouble(splitInput[i - 6]); // degrees
                            methodInput[4] = Double.parseDouble(splitInput[i - 5]); // minutes
                            methodInput[5] = Double.parseDouble(splitInput[i - 4]); // minutes
                            longitudeConversion.add(methodInput[3].toString());
                            longitudeConversion.add(methodInput[4].toString());
                            longitudeConversion.add(methodInput[5].toString());
                        }
                    }

                } else if (splitInput[i].contains(compass[2]) || splitInput[i].contains(compass[3])) {
                    if (splitInput[i].equals("E") || splitInput[i].equals("W")) {
                        // longitude chnaged into a double from the previous itterator
                        // example: 12.55d 34m 12s W 67.65d 43m 12s             12.55d 34m 12s 67.65d 43m 12s W
                        if(splitInput[i].equals("W")){
                            negativeFlaglong = true;
                        }
                        methodInput[3] = Double.parseDouble(splitInput[i - 3]); // degrees
                        methodInput[4] = Double.parseDouble(splitInput[i - 2]); // minutes
                        methodInput[5] = Double.parseDouble(splitInput[i - 1]); // seconds
                        longitudeConversion.add(methodInput[3].toString());
                        longitudeConversion.add(methodInput[4].toString());
                        longitudeConversion.add(methodInput[5].toString());
                        try {
                            // latitude first attempt if longitude is the first value
                            methodInput[0] = Double.parseDouble(splitInput[i + 1]); // degrees
                            methodInput[1] = Double.parseDouble(splitInput[i + 2]); // minutes
                            methodInput[2] = Double.parseDouble(splitInput[i + 3]); // seconds

                            latitudeConversion.add(methodInput[0].toString());
                            latitudeConversion.add(methodInput[1].toString());
                            latitudeConversion.add(methodInput[2].toString());

                        } catch (ArrayIndexOutOfBoundsException e) {
                            // latitude second attempt if longitude is the second value
                            methodInput[0] = Double.parseDouble(splitInput[i - 6]); // degrees
                            methodInput[1] = Double.parseDouble(splitInput[i - 5]); // minutes
                            methodInput[2] = Double.parseDouble(splitInput[i - 3]); // minutes

                            latitudeConversion.add(methodInput[0].toString());
                            latitudeConversion.add(methodInput[1].toString());
                            latitudeConversion.add(methodInput[2].toString());

                        }
                    }
                }
            }
            Double latitude = conversion(latitudeConversion, true);
            Double longitude = conversion(longitudeConversion, false);

            ArrayList<String> resultsArr = new ArrayList<String>();
            resultsArr.add(String.valueOf(latitude));
            resultsArr.add(String.valueOf(longitude));
            return resultsArr;
        }

        else if (compassMatchCounter == 2) {
            methodInput = new Double[6];
            for (int i = 0; i < splitInput.length; i++) {
                if (splitInput[i].contains(compass[0]) || splitInput[i].contains(compass[1])) {
                    if (splitInput[i].equals("N") || splitInput[i].equals("S")) {
                        // latitude changed into a double from the previous itterator
                        // example: 12.55d 34m 12s N 67.65d 34m 21s E 12.55d 34m 12s E 67.65d 34m 12s N
                        if(splitInput[i].equals("S")){
                            negativeFlaglat = true;
                        }
                        methodInput[0] = Double.parseDouble(splitInput[i - 3]); // degrees
                        methodInput[1] = Double.parseDouble(splitInput[i - 2]); // minutes
                        methodInput[2] = Double.parseDouble(splitInput[i - 1]); // minutes
                        latitudeConversion.add(methodInput[0].toString());
                        latitudeConversion.add(methodInput[1].toString());
                        latitudeConversion.add(methodInput[2].toString());
                    }
                }

                if (splitInput[i].contains(compass[2]) || splitInput[i].contains(compass[3])) {
                    if (splitInput[i].equals("E") || splitInput[i].equals("W")) {
                        // longitude chnaged into a double from the previous itterator
                        // example: 12.55d 34m 12s N 67.65d 34m 21s E 12.55d 34m E 67.65d 34m N
                        if(splitInput[i].equals("W")){
                            negativeFlaglong = true;
                        }
                        methodInput[3] = Double.parseDouble(splitInput[i - 3]); // degrees
                        methodInput[4] = Double.parseDouble(splitInput[i - 2]); // minutes
                        methodInput[5] = Double.parseDouble(splitInput[i - 1]); // minutes
                        longitudeConversion.add(methodInput[3].toString());
                        longitudeConversion.add(methodInput[4].toString());
                        longitudeConversion.add(methodInput[5].toString());
                    }
                }
            }
            Double latitude = conversion(latitudeConversion, true);
            Double longitude = conversion(longitudeConversion, false);
            ArrayList<String> resultsArr = new ArrayList<String>();
            resultsArr.add(String.valueOf(latitude));
            resultsArr.add(String.valueOf(longitude));
            return resultsArr;

        } else if (compassMatchCounter == 0) {
            int size = splitInput.length;
            if (latitudeConversion.size() == 0) {
                for (int i = 0; i < size / 2; i++) {
                    latitudeConversion.add(splitInput[i]);
                }
            }

            Double latitude = conversion(latitudeConversion, true);
            if (longitudeConversion.size() == 0) {
                for (int i = size / 2; i < size; i++) {
                    longitudeConversion.add(splitInput[i]);
                }
            }
            Double longitude = conversion(longitudeConversion, false);

            ArrayList<String> resultsArr = new ArrayList<String>();
            resultsArr.add(String.valueOf(latitude));
            resultsArr.add(String.valueOf(longitude));
            return resultsArr;
        }
        return null;
    }

    /**
     * @method createMapPoint(ArrayList<Double> pointMethodInput, String
     *         labeloutput)
     *         The purpose of this method is to create properly formatted entries
     *         for pin locations in the GEOJSON
     *         map file. referenceing the aobve mentiond datafile of arraylist. Each
     *         map marker is added to this
     *         arraylist before being printed to the GeoJSOn file as the last step.
     * 
     * @param: labeloutput:       this is the extracted label from the line of input
     *                            read by the scanner class.
     * 
     * @param: markerMethodInput: This parameter is an arraylist containing the
     *                            values converted to degrees metrics
     *                            which will be printed to the geoJSON output file.
     */
    public static void createMapPoint(ArrayList<Double> pointMethodInput, String labeloutput) throws IOException {
        ArrayList<Double> rotated = rotationValues(pointMethodInput);
        Double latitude = rotated.get(0);
        Double longitude = rotated.get(1);
        DecimalFormat newDecimalFormat = new DecimalFormat("0.000000");
        String latitudeOut = newDecimalFormat.format(latitude);
        String longitudeOut = newDecimalFormat.format(longitude);
        try {
                    JSONMapPoints.add(
                            "\n{\n" + "\"type\": \"Feature\"," + "\n" + "\"properties\": {\n"
                                    + "\"marker-color\": \"#FF0000\",\n"
                                    + "\"marker-size\": \"medium\",\n" + "\"marker-symbol\": \"\",\n" + "\"Name\": \""
                                    + labeloutput + "\"\n"
                                    + "\n},\n" + "\"geometry\": {" + "\n" + "\"type\":" + " \"Point\",\n"
                                    + "\"coordinates\""
                                    + ": " + "[" + "\n" + longitudeOut + "," + "\n" + latitudeOut + "\n]" + "\n}"
                                    + "\n}");
                    labeloutput = "";
                    makeMap();
                } catch (IOException ex) {
                    System.out.println("Something wrong with input.");
                    ex.printStackTrace();
                }
    }

    public static void makeMap() throws IOException {
        try {
            String mapPointsOutput = "";
            for (String points : JSONMapPoints) {
                mapPointsOutput += points + ",";
            }
            char comma = ',';
            int remove = mapPointsOutput.lastIndexOf(comma);
            String newOutput = mapPointsOutput.substring(0, remove) + mapPointsOutput.substring(remove + 1);
            String output = "{\n" + "\"type\": \"FeatureCollection\"," + "\n" + "\"features\": [" + newOutput
                    + "\n]\n}";
            FileWriter mapWriter = new FileWriter("MapOutput.geojson");
            mapWriter.write(output);
            mapWriter.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @method rotationValues(ArrayList<Double> pointInput)
     *         The creates corrected values of standard input. Values when passsed
     *         and formatted entries for pin locations in the GEOJSON
     *         map file. As latitude and Longitude both have a maximum value of
     *         90.00000 and 180.000000 respectively.
     *         If the entries of calculated standard input degrees vaues exceed
     *         these thresholds. They are rotated around the
     *         trheshold values to the begining of the initial value starting at
     *         0.000000.
     * 
     * @param: pointInput: this is the entire list of geoJSON points written into
     *                     the arraylist variable geoJSONMapPoints.
     *                     Each point will be formatted correctly before being
     *                     written to a GEOJSON Map file.
     */
    public static ArrayList<Double> rotationValues(ArrayList<Double> pointInput) {
        Double lat = pointInput.get(0);
        Double lon = pointInput.get(1);
        int latrotationBase;
        int latrotationsNum;
        int longrotationBase;
        int longrotationsNum;
        Double latmodulo;
        Double longmodulo;
        latrotationBase = (int) (lat / latConstant);
        latrotationsNum = Math.abs(latrotationBase);
        latmodulo = lat % latConstant;
        lat = latmodulo;
        longrotationBase = (int) (lon / longConstant);
        longrotationsNum = Math.abs(longrotationBase);
        longmodulo = lon % longConstant;
        lon = longmodulo;
        ArrayList<Double> formatOut = new ArrayList<Double>();
        if (lat > latConstant || latConstant > lat) {
            while (0 < latrotationsNum) {
                lat = lat * -1.0;
                latrotationsNum = latrotationsNum - 1;
            }
            formatOut.add(lat);
        } else {
            formatOut.add(lat);
        }
        if (lon > longConstant || longConstant > lon) {
            while (0 < longrotationsNum) {
                lon = lon * -1.0;
                longrotationsNum = longrotationsNum - 1;
            }
            formatOut.add(lon);
        } else {
            formatOut.add(lon);
        }
        return formatOut;
    }
}
