/**
 * Copyright:     Copyright (c) 2002
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.tools;

public class StatusString {

    /**
     *
     */
    private String myStatus;

    /**
     *
     */
    public StatusString() {
        myStatus = "";
    }

    /**
     *
     * @param myString
     */
    public void setStatus(String myString) {
        myStatus = myString;
    }

    /**
     *
     * @param myString
     */
    public void addStatus(String myString) {
        myStatus = myStatus + myString + "\n";
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return myStatus;
    }
}
