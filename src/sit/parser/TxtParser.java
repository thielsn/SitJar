
package sit.parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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


    private DATASET getDSInstance(){
        try {
            return (DATASET) dsClass.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(TxtParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TxtParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * engage parsing
     */
    public void parse(){


        try {
            BufferedReader reader = new BufferedReader( inBuffer );
            String line;
            DATASET dataSet = getDSInstance();

            while ((line = reader.readLine())!= null) {

                if (!dataSet.processLine(line)){
                    if (dataSet.isComplete()){
                        dataSets.add(dataSet);
                    }
                    dataSet = getDSInstance();
                    dataSet.processLine(line);
                }
                
            }
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
