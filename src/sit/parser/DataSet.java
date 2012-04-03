/*
 *  Description of DataSet
 *
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 *  @date 26.03.2012
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
     * @return true in case the line still belongs to a dataset; in case the dataset was completed already it returns false
     */
    public abstract boolean processLine(String line);

    /**
     * indicates that this dataset was the last one found in the file
     * gives an oppurtunity to mark the dataset as completed which will
     * cause to have it added to the list of recognized datasets
     */
    public abstract void reachedEndOfFile();

    /**
     * @return the complete
     */
    public boolean isComplete() {
        return complete;
    }
}
