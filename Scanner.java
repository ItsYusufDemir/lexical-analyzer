import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/* Authors: @erenduyuk, @selnaydinn and @ItsYusufDemir
 * Date: 8.04.2023 10:42
 * 
 * Description: Making a simple lexical analyzer
 */







public class Scanner {

    //GLOBAL VARIABLES
    static char currentChar = ' ';



    public static void main(String[]args) throws IOException{

        FileReader F=new FileReader("input.txt");


        while(currentChar != '\uffff') {  //F.read() returns -1 which is considered as \uffff since it is type casted to char

            //SKIP
            if (currentChar == ' ' | currentChar == '\t' | currentChar == '\n'){ //If the current char is space, tab or new line, skip
                continue;
            }



            //BRACKETS






            //STRING READING





            //IDENTIFIER





            //NUMBER



        }






    }


    //This function updates the global variable currentChar
    public static void lex(FileReader F) throws IOException {
        currentChar = (char) F.read();
    }



}

