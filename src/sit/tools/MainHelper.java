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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author simon
 */
public class MainHelper {

    public static class Parameter {

        private final ArrayList<String> params = new ArrayList();

        public Parameter() {
        }

        public boolean hasOption(String option) {
            return params.contains(option);
        }

        public String getOptionParameter(String option) {

            int index = params.indexOf(option);
            if (index == -1 || index >= params.size() - 1) {
                return null;
            }
            return params.get(index + 1);
        }

        private void parseParams(String[] args) {
            params.addAll(Arrays.asList(args));
        }

        public int size() {
            return params.size();
        }

        public List<String> getParamsFrom(int index) {
            if (index >= params.size() - 1) {
                return null;
            }
            return new ArrayList(params.subList(index+1, params.size()));
        }

        public String getParamsNAfterOption(String option, int nAfterOption){
            int index =getOptionIndex(option);
            if (index<0 || (index+nAfterOption>params.size()-1)){
                return null;
            }
            return params.get(index+nAfterOption);
        }

        /**
         * Returns the index of the first occurrence of the specified option in
         * this list, or -1 if this list does not contain the element. More
         * formally, returns the lowest index <tt>i</tt> such that
         * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
         * or -1 if there is no such index.
         *
         * @param option
         * @return
         */
        public int getOptionIndex(String option) {
            return params.indexOf(option);
        }
    }

    public static Parameter getParams(String[] args) {
        Parameter result = new Parameter();
        result.parseParams(args);
        return result;
    }

    //TEST
    public static void main(String[] args) {
        Parameter paramsEmpty = getParams(new String[]{""});
        Parameter params = getParams(new String[]{"a", "b", "c", "d"});
        System.out.println("has b: " + params.hasOption("b"));
        System.out.println("getOptionParam of a: " + params.getOptionParameter("a"));
        System.out.println("getOptionParam of c: " + params.getOptionParameter("c"));

        System.out.println("getOptionParam of d: " + params.getOptionParameter("d"));
        System.out.println("getOptionIndex of a: " + params.getOptionIndex("a"));
        System.out.println("getOptionIndex of d: " + params.getOptionIndex("d"));
        System.out.println("getOptionIndex of e: " + params.getOptionIndex("e"));
    }
}
