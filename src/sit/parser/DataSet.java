/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.parser;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public abstract class DataSet {

    protected boolean complete = false;

    public DataSet(){
        
    }

    /**
     * processes subsequent lines of a dataset
     * returns true in case the line still belongs to a dataset
     * in case the dataset was completed already it returns false
     * @param line
     * @return
     */
    public abstract boolean processLine(String line);

    /**
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }
}
