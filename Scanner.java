import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/* Authors: @erenduyuk, @selnaydinn and @ItsYusufDemir
 * Date: 8.04.2023 10:42
 * 
 * Description: 
 */







public class Scanner {

    //GLOBAL VARIABLES
    static char currentChar = ' ';


    public static void main(String[]args){

        while(currentChar != EOF) {  //I don't know how to do it yet

            if (currentChar == ' ' | currentChar == '\t' | currentChar == '\n'){ //If the current char is space, tab or new line, skip
                lex();
                continue;
            }

            //BRACKETS





            //STRING READING





            //IDENTIFIER





            //NUMBER



        }






    }


    //lex() function
    public static void lex(){

        File input = new File("input.txt");
        FileReader scan = null;
        try {
            scan = new FileReader(input);
            int content=0;
            while( (content = scan.read())!= -1){
                System.out.println((char)content);

            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}

