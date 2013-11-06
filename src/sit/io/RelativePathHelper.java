/*
* Copyright originally from Klaue http://klaue.net16.net/programme/snippets/java/getrelativepath.en.php
*  
* major rework by Simon Thiel 
*
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
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

package sit.io;

import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FIXME: wrong handling of non-canonical/relative sourcedir 
 */
public class RelativePathHelper {

    public String getWorkingDir() {
        return System.getProperty("user.dir") + File.separator;
    }

    public String getPathRelativeToWorkingDir(String targetDir) {
        String cwd = getWorkingDir();
        String result = getRelativePath(cwd, targetDir);

        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                "source:" + cwd + " target:" + targetDir + " result:" + result);

        return result;
    }

    private boolean isWindowsPath(String path) {
        return path.contains("\\");
    }

    private String getSeparator(String path) {
        if (isWindowsPath(path)) {
            return "\\";
        }//else
        return "/";
    }

    private String getSeparatorAsRegex(String path) {
        if (isWindowsPath(path)) {
            return "\\\\";
        }//else
        return "/";
    }

    private String[] getSegmentedPath(String path) {
        return path.split(getSeparatorAsRegex(path));
    }

    public String resolveDottedPath(String dottedPath) throws IllegalPathException {
        String[] paths = getSegmentedPath(dottedPath);
        Vector<String> segments = new Vector();
        for (String element : paths) {
            if (element.equals("..")) {
                if (segments.size() > 0) {
                    segments.removeElementAt(segments.size() - 1);
                } else {
                    throw new IllegalPathException("Path starting with \"..\" cannot be resolved, or too many segments with \"..\"!");
                }
            } else {
                segments.add(element);
            }
        }

        String sep = getSeparator(dottedPath);
        StringBuilder resultStr = new StringBuilder();
        for (String element : segments) {
            resultStr.append(element).append(sep);
        }

        //care about first and last separator
        if (dottedPath.startsWith(sep)) {
            resultStr.insert(0, sep);
        }
        if (!dottedPath.endsWith(sep)) { //we've added to many separators
            resultStr.delete(resultStr.length() - 1, resultStr.length());
        }

        return resultStr.toString();
    }

    public String getRelativePath(final String source, final String target) {

        String sourceDir = source;
        String targetDir = target;

        if (targetDir == null || sourceDir == null) {
            throw new NullPointerException();
        }

        if (sourceDir.startsWith("http://")) {
            if (!targetDir.startsWith("http://")) {
                throw new IllegalArgumentException("If one of the two is an URL, both have to be!");
            }
        }

        if (targetDir.startsWith("http://")) {
            if (!sourceDir.startsWith("http://")) {
                throw new IllegalArgumentException("If one of the Two is an URL, both have to be!");
            }
            String sTmpTarget = targetDir.substring(7);
            String sTmpSource = sourceDir.substring(7);
            sTmpTarget = sTmpTarget.substring(0, sTmpTarget.indexOf('/'));
            sTmpSource = sTmpSource.substring(0, sTmpSource.indexOf('/'));
            if (!sTmpTarget.equals(sTmpSource)) {
                throw new IllegalArgumentException("Two subsites of different Websites");
            }
        }

        boolean windows = false;
        if (targetDir.contains("\\") || sourceDir.contains("\\")) {
            windows = true;
            Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO, "is windows");
        }
        sourceDir = sourceDir.replaceAll("\\\\", "/");
        targetDir = targetDir.replaceAll("\\\\", "/");

        StringBuilder sTargetDir = new StringBuilder(targetDir);
        StringBuilder sSourceDir = new StringBuilder(sourceDir);

        // firstly save the filename so only the dir is left
        int iStart = sTargetDir.lastIndexOf("/") + 1;
        String sFileName = sTargetDir.substring(iStart);
        sTargetDir.delete(iStart, sTargetDir.length());
        iStart = sSourceDir.lastIndexOf("/") + 1;
        sSourceDir.delete(iStart, sSourceDir.length());

        // now we search from the left for a common base of sTargetDir and sSourceDir
        while (sSourceDir.length() != 0 && sTargetDir.length() != 0) {
            int iRelEnd = sSourceDir.indexOf("/");
            int iSrcEnd = sTargetDir.indexOf("/");

            if (sSourceDir.substring(0, iRelEnd).equals(sTargetDir.substring(0, iSrcEnd))) {
                sSourceDir.delete(0, iRelEnd + 1);
                sTargetDir.delete(0, iSrcEnd + 1);
            } else {
                break;
            }
        }

        // now we add a "../" for every folder left in the path of sSourceDir to sResult
        StringBuilder sResult = new StringBuilder("");
        while (sSourceDir.length() != 0) {
            int iRelEnd = sSourceDir.indexOf("/");
            if (iRelEnd == -1) {
                break;
            }
            sResult.append("../");
            sSourceDir.delete(0, iRelEnd + 1);
        }

        // and finally, we add the folders left in the path of sTargetDir to sResult
        while (sTargetDir.length() != 0) {
            int iSrcEnd = sTargetDir.indexOf("/");
            if (iSrcEnd == -1) {
                break;
            }
            sResult.append(sTargetDir.substring(0, iSrcEnd)).append("/");
            sTargetDir.delete(0, iSrcEnd + 1);
        }

        // now we add the file back to the string
        sResult.append(sFileName);
        String result = sResult.toString();
        if (windows) {
            result = result.replaceAll("/", "\\\\");
        }

        return result;
    }

    public String test(String source, String target, String correctPath, boolean shouldWork) {
        try {
            String result = getRelativePath(source, target);
            boolean testWorked = result.equals(correctPath);
            return "source:" + source
                    + " target:" + target
                    + " result:" + result
                    + " test:" + ((testWorked) ? "success" : "fail")
                    + ((testWorked==shouldWork)?" --> good": " --> bad");
        } catch (IllegalArgumentException ex) {
            
            if (!shouldWork){
                Logger.getLogger(getClass().getName()).log(Level.INFO, "source:" + source + " target:" + target + " Exception: " + ex.getMessage() +" --> good");
            }else{
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "source:" + source + " target:" + target + " Exception: " + ex.getMessage() +" --> bad");
            }
        }
        return "";
    }

    public static void main(String[] args) {

        //Test

        RelativePathHelper rph = new RelativePathHelper();

        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("C:\\jogurt\\muesli\\", "C:\\milch\\muesli\\", "..\\..\\milch\\muesli\\", true));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("/home/max/moritz/", "home/max/moritz/", "home/max/moritz/", true));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("/home/../home/max/moritz/", "/home/max/moritz/", "/home/max/moritz/", true));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("/home/max/moritz/", "/home/max/lempke/", "../lempke/", true));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("C:\\muesli\\", "C:\\Programme\\Java\\", "..\\Programme\\Java\\", true));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("http://mywebhome.com/home.html", "http://mywebhome.com/unterseite/test.html", "unterseite/test.html", true));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("http://mywebhome.com/unterseite/test.html", "http://mywebhome.com/home.html", "../home.html", true));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("http://mywebhome.com/home.html", "http://myOTHERwebhome.com/unterseite/test.html", "", false));

    }
}
