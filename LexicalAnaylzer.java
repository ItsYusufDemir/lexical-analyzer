import java.io.FileReader;
import java.io.IOException;


/* Authors: @erenduyuk, @selnaydinn and @ItsYusufDemir
 * Date: 8.04.2023 10:42
 * 
 * Description: Making a simple lexical analyzer
 */


public class LexicalAnaylzer {

    //  ---GLOBAL VARIABLES---
    static char previousChar = ' '; //previous char
    static char currentChar = ' ';  //current char
    static String currentToken = ""; //keeps current token
    static int line = 0; //keeps line index of the token
    static int column = 0; //keeps column index of the token
    static int tokenStartingColumn = 0;  //keeps starting column index of the token



    public static void main(String[]args) throws IOException{

        //Scanner input = new Scanner(System.in); HOCA FILE ISMINI KENDISI YAZCAKMIS EN SON BUNU KOYALIM
        //String fileName = input.next();

        //FileReader F=new FileReader(fileName);

        FileReader F =new FileReader("input.txt");  //reading file by using FileReader

        while(currentChar != '\uffff') {  //.read() method that we use inside the lex() method returns -1 while
            //there is no more character in the input file
            // -1 is considered as \uffff  when it is type cast to char

            if (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r'){
                //If the current char is space, tab or new line or skip then we call lex() method which will read the file char by char
                lex(F);
                continue;
            }


            //BRACKETS
            if(isParenthesis(currentChar)) {   //If the current character is a parenthesis, enter this if

                currentToken += currentChar;

                if (currentChar == '(') {
                    printToken("LEFTPAR");
                } else if (currentChar == ')') {
                    printToken("RIGHTPAR");
                } else if (currentChar == '[') {
                    printToken("LEFTSQUAREB");
                } else if (currentChar == ']') {
                    printToken("RIGHTSQUAREB");
                } else if (currentChar == '{') {
                    printToken("LEFTCURLYB");
                } else if (currentChar == '}') {
                    printToken("RIGHTCURLYB");
                }

                lex(F);
                currentToken = "";
                continue;
            }


            /* CHAR READING
             *
             * Char can be given like: 'c'   'd'   '\''   '\n' etc.
             */
            if(currentChar == '\''){ //if taken character is single quote which means data type is char

                boolean haveError = false;
                currentToken += currentChar; //Start recording the token
                lex(F); //read next character


                while (currentChar != ' ' && currentChar != '\uffff' && currentChar != '\n' && currentChar != '\r' && !isParenthesis(currentChar)){ //Read until space character
                    currentToken += currentChar;
                    lex(F);

                }

                if(currentToken.length() == 3) {  //Case: 'c'
                    if(currentToken.charAt(1) == ' ' || currentToken.charAt(1) == '\''){
                        haveError = true; //we have an error if it is ' ' or '''
                    }
                }
                else if(currentToken.length() == 4){  //Case: '\''   or   '\n'   or '\r'
                    if(currentToken.charAt(1) != '\\')
                        haveError = true;

                    if(!(currentToken.charAt(2) == 'n' || currentToken.charAt(2) == 'r'))
                        haveError = true; //we have an error if n or r don't come after \
                }
                else //If the length is not 3 or 4, give error
                    haveError = true;

                if(haveError) //print error message
                    printError(currentToken);
                else //print token as char
                    printToken("CHAR");

                currentToken = ""; //Reset recording
                continue;
            }

            //STRING
            // not correct string examples: "ab\\\cd"
            //                              "abc"def"
            //                              "abc"\\e"

          if(currentChar == '\"') {  //if is starts with double quote
              boolean haveError = false; //set error to false
              currentToken += currentChar; //start recording token
              lex(F); //read next char by using lex() method
              while (currentChar != ' ' && currentChar != '\uffff' && currentChar != '\n' && currentChar != '\r') { //Read until space character
                  currentToken += currentChar; //Continue recording the token
                  lex(F); //read next character until space or end of line or end of file
              }

              if (currentToken.charAt(currentToken.length() - 1) == '\"') { //checking it ends with double quote
                  for (int i = 1; i < currentToken.length() - 1; i++) { //checking token except quotes
                      if (currentToken.charAt(i) == '\\' ) { //if currentChar is backslash character
                          if (currentToken.charAt(i + 1) == '\"' && i != currentToken.length()-2) {
                              //if next char is double quote and also check it is not the ending quote
                              haveError = false; //set error to false
                          } else if (currentToken.charAt(i + 1) == '\\'  ) {
                              //if next char is backslash too
                              haveError = false; //set error to false
                              i++; //increase i by one since not to check matching backslashes
                          } else { //if there is no quote or backslash after the first backslash then there will be an error
                              haveError = true; //set error to true
                          }
                      }
                  }
              }
              else {   //if it doesn't end with double quote it is incorrect
                  haveError = true;
              }
              if (haveError == true) { //print token if error is occured
                  printError(currentToken);
              } else {  //print STRING if error is not occured
                  printToken("STRING");
              }
              currentToken = "";
            continue;
          }

            /* IDENTIFIER AND KEYWORDS
             *
             * first read the token, then check if it is a keyword or not. If not, then print identifier.
             * If it is a keyword, then print its name.
             */
            if(currentChar == '!' || currentChar == '*' || currentChar == '/' || currentChar == ':' || currentChar == '<'
                    || currentChar == '=' || currentChar == '>' || currentChar == '!' || currentChar == '?' || isLetter(currentChar) ){
              //current char can be one of above
                tokenStartingColumn = column;
                boolean haveError = false;  //initializing haveError

                currentToken += currentChar; //Start recording the token
                lex(F); //read next char inside lex() method



                while(currentChar != ' ' && currentChar != '\uffff' && currentChar != '\n' && currentChar != '\r' && !isParenthesis(currentChar)){
                    currentToken += currentChar; //Continue recording

                   if(isLetter(currentChar) || isDecDigit(currentChar) || currentChar == '.' || currentChar == '+' || currentChar == '-')
                       lex(F);
                   else {
                       haveError = true;
                       lex(F);
                   }

                }

                if(haveError) //if an error is occured, print error message
                    printError(currentToken);
                else{

                    if(isKeyword(currentToken)){  //checking token is a keyword or not

                        if(isBoolean(currentToken)) //checking if  it is a boolean expression(true or false) which is
                            // also a keyword, as if print boolean
                            printToken("BOOLEAN");
                        else //otherwise it is a regular keyword, print token in uppercase
                            printToken(currentToken.toUpperCase()); //

                    }
                    else{  //if it is none of above, it is an identifier
                        printToken("IDENTIFIER");
                    }

                }

                currentToken = ""; //Reset recording
                continue;
            }


            //NUMBER
            else if(isDecDigit(currentChar) || currentChar == '+' || currentChar == '-' || currentChar == '.') {
                //checking weather currentChar starts with a digit, '+', '-' or '.' signs
                tokenStartingColumn = column;

                boolean isIdentifier = false; //initializing isIdentifier and haveError as false
                boolean haveError = false;

                currentToken += currentChar; //starts recording the token
                //previousChar = currentChar;
                lex(F); //reads next char
                currentToken += currentChar; //??????

                //checking currentToken consists of only +,-,., or a digit such as 0,1,2,3,4,5,6,7,8,9
                if(currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r' || currentChar == '\uffff') {
                    //checking previous char if it consists of only one of the +,- or . signs; then it cannot be a number
                    //it can only be identifier
                    if (previousChar == '.' || previousChar == '+' || previousChar == '-') {
                        isIdentifier = true;
                    }
                }

                //Checks for two consecutive transaction operators, if it is set error to true
                if((previousChar == '+' && currentChar == '+') || (previousChar == '-' && currentChar == '+') ||
                        (previousChar == '-' && currentChar == '-') || (previousChar == '+' && currentChar == '-') ) {
                    haveError = true;
                }

                //Checks if the number starts with a dot, set error to true
                if(previousChar == '.') {
                    haveError = true;
                }

                //If number is binary, enters this block: 0b...
                if(previousChar == '0' && currentChar == 'b') {
                    lex(F); //reads next char //??????
                    currentToken += currentChar; //continue recording token
                    while(true) {
                        lex(F); //continue reading next char
                        if(currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r' || currentChar == '\uffff') {
                            break; //stop the code if it is blank or new line
                        } else if(!isBinaryDigit(currentChar)) { //if it is not a binary digit there should be an error
                            currentToken += currentChar; //keep recording token since we print it
                            haveError = true; //set error to true
                        } else if(isBinaryDigit(currentChar)) { //if it is a binary digit there is no error
                            currentToken += currentChar; //keep recording token
                        }
                    }
                }

                //If number is hexadecimal enters this block: 0x...
                else if(previousChar == '0' && currentChar == 'x') {
                    lex(F);
                    currentToken += currentChar; //continue recording token
                    while(true) {
                        lex(F); //reads next char
                        if(currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r' || currentChar == '\uffff') {
                            break; //stop the code if it is blank or new line
                        } else if(!isHexDigit(currentChar)) { //if it is not a hexadecimal digit there should be an error
                            currentToken += currentChar; //keep recording token since we print it
                            haveError = true; //set error to true
                        } else if(isHexDigit(currentChar)) { //if it is a hexadecimal digit
                            currentToken += currentChar; //keep recording token
                        }
                    }
                }

                //If number is decimal or float enters this block
                else {
                    while(true) {
                        lex(F); //reads next char
                        //Checks for two consecutive transaction operators
                        if((previousChar == '+' && currentChar == '+') || (previousChar == '-' && currentChar == '+') ||
                                (previousChar == '-' && currentChar == '-') || (previousChar == '+' && currentChar == '-')) {
                            haveError = true;
                        }

                        //Checks if the token has reached the end
                        if(currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r' || currentChar == '\uffff') {
                            //Checks for errors in e and transaction operators
                            if (previousChar == 'e' || previousChar == 'E' || previousChar == '+' || previousChar == '-') {
                                haveError = true;
                            }
                            break;
                        }
                        //Checks for errors in e and transaction operators example 334Ee+2, ++4536
                        else if ((previousChar == 'E' && currentChar == 'e') || (previousChar == 'e' && currentChar == 'E') ||
                                (previousChar == 'E' && currentChar == 'E') || (previousChar == 'E' && currentChar == 'E') ||
                                (previousChar == 'E' && currentChar == '.') || (previousChar == '.' && currentChar == 'E') ||
                                (previousChar == '.' && currentChar == '+') || (previousChar == '+' && currentChar == '.') ||
                                (previousChar == '.' && currentChar == '-') || (previousChar == '-' && currentChar == '.')) {
                            currentToken += currentChar;
                            haveError = true;
                        }
                        else {
                            currentToken += currentChar;
                        }
                        //Checks for errors in e and transaction operators example 432E+34E-2
                        if (!isHaveOne(currentToken, '.') || !isHaveOne(currentToken, 'E') || !isHaveTwo(currentToken)) {
                            haveError = true;
                        }

                    }
                }

                //Checks for errors in e example 354.42E-2
                if((currentToken.contains("E") && currentToken.contains("."))) {
                    if(currentToken.indexOf('E') < currentToken.indexOf('.')) {
                        haveError = true;
                    }
                }

                //Checks for errors in e example 354.42e-2
                if((currentToken.contains("e") && currentToken.contains("."))) {
                    if(currentToken.indexOf('e') < currentToken.indexOf('.')) {
                        haveError = true;
                    }
                }

                //Finally, it checks the token and acts accordingly
                if (haveError) {
                    printError(currentToken);
                }
                else if (isIdentifier) {
                    printToken("IDENTIFIER");
                }
                else {
                    printToken("NUMBER");
                }

                currentToken = ""; //Reset recording
            }//number block end
        }

    }


