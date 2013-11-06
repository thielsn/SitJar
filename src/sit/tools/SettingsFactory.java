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
 *  @author Simon Thiel <simon.thiel at gmx.de>
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

/**
 *
 * @author thiel
 */
public class SettingsFactory<T extends SettingsRoot> {

    private File settingFile = null;

    public String generateFileName() {
        return System.getProperty("user.dir") + System.getProperty("file.separator") + "settings.xml";
    }

    public File getSettingsFileForWrite() {
        if (settingFile != null) {
            return settingFile;
        }

        settingFile = new File(generateFileName());

        if (settingFile.isDirectory()) {
            settingFile = null;
        }

        return settingFile;
    }

    public File getSettingsFileForRead() {
        if (settingFile != null) {
            return settingFile;
        }

        settingFile = new File(generateFileName());
        if ((!settingFile.exists())
                || (!settingFile.canRead())) {

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
