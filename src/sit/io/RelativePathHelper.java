/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.io;

import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FIXME: wrong handling of non-canonical/relative sourcedir
 * from http://klaue.net16.net/programme/snippets/java/getrelativepath.en.php
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
        for (int i = 0; i < paths.length; i++) {
            String element = paths[i];

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
            sTmpTarget = sTmpTarget.substring(0, sTmpTarget.indexOf("/"));
            sTmpSource = sTmpSource.substring(0, sTmpSource.indexOf("/"));
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

    public String test(String source, String target, String correctPath) {
        try {
            String result = getRelativePath(source, target);
            return "source:" + source
                    + " target:" + target
                    + " result:" + result
                    + " test:" + ((result.equals(correctPath)) ? "success" : "fail");
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "source:" + source + " target:" + target, ex);
        }
        return "";
    }

    public static void main(String[] args) {

        //Test

        RelativePathHelper rph = new RelativePathHelper();

        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("C:\\jogurt\\muesli\\", "C:\\milch\\muesli\\", "..\\..\\milch\\muesli\\"));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("/home/max/moritz/", "home/max/moritz/", "home/max/moritz/"));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("/home/../home/max/moritz/", "/home/max/moritz/", "/home/max/moritz/"));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("/home/max/moritz/", "/home/max/lempke/", "../lempke/"));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("C:\\muesli\\", "C:\\Programme\\Java\\", "..\\Programme\\Java\\"));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("http://mywebhome.com/home.html", "http://mywebhome.com/unterseite/test.html", "unterseite/test.html"));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("http://mywebhome.com/unterseite/test.html", "http://mywebhome.com/home.html", "../home.html"));
        Logger.getLogger(RelativePathHelper.class.getName()).log(Level.INFO,
                rph.test("http://mywebhome.com/home.html", "http://myOTHERwebhome.com/unterseite/test.html", ""));

    }
}