    //This function updates the global variable currentChar and keeps track of location
    public static void lex(FileReader F) throws IOException {
        previousChar = currentChar; //assign value of current char to previous char
        currentChar = (char) F.read(); //update current char by reading file


        column++; //in each read of char, column index should be increased by one
        if(currentChar == '\n' || currentChar == '\r'){  //if we go to new line
            column = 0; //assign column index to 0 since it turns back to the beginning of the line
            line++;  //increase line index by 1
        }
    }

    // this method checks token is a binary digit or not
    public static boolean isBinaryDigit(char c){

        if(c == '0' || c == '1')   //if it is  0 or 1, returns true
            return true;
        else
            return false;
    }

    // this method checks token is a decimal digit or not
    public static boolean isDecDigit(char c){

        if( c >= '0' && c <= '9')  // if it is one of 0,1,2,3,4,5,6,7,8,9 returns true
            return true;
        else
            return false;
    }


    //this method checks token is a hexadecimal digit or not
    public static boolean isHexDigit(char c){

        if( (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))  //it can be a decimal digit from 0
            //up to 9, or a letter from a to f, or again a letter from A to F
            return true;
        else
            return false;
    }

    //this method checks weather token is a letter or not
    public static boolean isLetter(char c){

        if(c >= 'a' && c <= 'z')
            return true;
        else
            return false;
    }


