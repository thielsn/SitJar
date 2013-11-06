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

/**
 * JTextAreaConsoleHandler.java
 *
 * 11-May-2010
 *
 * @author Simon Thiel <simon.thiel@gmx.de>
 *
 *
 */



package sit.gui;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;

/**
 * class JTextAreaConsoleHandler
 *
 */
public class JTextAreaConsoleHandler extends StreamHandler {

    private JTextArea textArea = null;

    public JTextAreaConsoleHandler(JTextArea textArea) {
        super();
        this.textArea = textArea;
    }

    @Override
    public synchronized void flush() {
        textArea.revalidate();
        
    }

    @Override
    public synchronized void publish(LogRecord record) {
        textArea.append(getFormatter().format(record));
    
    }



}
