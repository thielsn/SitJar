/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon
 */
public class SimpleTextParser {
    
    
    
    public static abstract class LineProcessor{
         public abstract void processLine(ParseContext context, String line);
    }
    
    private final Reader inBuffer;
    private final LineProcessor processor;
    

    public SimpleTextParser(String filename, LineProcessor processor) throws FileNotFoundException{       
        
        this.inBuffer = new FileReader(new File(filename));
        this.processor = processor;
    }

    public SimpleTextParser(File file, LineProcessor processor) throws FileNotFoundException{       
        
        this.inBuffer = new FileReader(file);
        this.processor = processor;
    }
    
    public SimpleTextParser(Reader inBuffer, LineProcessor processor) {
        this.inBuffer = inBuffer;
        this.processor = processor;
    }

    

    /**
     * engage parsing
     * @param context 
     */
    public void parse(ParseContext context){

        try {
            BufferedReader reader = new BufferedReader( inBuffer );
            String line;

            while ((line = reader.readLine())!= null) {
                processor.processLine(context, line);
            }
            //close file etc...
            reader.close();
            
        }
        catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }



       
}
