/*
 *  Description of MimeTypes
 * 
 *  @author Simon Thiel
 *  @version $Revision: $
 *  @date 22.06.2012
 */ 
package sit.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MimeTypes
 *
 */
public class MimeTypes {
    /*
     * static map
     */

    private final static Map<String, String> mimeTypes;

    static {
        HashMap<String, String> myMimeTypes = new HashMap();

        myMimeTypes.put("", "content/unknown");
        myMimeTypes.put(".3dm", "x-world/x-3dmf"); // 
        myMimeTypes.put(".3dmf", "x-world/x-3dmf"); // 
        myMimeTypes.put(".a", "application/octet-stream");
        myMimeTypes.put(".aab", "application/x-authorware-bin");
        myMimeTypes.put(".aam", "application/x-authorware-map");
        myMimeTypes.put(".aas", "application/x-authorware-seg");
        myMimeTypes.put(".abc", "text/vnd.abc");
        myMimeTypes.put(".acgi", "text/html");
        myMimeTypes.put(".afl", "video/animaflex");
        myMimeTypes.put(".ai", "application/postscript"); // 
        myMimeTypes.put(".aif", "audio/x-aiff");
        myMimeTypes.put(".aifc", "audio/x-aiff");
        myMimeTypes.put(".aiff", "audio/x-aiff");
        myMimeTypes.put(".aim", "application/x-aim");
        myMimeTypes.put(".aip", "text/x-audiosoft-intra");
        myMimeTypes.put(".ani", "application/x-navi-animation");
        myMimeTypes.put(".aos", "application/x-nokia-9000-communicator-add-on-software");
        myMimeTypes.put(".aps", "application/mime");
        myMimeTypes.put(".arc", "application/octet-stream");
        myMimeTypes.put(".arj", "application/octet-stream");
        myMimeTypes.put(".art", "image/x-jg");
        myMimeTypes.put(".asd", "application/astound"); // 
        myMimeTypes.put(".asf", "video/x-ms-asf");
        myMimeTypes.put(".asm", "text/x-asm");
        myMimeTypes.put(".asn", "application/astound");
        myMimeTypes.put(".asp", "text/asp");
        myMimeTypes.put(".asx", "video/x-ms-asf");
        myMimeTypes.put(".au", "audio/basic");
        myMimeTypes.put(".avi", "video/x-msvideo");
        myMimeTypes.put(".avs", "video/avs-video");
        myMimeTypes.put(".bcpio", "application/x-bcpio"); // BCPIO-File
        myMimeTypes.put(".bin", "application/octet-stream"); // 
        myMimeTypes.put(".bm", "image/bmp");
        myMimeTypes.put(".bmp", "image/bmp");
        myMimeTypes.put(".boo", "application/book");
        myMimeTypes.put(".book", "application/book");
        myMimeTypes.put(".boz", "application/x-bzip2");
        myMimeTypes.put(".bsh", "application/x-bsh");
        myMimeTypes.put(".bz", "application/x-bzip");
        myMimeTypes.put(".bz2", "application/x-bzip2");
        myMimeTypes.put(".c", "text/plain");
        myMimeTypes.put(".c++", "text/plain");
        myMimeTypes.put(".cab", "application/x-shockwave-flash");
        myMimeTypes.put(".cat", "application/vnd.ms-pki.seccat");
        myMimeTypes.put(".cc", "text/plain");
        myMimeTypes.put(".ccad", "application/clariscad");
        myMimeTypes.put(".cco", "application/x-cocoa");
        myMimeTypes.put(".cdf", "application/x-cdf");
        myMimeTypes.put(".cer", "application/x-x509-ca-cert");
        myMimeTypes.put(".cha", "application/x-chat");
        myMimeTypes.put(".chat", "application/x-chat");
        myMimeTypes.put(".chm", "application/mshelp");
        myMimeTypes.put(".cht", "audio/x-dspeeh");
        myMimeTypes.put(".class", "application/octet-stream");
        myMimeTypes.put(".cod", "image/cis-cod"); // CIS-Cod-File
        myMimeTypes.put(".com", "application/octet-stream");
        myMimeTypes.put(".conf", "text/plain");
        myMimeTypes.put(".cpio", "application/x-cpio"); // CPIO-File
        myMimeTypes.put(".cpp", "text/plain");
        myMimeTypes.put(".cpt", "application/mac-compactpro");
        myMimeTypes.put(".crl", "application/pkcs-crl");
        myMimeTypes.put(".crt", "application/x-x509-ca-cert");
        myMimeTypes.put(".csh", "application/x-csh"); // C-Shellscript-File
        myMimeTypes.put(".css", "text/css"); // CSS
        myMimeTypes.put(".csv", "text/comma-separated-values");
        myMimeTypes.put(".cxx", "text/plain");
        myMimeTypes.put(".dcr", "application/x-director"); // 
        myMimeTypes.put(".deepv", "application/x-deepv");
        myMimeTypes.put(".def", "text/plain");
        myMimeTypes.put(".der", "application/x-x509-ca-cert");
        myMimeTypes.put(".dif", "video/x-dv");
        myMimeTypes.put(".dir", "application/x-director");
        myMimeTypes.put(".dl", "video/dl");
        myMimeTypes.put(".dll", "application/octet-stream");
        myMimeTypes.put(".doc", "application/msword"); // 
        myMimeTypes.put(".dot", "application/msword");
        myMimeTypes.put(".dp", "application/commonground");
        myMimeTypes.put(".drw", "application/drafting");
        myMimeTypes.put(".dump", "application/octet-stream");
        myMimeTypes.put(".dus", "audio/x-dspeeh");
        myMimeTypes.put(".dv", "video/x-dv");
        myMimeTypes.put(".dvi", "application/x-dvi");
        myMimeTypes.put(".dwf", "drawing/x-dwf");
        myMimeTypes.put(".dwg", "application/acad");
        myMimeTypes.put(".dxf", "application/dxf");
        myMimeTypes.put(".dxr", "application/x-director");
        myMimeTypes.put(".el", "text/x-script.elisp");
        myMimeTypes.put(".elc", "application/x-bytecode.elisp");
        myMimeTypes.put(".env", "application/x-envoy");
        myMimeTypes.put(".eps", "application/postscript");
        myMimeTypes.put(".es", "audio/echospeech"); // Echospeed-File
        myMimeTypes.put(".etx", "text/x-setext"); // SeText-File
        myMimeTypes.put(".evy", "application/x-envoy"); // Envoy-File
        myMimeTypes.put(".exe", "application/octet-stream");
        myMimeTypes.put(".f", "text/plain");
        myMimeTypes.put(".f77", "text/x-fortran");
        myMimeTypes.put(".f90", "text/plain");
        myMimeTypes.put(".fdf", "application/vnd.fdf");
        myMimeTypes.put(".fh4", "image/x-freehand"); // 
        myMimeTypes.put(".fh5", "image/x-freehand"); // 
        myMimeTypes.put(".fif", "image/fif"); // FIF-File
        myMimeTypes.put(".fli", "video/fli");
        myMimeTypes.put(".flo", "image/florian");
        myMimeTypes.put(".flx", "text/vnd.fmi.flexstor");
        myMimeTypes.put(".fmf", "video/x-atomic3d-feature");
        myMimeTypes.put(".for", "text/plain");
        myMimeTypes.put(".fpx", "image/vnd.fpx");
        myMimeTypes.put(".frl", "application/freeloader");
        myMimeTypes.put(".funk", "audio/make");
        myMimeTypes.put(".g", "text/plain");
        myMimeTypes.put(".g3", "image/g3fax");
        myMimeTypes.put(".gif", "image/gif");
        myMimeTypes.put(".gl", "video/gl");
        myMimeTypes.put(".gsd", "audio/x-gsm");
        myMimeTypes.put(".gsm", "audio/x-gsm");
        myMimeTypes.put(".gsp", "application/x-gsp");
        myMimeTypes.put(".gss", "application/x-gss");
        myMimeTypes.put(".gtar", "application/x-gtar");
        myMimeTypes.put(".gz", "application/gzip"); // GNU
        myMimeTypes.put(".gzip", "application/x-gzip");
        myMimeTypes.put(".h", "text/plain");
        myMimeTypes.put(".hdf", "application/x-hdf");
        myMimeTypes.put(".help", "application/x-helpfile");
        myMimeTypes.put(".hgl", "application/vnd.hp-hpgl");
        myMimeTypes.put(".hh", "text/plain");
        myMimeTypes.put(".hlb", "text/x-script");
        myMimeTypes.put(".hlp", "application/mshelp"); //
        myMimeTypes.put(".hpg", "application/vnd.hp-hpgl");
        myMimeTypes.put(".hqx", "application/binhex");
        myMimeTypes.put(".hta", "application/hta");
        myMimeTypes.put(".htc", "text/x-component");
        myMimeTypes.put(".htm", "text/html"); // 
        myMimeTypes.put(".html", "text/html");
        myMimeTypes.put(".htmls", "text/html");
        myMimeTypes.put(".htt", "text/webviewhtml");
        myMimeTypes.put(".htx", "text/html");
        myMimeTypes.put(".ice", "x-conference/x-cooltalk");
        myMimeTypes.put(".ico", "image/x-icon");
        myMimeTypes.put(".idc", "text/plain");
        myMimeTypes.put(".ief", "image/ief");
        myMimeTypes.put(".iefs", "image/ief");
        myMimeTypes.put(".iges", "application/iges");
        myMimeTypes.put(".igs", "application/iges");
        myMimeTypes.put(".ima", "application/x-ima");
        myMimeTypes.put(".imap", "application/x-httpd-imap");
        myMimeTypes.put(".inf", "application/inf");
        myMimeTypes.put(".ins", "application/x-internett-signup");
        myMimeTypes.put(".ip", "application/x-ip2");
        myMimeTypes.put(".isu", "video/x-isvideo");
        myMimeTypes.put(".it", "audio/it");
        myMimeTypes.put(".iv", "application/x-inventor");
        myMimeTypes.put(".ivr", "i-world/i-vrml");
        myMimeTypes.put(".ivy", "application/x-livescreen");
        myMimeTypes.put(".jam", "audio/x-jam");
        myMimeTypes.put(".jav", "text/plain");
        myMimeTypes.put(".java", "text/plain");
        myMimeTypes.put(".jcm", "application/x-java-commerce");
        myMimeTypes.put(".jfif", "image/jpeg");
        myMimeTypes.put(".jfif-tbnl", "image/jpeg");
        myMimeTypes.put(".jpe", "image/jpeg");
        myMimeTypes.put(".jpeg", "image/jpeg"); // 
        myMimeTypes.put(".jpg", "image/jpeg");
        myMimeTypes.put(".jps", "image/x-jps");
        myMimeTypes.put(".js", "text/javascript"); // JavaScript-File
        myMimeTypes.put(".json", "application/json"); // Json        
        myMimeTypes.put(".jut", "image/jutvision");
        myMimeTypes.put(".kar", "audio/midi");
        myMimeTypes.put(".ksh", "application/x-ksh");
        myMimeTypes.put(".la", "audio/nspaudio");
        myMimeTypes.put(".lam", "audio/x-liveaudio");
        myMimeTypes.put(".latex", "application/x-latex");
        myMimeTypes.put(".lha", "application/octet-stream");
        myMimeTypes.put(".lhx", "application/octet-stream");
        myMimeTypes.put(".list", "text/plain");
        myMimeTypes.put(".lma", "audio/nspaudio");
        myMimeTypes.put(".log", "text/plain");
        myMimeTypes.put(".lst", "text/plain");
        myMimeTypes.put(".lsx", "text/x-la-asf");
        myMimeTypes.put(".ltx", "application/x-latex");
        myMimeTypes.put(".lzh", "application/octet-stream");
        myMimeTypes.put(".lzx", "application/octet-stream");
        myMimeTypes.put(".m", "text/plain");
        myMimeTypes.put(".m1v", "video/mpeg");
        myMimeTypes.put(".m2a", "audio/mpeg");
        myMimeTypes.put(".m2v", "video/mpeg");
        myMimeTypes.put(".m3u", "audio/x-mpequrl");
        myMimeTypes.put(".man", "application/x-troff-man");
        myMimeTypes.put(".map", "application/x-navimap");
        myMimeTypes.put(".mar", "text/plain");
        myMimeTypes.put(".mbd", "application/mbedlet");
        myMimeTypes.put(".mc$", "application/x-magic-cap-package-1.0");
        myMimeTypes.put(".mcd", "application/mcad");
        myMimeTypes.put(".mcf", "image/vasa");
        myMimeTypes.put(".mcp", "application/netmc");
        myMimeTypes.put(".me", "application/x-troff-me");
        myMimeTypes.put(".mht", "message/rfc822");
        myMimeTypes.put(".mhtml", "message/rfc822");
        myMimeTypes.put(".mid", "audio/midi");
        myMimeTypes.put(".midi", "audio/midi");
        myMimeTypes.put(".mif", "application/mif"); // FrameMaker
        myMimeTypes.put(".mime", "message/rfc822");
        myMimeTypes.put(".mjf", "audio/x-vnd.audioexplosion.mjuicemediafile");
        myMimeTypes.put(".mjpg", "video/x-motion-jpeg");
        myMimeTypes.put(".mm", "application/base64");
        myMimeTypes.put(".mme", "application/base64");
        myMimeTypes.put(".mod", "audio/mod");
        myMimeTypes.put(".moov", "video/quicktime");
        myMimeTypes.put(".mov", "video/quicktime"); // 
        myMimeTypes.put(".movie", "video/x-sgi-movie");
        myMimeTypes.put(".mp2", "audio/mpeg");
        myMimeTypes.put(".mp3", "audio/mpeg3");
        myMimeTypes.put(".mpa", "audio/mpeg");
        myMimeTypes.put(".mpc", "application/x-project");
        myMimeTypes.put(".mpe", "video/mpeg"); // 
        myMimeTypes.put(".mpeg", "video/mpeg"); // 
        myMimeTypes.put(".mpg", "video/mpeg"); // 
        myMimeTypes.put(".mpga", "audio/mpeg");
        myMimeTypes.put(".mpp", "application/vnd.ms-project");
        myMimeTypes.put(".mpt", "application/x-project");
        myMimeTypes.put(".mpv", "application/x-project");
        myMimeTypes.put(".mpx", "application/x-project");
        myMimeTypes.put(".mrc", "application/marc");
        myMimeTypes.put(".ms", "application/x-troff-ms");
        myMimeTypes.put(".mv", "video/x-sgi-movie");
        myMimeTypes.put(".my", "audio/make");
        myMimeTypes.put(".mzz", "application/x-vnd.audioexplosion.mzz");
        myMimeTypes.put(".nap", "image/naplps");
        myMimeTypes.put(".naplps", "image/naplps");
        myMimeTypes.put(".nc", "application/x-netcdf");
        myMimeTypes.put(".ncm", "application/vnd.nokia.configuration-message");
        myMimeTypes.put(".niff", "image/x-niff");
        myMimeTypes.put(".nix", "application/x-mix-transfer");
        myMimeTypes.put(".nsc", "application/x-nschat"); // NS
        myMimeTypes.put(".nvd", "application/x-navidoc");
        myMimeTypes.put(".o", "application/octet-stream");
        myMimeTypes.put(".oda", "application/oda"); // Oda-File
        myMimeTypes.put(".omc", "application/x-omc");
        myMimeTypes.put(".omcd", "application/x-omcdatamaker");
        myMimeTypes.put(".omcr", "application/x-omcregerator");
        myMimeTypes.put(".p", "text/x-pascal");
        myMimeTypes.put(".p10", "application/pkcs10");
        myMimeTypes.put(".p12", "application/pkcs-12");
        myMimeTypes.put(".p7a", "application/x-pkcs7-signature");
        myMimeTypes.put(".p7c", "application/pkcs7-mime");
        myMimeTypes.put(".p7m", "application/pkcs7-mime");
        myMimeTypes.put(".p7r", "application/x-pkcs7-certreqresp");
        myMimeTypes.put(".p7s", "application/pkcs7-signature");
        myMimeTypes.put(".part", "application/pro_eng");
        myMimeTypes.put(".pas", "text/pascal");
        myMimeTypes.put(".pbm", "image/x-portable-bitmap"); // PBM
        myMimeTypes.put(".pcl", "application/vnd.hp-pcl");
        myMimeTypes.put(".pct", "image/x-pict");
        myMimeTypes.put(".pcx", "image/x-pcx");
        myMimeTypes.put(".pdb", "chemical/x-pdb");
        myMimeTypes.put(".pdf", "application/pdf"); // Adobe
        myMimeTypes.put(".pfunk", "audio/make");
        myMimeTypes.put(".pgm", "image/x-portable-graymap"); // PBM
        myMimeTypes.put(".php", "application/x-httpd-php"); // 
        myMimeTypes.put(".phtml", "application/x-httpd-php");
        myMimeTypes.put(".pic", "image/pict");
        myMimeTypes.put(".pict", "image/pict");
        myMimeTypes.put(".pkg", "application/x-newton-compatible-pkg");
        myMimeTypes.put(".pko", "application/vnd.ms-pki.pko");
        myMimeTypes.put(".pl", "text/plain");
        myMimeTypes.put(".plx", "application/x-pixclscript");
        myMimeTypes.put(".pm", "image/x-xpixmap");
        myMimeTypes.put(".pm4", "application/x-pagemaker");
        myMimeTypes.put(".pm5", "application/x-pagemaker");
        myMimeTypes.put(".png", "image/png"); // PNG-File
        myMimeTypes.put(".pnm", "image/x-portable-anymap"); // PBM
        myMimeTypes.put(".pot", "application/mspowerpoint");
        myMimeTypes.put(".pov", "model/x-pov");
        myMimeTypes.put(".ppa", "application/vnd.ms-powerpoint");
        myMimeTypes.put(".ppm", "image/x-portable-pixmap"); // PBM
        myMimeTypes.put(".pps", "application/mspowerpoint");
        myMimeTypes.put(".ppt", "application/mspowerpoint"); // 
        myMimeTypes.put(".ppz", "application/mspowerpoint");
        myMimeTypes.put(".pre", "application/x-freelance");
        myMimeTypes.put(".prt", "application/pro_eng");
        myMimeTypes.put(".ps", "application/postscript");
        myMimeTypes.put(".psd", "application/octet-stream");
        myMimeTypes.put(".ptlk", "application/listenup"); // Listenup-File
        myMimeTypes.put(".pvu", "paleovu/x-pv");
        myMimeTypes.put(".pwz", "application/vnd.ms-powerpoint");
        myMimeTypes.put(".py", "text/x-script.phyton");
        myMimeTypes.put(".pyc", "applicaiton/x-bytecode.python");
        myMimeTypes.put(".qcp", "audio/vnd.qcelp");
        myMimeTypes.put(".qd3", "x-world/x-3dmf");
        myMimeTypes.put(".qd3d", "x-world/x-3dmf");
        myMimeTypes.put(".qif", "image/x-quicktime");
        myMimeTypes.put(".qt", "video/quicktime"); // 
        myMimeTypes.put(".qtc", "video/x-qtc");
        myMimeTypes.put(".qti", "image/x-quicktime");
        myMimeTypes.put(".qtif", "image/x-quicktime");
        myMimeTypes.put(".ra", "audio/x-pn-realaudio"); // 
        myMimeTypes.put(".ram", "audio/x-pn-realaudio"); // 
        myMimeTypes.put(".ras", "image/cmu-raster");
        myMimeTypes.put(".rast", "image/cmu-raster");
        myMimeTypes.put(".rexx", "text/x-script.rexx");
        myMimeTypes.put(".rf", "image/vnd.rn-realflash");
        myMimeTypes.put(".rgb", "image/x-rgb"); // RGB-File
        myMimeTypes.put(".rm", "audio/x-pn-realaudio");
        myMimeTypes.put(".rmi", "audio/mid");
        myMimeTypes.put(".rmm", "audio/x-pn-realaudio");
        myMimeTypes.put(".rmp", "audio/x-pn-realaudio");
        myMimeTypes.put(".rng", "application/ringing-tones");
        myMimeTypes.put(".rnx", "application/vnd.rn-realplayer");
        myMimeTypes.put(".roff", "application/x-troff");
        myMimeTypes.put(".rp", "image/vnd.rn-realpix");
        myMimeTypes.put(".rpm", "audio/x-pn-realaudio-plugin"); // RealAudio-Plugin-File
        myMimeTypes.put(".rt", "text/richtext");
        myMimeTypes.put(".rtf", "text/richtext");
        myMimeTypes.put(".rtx", "text/richtext"); // Richtext-File
        myMimeTypes.put(".rv", "video/vnd.rn-realvideo");
        myMimeTypes.put(".s", "text/x-asm");
        myMimeTypes.put(".s3m", "audio/s3m");
        myMimeTypes.put(".saveme", "application/octet-stream");
        myMimeTypes.put(".sbk", "application/x-tbook");
        myMimeTypes.put(".sca", "application/x-supercard"); // Supercard-File
        myMimeTypes.put(".scm", "video/x-scm");
        myMimeTypes.put(".sdml", "text/plain");
        myMimeTypes.put(".sdp", "application/sdp");
        myMimeTypes.put(".sdr", "application/sounder");
        myMimeTypes.put(".sea", "application/sea");
        myMimeTypes.put(".set", "application/set");
        myMimeTypes.put(".sgm", "text/sgml");
        myMimeTypes.put(".sgml", "text/sgml");
        myMimeTypes.put(".sh", "application/x-sh");
        myMimeTypes.put(".shar", "application/x-shar");
        myMimeTypes.put(".shtml", "text/html");
        myMimeTypes.put(".sid", "audio/x-psid");
        myMimeTypes.put(".sit", "application/x-stuffit"); // Stuffit-File
        myMimeTypes.put(".skd", "application/x-koan");
        myMimeTypes.put(".skm", "application/x-koan");
        myMimeTypes.put(".skp", "application/x-koan");
        myMimeTypes.put(".skt", "application/x-koan");
        myMimeTypes.put(".sl", "application/x-seelogo");
        myMimeTypes.put(".smi", "application/smil");
        myMimeTypes.put(".smil", "application/smil");
        myMimeTypes.put(".smp", "application/studiom"); // Studiom-File
        myMimeTypes.put(".snd", "audio/basic");
        myMimeTypes.put(".sol", "application/solids");
        myMimeTypes.put(".spc", "text/x-speech");
        myMimeTypes.put(".spl", "application/futuresplash"); // Flash
        myMimeTypes.put(".spr", "application/x-sprite");
        myMimeTypes.put(".sprite", "application/x-sprite");
        myMimeTypes.put(".src", "application/x-wais-source"); // WAIS
        myMimeTypes.put(".ssi", "text/x-server-parsed-html");
        myMimeTypes.put(".ssm", "application/streamingmedia");
        myMimeTypes.put(".sst", "application/vnd.ms-pki.certstore");
        myMimeTypes.put(".step", "application/step");
        myMimeTypes.put(".stl", "application/sla");
        myMimeTypes.put(".stp", "application/step");
        myMimeTypes.put(".stream", "audio/x-qt-stream"); // Quicktime-Streaming-File
        myMimeTypes.put(".sv4cpio", "application/x-sv4cpio"); // CPIO-File
        myMimeTypes.put(".sv4crc", "application/x-sv4crc"); // CPIO-File
        myMimeTypes.put(".svf", "image/x-dwg");
        myMimeTypes.put(".svr", "application/x-world");
        myMimeTypes.put(".swf", "application/x-shockwave-flash"); // 
        myMimeTypes.put(".t", "application/x-troff"); // 
        myMimeTypes.put(".talk", "text/x-speech"); // 
        myMimeTypes.put(".tar", "application/x-tar"); // tar-Archiv-files
        myMimeTypes.put(".tbk", "application/toolbook"); // Toolbook-File
        myMimeTypes.put(".tcl", "application/x-tcl"); // TCL
        myMimeTypes.put(".tcsh", "text/x-script.tcsh");
        myMimeTypes.put(".tex", "application/x-tex"); // TeX-File
        myMimeTypes.put(".texi", "application/x-texinfo");
        myMimeTypes.put(".texinfo", "application/x-texinfo"); //
        myMimeTypes.put(".text", "text/plain");
        myMimeTypes.put(".tgz", "application/x-compressed");
        myMimeTypes.put(".tif", "image/tiff"); // 
        myMimeTypes.put(".tiff", "image/tiff"); //
        myMimeTypes.put(".tr", "application/x-troff");
        myMimeTypes.put(".troff", "application/x-troff");
        myMimeTypes.put(".tsi", "audio/tsp-audio");
        myMimeTypes.put(".tsp", "application/dsptype"); // TSP-File
        myMimeTypes.put(".tsv", "text/tab-separated-values"); // tabulator-...
        myMimeTypes.put(".turbot", "image/florian");
        myMimeTypes.put(".txt", "text/plain"); // 
        myMimeTypes.put(".uil", "text/x-uil");
        myMimeTypes.put(".uni", "text/uri-list");
        myMimeTypes.put(".unis", "text/uri-list");
        myMimeTypes.put(".unv", "application/i-deas");
        myMimeTypes.put(".uri", "text/uri-list");
        myMimeTypes.put(".uris", "text/uri-list");
        myMimeTypes.put(".ustar", "application/x-ustar");
        myMimeTypes.put(".uu", "application/octet-stream");
        myMimeTypes.put(".uue", "text/x-uuencode");
        myMimeTypes.put(".vcd", "application/x-cdlink");
        myMimeTypes.put(".vcs", "text/x-vcalendar");
        myMimeTypes.put(".vda", "application/vda");
        myMimeTypes.put(".vdo", "video/vdo");
        myMimeTypes.put(".vew", "application/groupwise");
        myMimeTypes.put(".viv", "video/vnd.vivo"); // 
        myMimeTypes.put(".vivo", "video/vivo");
        myMimeTypes.put(".vmd", "application/vocaltec-media-desc");
        myMimeTypes.put(".vmf", "application/vocaltec-media-file");
        myMimeTypes.put(".voc", "audio/voc");
        myMimeTypes.put(".vos", "video/vosaic");
        myMimeTypes.put(".vox", "audio/voxware"); // Vox-File
        myMimeTypes.put(".vqe", "audio/x-twinvq-plugin");
        myMimeTypes.put(".vqf", "audio/x-twinvq");
        myMimeTypes.put(".vql", "audio/x-twinvq-plugin");
        myMimeTypes.put(".vrml", "model/vrml");
        myMimeTypes.put(".vrt", "x-world/x-vrt");
        myMimeTypes.put(".vsd", "application/x-visio");
        myMimeTypes.put(".vst", "application/x-visio");
        myMimeTypes.put(".vsw", "application/x-visio");
        myMimeTypes.put(".vts", "workbook/formulaone"); // 
        myMimeTypes.put(".vtts", "workbook/formulaone"); // 
        myMimeTypes.put(".w60", "application/wordperfect6.0");
        myMimeTypes.put(".w61", "application/wordperfect6.1");
        myMimeTypes.put(".w6w", "application/msword");
        myMimeTypes.put(".wav", "audio/x-wav"); // WAV-File
        myMimeTypes.put(".wb1", "application/x-qpro");
        myMimeTypes.put(".wbmp", "image/vnd.wap.wbmp"); // Bitmap-File
        myMimeTypes.put(".web", "application/vnd.xara");
        myMimeTypes.put(".wiz", "application/msword");
        myMimeTypes.put(".wk1", "application/x-123");
        myMimeTypes.put(".wmf", "windows/metafile");
        myMimeTypes.put(".wml", "text/vnd.wap.wml"); // WML-File
        myMimeTypes.put(".wmlc", "application/vnd.wap.wmlc"); // WMLC-File
        myMimeTypes.put(".wmls", "text/vnd.wap.wmlscript"); // WML-Script-files
        myMimeTypes.put(".wmlsc", "application/vnd.wap.wmlscriptc"); // WML-Script-C--files
        myMimeTypes.put(".word", "application/msword");
        myMimeTypes.put(".wp", "application/wordperfect");
        myMimeTypes.put(".wp5", "application/wordperfect");
        myMimeTypes.put(".wp6", "application/wordperfect");
        myMimeTypes.put(".wpd", "application/wordperfect");
        myMimeTypes.put(".wq1", "application/x-lotus");
        myMimeTypes.put(".wri", "application/mswrite");
        myMimeTypes.put(".wrl", "model/vrml"); // Visualisierung
        myMimeTypes.put(".wrz", "model/vrml");
        myMimeTypes.put(".wsc", "text/scriplet");
        myMimeTypes.put(".wsrc", "application/x-wais-source");
        myMimeTypes.put(".wtk", "application/x-wintalk");
        myMimeTypes.put(".xbm", "image/x-xbitmap"); // XBM-File
        myMimeTypes.put(".xdr", "video/x-amt-demorun");
        myMimeTypes.put(".xgz", "xgl/drawing");
        myMimeTypes.put(".xif", "image/vnd.xiff");
        myMimeTypes.put(".xl", "application/excel");
        myMimeTypes.put(".xla", "application/excel");
        myMimeTypes.put(".xlb", "application/excel");
        myMimeTypes.put(".xlc", "application/excel");
        myMimeTypes.put(".xld", "application/excel");
        myMimeTypes.put(".xlk", "application/excel");
        myMimeTypes.put(".xll", "application/excel");
        myMimeTypes.put(".xlm", "application/excel");
        myMimeTypes.put(".xls", "application/excel");
        myMimeTypes.put(".xlt", "application/excel");
        myMimeTypes.put(".xlv", "application/excel");
        myMimeTypes.put(".xlw", "application/excel");
        myMimeTypes.put(".xm", "audio/xm");
        myMimeTypes.put(".xml", "text/xml"); // XML-File        
        myMimeTypes.put(".xmz", "xgl/movie");
        myMimeTypes.put(".xpix", "application/x-vnd.ls-xpix");
        myMimeTypes.put(".xpm", "image/xpm");
        myMimeTypes.put(".x-png", "image/png");
        myMimeTypes.put(".xsr", "video/x-amt-showrun");
        myMimeTypes.put(".xwd", "image/x-windowdump"); // X-Windows
        myMimeTypes.put(".xyz", "chemical/x-pdb");
        myMimeTypes.put(".z", "application/x-compress"); // zlib-compressed
        myMimeTypes.put(".zip", "application/zip"); // ZIP-Archiv-files
        myMimeTypes.put(".zoo", "application/octet-stream");
        myMimeTypes.put(".zsh", "text/x-script.zsh");

        mimeTypes = Collections.unmodifiableMap(myMimeTypes);


    }

    /**
     * looks up mimetype for give filetype in ".abc" style
     *
     * @param fileType
     * @return
     */
    public static String getMimeType(String fileType) {
        return mimeTypes.get(fileType);
    }

    public static String getMimeTypeFromFileName(String fileName) {

        int ind = fileName.lastIndexOf('.');
        
        if (ind > 0) {
            return  getMimeType(fileName.substring(ind));
        }
        
        return getMimeType("");
    }
}
