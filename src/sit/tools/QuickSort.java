/*
 * QuickSort.java
 *
 * Created on 25. Januar 2005, 11:11
 */

package sit.tools;
import java.util.Vector;

/**
 *
 * @author Simon Thiel
 */
public class QuickSort {
    
    /** Creates a new instance of QuickSort */
    public QuickSort() {
    }
    
    
    /**
     * Sort the entire vector, if it is not empty
     * @param elements
     */
    public void quickSort(Vector elements)
    { if (! elements.isEmpty())
      { this.quickSort(elements, 0, elements.size()-1);
      }
    }
    
    
    /**
     * QuickSort.java by Henk Jan Nootenboom, 9 Sep 2002
     * Copyright 2002-2005 SUMit. All Rights Reserved.
     *
     * Algorithm designed by prof C. A. R. Hoare, 1962
     * See http://www.sum-it.nl/en200236.html
     * for algorithm improvement by Henk Jan Nootenboom, 2002.
     *
     * Recursive Quicksort, sorts (part of) a Vector by
     *  1.  Choose a pivot, an element used for comparison
     *  2.  dividing into two parts:
     *      - less than-equal pivot
     *      - and greater than-equal to pivot.
     *      A element that is equal to the pivot may end up in any part.
     *      See www.sum-it.nl/en200236.html for the theory behind this.
     *  3. Sort the parts recursively until there is only one element left.
     *
     * www.sum-it.nl/QuickSort.java this source code
     * www.sum-it.nl/quicksort.php3 demo of this quicksort in a java applet
     *
     * Permission to use, copy, modify, and distribute this java source code
     * and its documentation for NON-COMMERCIAL or COMMERCIAL purposes and
     * without fee is hereby granted.
     * See http://www.sum-it.nl/security/index.html for copyright laws.
     * @param elements
     * @param lowIndex
     * @param highIndex
     */
    private void quickSort(Vector elements, int lowIndex, int highIndex){
      int lowToHighIndex;
      int highToLowIndex;
      int pivotIndex;
      
      Comparable pivotValue;  // values 
      Comparable lowToHighValue;
      Comparable highToLowValue;
      Comparable parking; //values end
      
      int newLowIndex;
      int newHighIndex;
      int compareResult;
      
      lowToHighIndex = lowIndex;
      highToLowIndex = highIndex;
      /** Choose a pivot, remember it's value
       *  No special action for the pivot element itself.
       *  It will be treated just like any other element.
       */
      pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
      pivotValue = getValue(elements.elementAt(pivotIndex));
      
      /** Split the Vector in two parts.
       *
       *  The lower part will be lowIndex - newHighIndex,
       *  containing elements <= pivot Value
       *
       *  The higher part will be newLowIndex - highIndex,
       *  containting elements >= pivot Value
       */
      
      newLowIndex = highIndex + 1;
      newHighIndex = lowIndex - 1;
      // loop until low meets high
      while ((newHighIndex + 1) < newLowIndex) // loop until partition complete
      { // loop from low to high to find a candidate for swapping
          lowToHighValue = getValue(elements.elementAt(lowToHighIndex)); 
          while (lowToHighIndex < newLowIndex
          & lowToHighValue.compareTo(pivotValue)<0 )
          { newHighIndex = lowToHighIndex; // add element to lower part
            lowToHighIndex ++;
            lowToHighValue = getValue(elements.elementAt(lowToHighIndex));
          }
          
          // loop from high to low find other candidate for swapping
          highToLowValue = getValue(elements.elementAt(highToLowIndex));
          while (newHighIndex <= highToLowIndex
          & (highToLowValue.compareTo(pivotValue)>0)
          )
          { newLowIndex = highToLowIndex; // add element to higher part
            highToLowIndex --;
            highToLowValue = getValue(elements.elementAt(highToLowIndex));
          }
          
          // swap if needed
          if (lowToHighIndex == highToLowIndex) // one last element, may go in either part
          { newHighIndex = lowToHighIndex; // move element arbitrary to lower part
          }
          else if (lowToHighIndex < highToLowIndex) // not last element yet
          { compareResult = lowToHighValue.compareTo(highToLowValue);
            if (compareResult >= 0) // low >= high, swap, even if equal
            { parking = lowToHighValue;
              elements.setElementAt(highToLowValue, lowToHighIndex);
              elements.setElementAt(parking, highToLowIndex);
              
              newLowIndex = highToLowIndex;
              newHighIndex = lowToHighIndex;
              
              lowToHighIndex ++;
              highToLowIndex --;
            }
          }
      }
      
      // Continue recursion for parts that have more than one element
      if (lowIndex < newHighIndex)
      { this.quickSort(elements, lowIndex, newHighIndex); // sort lower subpart
      }
      if (newLowIndex < highIndex)
      { this.quickSort(elements, newLowIndex, highIndex); // sort higher subpart
      }
    }
    
    
    
    /**
     *
     * @param o
     * @return
     */    
    private Comparable getValue(Object o){
        if (o==null) return null;
        return (Comparable)o;
    }
}
