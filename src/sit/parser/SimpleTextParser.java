/*
* Copyright 2013 Simon Thiel
*
* This file is part of SitJar.
*
* SitJar is free software: you can redistribute it and/or modify
* it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SitJar is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
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
