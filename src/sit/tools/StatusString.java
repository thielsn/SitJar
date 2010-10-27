package sit.tools;

/**
 * Überschrift:   
 * Beschreibung:  
 * Copyright:     Copyright (c) 2002
 * Organisation:
 * @author Simon
 * @version 1.0
 */

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
  public void setStatus(String myString){
   myStatus=myString ;
  }
  /**
   *
   * @param myString
   */  
  public void addStatus(String myString){
   myStatus = myStatus + myString + "\n";
  }
  /**
   *
   * @return
   */  
  public String getStatus(){
    return myStatus;
  }
}