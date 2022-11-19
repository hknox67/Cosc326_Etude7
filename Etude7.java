
/**
 * Program Etude7V4
 * This program creates a geojson file by sanitising inputs read from a text file created by the 
 * program user. This program accepts three different forms of input (1: Standard Input (DD), 
 * 2: Degrees Decimal Minutes (DDM), 3: Degrees Minutes Seconds (DMS)) and converts them to standard form
 * to be displayed on the geoJSON framework.
 @author Hayden Knox    15/06/2022
 */

import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * This class contains the main method of this java program. Using a scanner class and an instance of the file class.
 * This class reads in individual lines of input and checks to see if they are in the correct forma to be processed.
 */
public class Etude7{
    static String labeloutput = "";
    static Pattern label = Pattern.compile(("([A-Za-z]{2,30})"));
    static Pattern numeric = Pattern.compile(("-?\\d+(\\.\\d+)?"));
    static Pattern compassMatchCount = Pattern.compile("([NSEW])");
    static String[] compass = { "N", "S", "W", "E" };
    public static void main(String args[]) throws IOException {
        try {
            File inputFile = new File("userInput.txt.txt");
            Scanner fileReader = new Scanner(inputFile, "UTF-8");
            while (fileReader.hasNextLine()) {
                String inputData = fileReader.nextLine();
                String[] splitInput = inputData.split("[ ,\"'°]+");
                ArrayList<String> convertedSplit = new ArrayList<>();
                ArrayList<String> coords = new ArrayList<>();
                int numericCounter = 0; 
                for (String word : splitInput) {
                    convertedSplit.add(word);
                }
                for (String inputWord : convertedSplit) {
                    if (label.matcher(inputWord).find()) {
                        labeloutput += inputWord + " ";
                            coords.remove(inputWord);
                    } else {
                        coords.add(inputWord);
                    }
                }
                StringBuffer rebuiltString = new StringBuffer();
                for (String coord : coords) {
                rebuiltString.append(coord);
                rebuiltString.append(" ");
                }
                String newString = rebuiltString.toString();
                String[] newSplit = newString.split("[dms ]+");
                ArrayList<String> newArrSplit = new ArrayList<>();
                for (String word : newSplit) {
                          newArrSplit.add(word);
                }
                int compassMatchCounter = 0;
                for (String inputWord : newArrSplit) {
                          if (compassMatchCount.matcher(inputWord).find()) {
                              compassMatchCounter++;
                          }
                }
                String[] stage2Array = new String[newArrSplit.size()];
                for (int i = 0; i < newArrSplit.size(); i++){
                          stage2Array[i] = newArrSplit.get(i);
                }
                for(int n = 0; n < stage2Array.length; n++){
                  if(numeric.matcher(stage2Array[n]).find()){
                      numericCounter++;
                  }
                  }
                if (numericCounter == 2) {
                    String locationString ;
                    StringBuffer standardString = new StringBuffer();
                    for (String coord : stage2Array) {
                    standardString.append(coord);
                    standardString.append(" ");
                    }
                    locationString = standardString.toString();
                    String[] standardInput = inputEvaluation.standardForm(locationString, compassMatchCounter);
                    ArrayList<Double> markerStandardInput = new ArrayList<Double>();
                    for (String input : standardInput) {
                        markerStandardInput.add(Double.parseDouble(input));
                    }
                    numericCounter = 0;
                    inputEvaluation.createMapPoint(markerStandardInput, labeloutput);
                    labeloutput = "";
                } else if (numericCounter == 4) {
                    String locationString ;
                    StringBuffer DDMString  = new StringBuffer();
                    for (String coord : stage2Array) {
                        DDMString.append(coord);
                        DDMString.append(" ");
                    }
                    locationString = DDMString.toString();
                    System.out.println(locationString + "Before error");
                    ArrayList<String> DDMInput = inputEvaluation.DDMCheck(locationString, compassMatchCounter);
                    ArrayList<Double> markerDDMInput = new ArrayList<Double>();
                    for (String input : DDMInput) {
                        markerDDMInput.add(Double.parseDouble(input));
                    }
                    numericCounter = 0;
                    inputEvaluation.createMapPoint(markerDDMInput, labeloutput);
                    labeloutput = "";
                    
                } else if (numericCounter == 6) {
                    String locationString ;
                    StringBuffer DMSString = new StringBuffer();
                    for (String coord : stage2Array) {
                        DMSString.append(coord);
                        DMSString.append(" ");
                    }
                    locationString = DMSString.toString();                    
                    ArrayList<String> DMSInput = inputEvaluation.DMSCheck(locationString, compassMatchCounter);
                    ArrayList<Double> markerDMSInput = new ArrayList<Double>();
                    for (String input : DMSInput) {
                        markerDMSInput.add(Double.parseDouble(input));
                    }
                    numericCounter = 0;
                    inputEvaluation.createMapPoint(markerDMSInput, labeloutput);
                    labeloutput = "";
                    //In the event that a line is encountered which does not match any of the specified forms
                    //of input, An error message is printed to the user which presents the incorrect textfile
                    // entry.

                    // This case usually covers unacceptable symbols including @#!
                } else {
                    System.out.println("Unable to process :" + inputData + " cant match");
                }
            }
            //This closes the input reader once all the lines of text are read.
            fileReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
