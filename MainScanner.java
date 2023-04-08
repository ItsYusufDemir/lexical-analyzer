import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/* Author:
 * Date: 8.04.2023 10:42
 * 
 * Description: 
 */
public class MainScanner {
    public static void main(String[]args){
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

