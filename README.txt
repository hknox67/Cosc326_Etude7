Date: 16/06/22

Program: Etude7 Geojson Coordinate Map 
@Author: Hayden Knox 2485875

iponut file: userInput.xtx

Summary: 
This program reads in a series of input lines of text from a specified text file. 
Each line of input must follow either one of the three formats; Standard format, DDM
(Decimal Degrees Minutes) or DMS (decimal Degrees Minutes). Using iteration of split
characters from the input string and a series of various conditions to check input 
validity the program will correct any data which is acceptable to be processed to
a geojson file. If the Etude7 program is successful is correcly formating an acceptable
input line. A location point will be created with the input data converted into the 
standard form format in the output geojson Map file. If however a line of input is 
not able to be corrected. An error message along with the incorrect input adjacent
will be printed out in the terminal of the coding IDE and it will not be written to
the geojson file. 

This programs functions mainly by the use of the split method to divide the lines of 
input from the text file on white space and possible UTF-8 symbols which can occour 
in a string. Splitting on these charcters divides the input neatly and simulteneously 
removes these unwanted characters. Then using regex statements on the array of divided
strings, The label words and numbers in the array are counted. Whatever count number
occours for coordinates determines what method will be called to process a specific 
input format.

Once the program has been run and the gejson file has been produced. Theres two options 
to display the results. Visit the website https://geojson.io/#map=2/20.0/0.0. And either 
copy and paste the text produced in the geojson file from the IDE to the left text pane
on the geojson website to produce the user map point results on the map. Or upload the 
geojson file directly from the directory which it is saved to. this be done by clicking 
open above the map header.