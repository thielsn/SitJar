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

/*
 *  Description of TxtParser
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @date 26.03.2012
 */
package sit.parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @param <DATASET> 
 * @author thiel
 */
public class TxtParser <DATASET extends DataSet> implements Iterable<DATASET>{

    
    
    private Class dsClass;
    private Vector<DATASET> dataSets = new Vector();
    private Reader inBuffer = null;
    

    public TxtParser(String filename, Class dataSetClass) throws FileNotFoundException{       
        this.dsClass = dataSetClass;
        this.inBuffer = new FileReader(new File(filename));
    }

     public TxtParser(Reader inputStream, Class dataSetClass) {
        this.dsClass = dataSetClass;
        this.inBuffer = inputStream;
    }

    /**
     * produce new data set
     * @return
     */
    private DATASET getDSInstance(){
        try {
            return (DATASET) dsClass.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(TxtParser.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TxtParser.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * engage parsing
     */
    public void parse(ParseContext context){


        try {
            BufferedReader reader = new BufferedReader( inBuffer );
            String line;
            DATASET dataSet = getDSInstance();

            while ((line = reader.readLine())!= null) {

                if (!dataSet.processLine(context, line)){
                    if (dataSet.isComplete()){
                        dataSets.add(dataSet);
                    }
                    dataSet = getDSInstance(); //new DATASET()
                    dataSet.processLine(context, line);
                }
                
            }
            //give the last dataset the chance to be completed
            dataSet.reachedEndOfFile();
            if (!dataSets.contains(dataSet)){ //in case this dataset was not yet added
                if (dataSet.isComplete()){
                    dataSets.add(dataSet);
                }
            }
            //close file etc...
            reader.close();
            Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                    "successfully processed " + dataSets.size()+" data sets");
        }
        catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int size(){
        return dataSets.size();
    }

    public Iterator<DATASET> iterator() {
        return dataSets.iterator();
    }


       
}