    //Printing the error
    public static void printError(String token){
        System.out.println("LEXICAL ERROR" + "[" + line + ":" + column + "]: Invalid token '" + token + "'");
    }



    //Printing the token
    public static void printToken(String token){
        System.out.println(token + " " + line + ":" + column );
    }


    //This method checks weather a token is keyword or not
    public static boolean isKeyword(String token){

        String keywords[] = {"define", "let", "cond", "if", "begin", "true", "false"};

        for(int i = 0; i < keywords.length; i++){  //comparing token with keywords
            if(token.equals(keywords[i]))
                return true;
        }

        return false;
    }


   //This method checks token is a boolean expression or not
    public static boolean isBoolean(String token){

        if(token.equals("true") || token.equals("false"))
            return true;
        else
            return false;
    }


    //Returns false if there is more than one given char in the String
    public static boolean isHaveOne(String str, char ch) {
        str = str.toUpperCase(); //set string to uppercase
        int count = 0;
        for (int i = 0; i < str.length(); i++) { //counts the occurrence of given char
            if (str.charAt(i) == ch) {
                count++;
            }
        }

        if(count > 1) {
            return false;
        } else {
            return true;
        }
    }

    //Returns false if there is more than two + or - in the String.
    public static boolean isHaveTwo(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) { //counts the occurrence of + or -
            if (str.charAt(i) == '+' || str.charAt(i) == '-') {
                count++;
            }
        }

        if(count > 2) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isParenthesis(char c){
        if(c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}')
            return true;
        else
            return false;
    }

}

