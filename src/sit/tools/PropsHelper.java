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
 * PropsHelper.java
 *  @author Simon Thiel <simon.thiel at gmx.de>
 * Created on 25. Maerz 2006, 22:51
 */

package sit.tools;

/**
 *
 * @author  thiel
 */
public class PropsHelper {
    
    /** singleton */
    protected static PropsHelper myPropsHelper = new PropsHelper();
    
    protected java.util.Hashtable properties = new java.util.Hashtable();
    
    /** Creates a new instance of PropsHelper */
    protected PropsHelper() {
    }
    
    public void addPropertys(DefaultProps myProp){
        properties.put(myProp.getHeader(), myProp);
    }
    
    public DefaultProps getPropertys(String header){
        return (DefaultProps)properties.get(header);
    }
    
    public DefaultProps loadNAddProps(DefaultProps myProp){
        if (!properties.containsKey(myProp.header)){
            
            //if file does not exist
            //write default values to file
            if (!myProp.loadFromFile()){
                myProp.overwriteFileWithDefaults();
            }
            addPropertys(myProp);
        }
        return getPropertys(myProp.header);
    }
    
    
    
    public static PropsHelper getInstance(){
        return myPropsHelper;
    }
}
