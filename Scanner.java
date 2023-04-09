import java.io.FileReader;
import java.io.IOException;


/* Authors: @erenduyuk, @selnaydinn and @ItsYusufDemir
 * Date: 8.04.2023 10:42
 * 
 * Description: Making a simple lexical analyzer
 */


public class Scanner {

    //GLOBAL VARIABLES
    static char previousChar = ' ';
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










            /* CHAR READING
             *
             * Char will be given in unicode like '\u1234'
             */
            if(currentChar == '\''){

                boolean haveError = false;
                currentToken += currentChar;
                lex(F);

                while (currentChar != ' ' && currentChar != '\uffff' && currentChar != '\n' && currentChar != '\r'){
                    currentToken += currentChar;
                    lex(F);
                }

                if(currentToken.length() == 4){  //If we have '\''
                    if(!(currentToken.charAt(0) == '\'' && currentToken.charAt(1) == '\\' && currentToken.charAt(2) == '\'' && currentToken.charAt(3) == '\''))
                        haveError = true;
                }
                else if (currentToken.length() == 8 ) {  //If we have '\u12345
                    if(!(currentToken.charAt(0) == '\'' && currentToken.charAt(1) == '\\' && currentToken.charAt(2) == 'u' && isHexDigit(currentToken.charAt(3))
                    && isHexDigit(currentToken.charAt(4)) && isHexDigit(currentToken.charAt(5)) && isHexDigit(currentToken.charAt(6)) && currentToken.charAt(7) == '\'' ))
                        haveError = true;
                }
                else
                    haveError = true;


                if(haveError)
                    printError(currentToken);
                else
                    printToken("CHAR");

                currentToken = ""; //Reset recording
                continue;
            }





            //STRING













            /* IDENTIFIER AND KEYWORDS
             *
             * first read the token, than check if it is a keyword or not. If not, then print identifier.
             * If it is a keyword, then print its name.
             */
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
                    //Buraya da bi tane daha lex lazım??
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
        previousChar = currentChar;
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

