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
 *  Description of HTTPParseHelper
 * 
 *  @author Simon Thiel
 *  @date 26.06.2012
 */
package sit.web;

import java.util.HashMap;

/**
 * HTTPParseHelper
 * 
 */
public class HTTPParseHelper {
    
    public static String getValueIfExists(String prefix, String value){
        if (value.toLowerCase().startsWith(prefix.toLowerCase())){            
            String result = value.substring(prefix.length());
            if (result.isEmpty()){
                return result;
            }
            //remove quotes if any
            if (result.equals("\"\"")){
                return "";
            }
            if (result.charAt(0)=='"' && result.charAt(result.length()-1)=='"'){
                result = result.substring(1, result.length()-1);
            }
            return result;
        }
        return null;
    } 
    
  public static HashMap<String, String> parseAndFillFittingValues(String[] prefixes, String[] values){
        HashMap<String, String> result = new HashMap();
        for (String value : values){
            for (String prefix : prefixes){
                String realValue = getValueIfExists(prefix, value);
                if (realValue!=null){
                    result.put(prefix, realValue);
                }
            }
        }
        return result;
    }
}
