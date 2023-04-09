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
    static String currentToken = "";
    static int line = 0;
    static int column = 0;
    static int tokenStartingColumn = 0;



    public static void main(String[]args) throws IOException{

        FileReader F=new FileReader("input.txt");

        while(currentChar != '\uffff') {  //F.read() returns -1 which is considered as \uffff since it is type cast to char

            //SKIP
            if (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r'){ //If the current char is space, tab or new line, skip
                lex(F);
                continue;
            }



            //BRACKETS






            //CHAR








            //STRING









            //IDENTIFIER AND KEYWORD
            if(currentChar == '!' || currentChar == '*' || currentChar == '/' || currentChar == ':' || currentChar == '<'
                    || currentChar == '=' || currentChar == '>' || currentChar == '!' || currentChar == '?' || isLetter(currentChar) ){

                tokenStartingColumn = column;

                boolean haveError = false;
                currentToken += currentChar; //Start recording the token
                lex(F);

                while(currentChar != ' ' && currentChar != '\uffff' && currentChar != '\n' && currentChar != '\r'){
                    currentToken += currentChar; //Continue recording

                   if(isLetter(currentChar) || isDecDigit(currentChar) || currentChar == '.' || currentChar == '+' || currentChar == '-')
                       lex(F);
                   else
                       haveError = true;

                }

                if(haveError)
                    printError(currentToken);
                else{

                    if(isKeyword(currentToken)){

                        if(isBoolean(currentToken))
                            printToken("BOOLEAN");
                        else
                            printToken(currentToken.toUpperCase());

                    }
                    else{
                        printToken("IDENTIFIER");
                    }

                }

                currentToken = ""; //Reset recording
                continue;
            }





            //NUMBER








        }






    }


    //This function updates the global variable currentChar and keeps track of location
    public static void lex(FileReader F) throws IOException {
        currentChar = (char) F.read();


        column++;
        if(currentChar == '\n' || currentChar == '\r'){
            column = 0;
            line++;
        }
    }


    public static boolean isBinaryDigit(char c){

        if(c == '0' || c == '1')
            return true;
        else
            return false;
    }


    public static boolean isDecDigit(char c){

        if( c >= '0' && c <= '9')
            return true;
        else
            return false;
    }



    public static boolean isHexDigit(char c){

        if( (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
            return true;
        else
            return false;
    }

    public static boolean isLetter(char c){

        if(c >= 'a' && c <= 'z')
            return true;
        else
            return false;
    }


    //Printing the error
    public static void printError(String token){

    }



    //Printing the token
    public static void printToken(String token){

    }


    //Check whether a token is keyword or not
    public static boolean isKeyword(String token){

        String keywords[] = {"define", "let", "cond", "if", "begin", "true", "false"};

        for(int i = 0; i < keywords.length; i++){
            if(token.equals(keywords[i]))
                return true;
        }

        return false;
    }



    public static boolean isBoolean(String token){

        if(token.equals("true") || token.equals("false"))
            return true;
        else
            return false;
    }



}

