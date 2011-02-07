/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author thiel
 */
public class SettingsFactory <T extends SettingsRoot> {

   private  File settingFile = null;

   public  String generateFileName(){
        return System.getProperty("user.dir")+System.getProperty("file.separator")+"settings.xml";
    }

    public File getSettingsFileForWrite(){
        if (settingFile!=null){
            return settingFile;
        }

        settingFile = new File(generateFileName());

        if (settingFile.isDirectory()){
            settingFile = null;
        }

        return settingFile;
    }


    public File getSettingsFileForRead(){
        if (settingFile!=null){
            return settingFile;
        }

        settingFile = new File(generateFileName());
        if ((!settingFile.exists())
                || (!settingFile.canRead())){

            settingFile = null;
        }
        return settingFile;
    }

  public T loadSettings(Class<T> theClass) {
        T result = null;

        if (getSettingsFileForRead() == null) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "cannot load settings at " + generateFileName());
            return null;
        }
        Logger.getLogger(getClass().getName()).info("load settings...");

        try {

            JAXBContext context = JAXBContext.newInstance(theClass);
            Unmarshaller um = context.createUnmarshaller();
            result = (T) um.unmarshal(new FileReader(getSettingsFileForRead()));

            return result;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public void saveSettings(T settings) {

        if (getSettingsFileForWrite() == null) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "cannot save settings at " + generateFileName());
            return;
        }
        Logger.getLogger(getClass().getName()).info(
                "save settings... ");



        try {
            JAXBContext context = JAXBContext.newInstance(settings.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(settings, new FileOutputStream(getSettingsFileForWrite()));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
