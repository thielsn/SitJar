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
 * along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>. * 
 */
package sit.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author simon
 */
public class JTextfieldHintHelper {

    public static void addHint(JTextComponent textCmp, String hintText) {
        textCmp.setText(hintText);
        registerEvents(textCmp, hintText);
    }


    private static void registerEvents(final JTextComponent textCmp, final String hintText) {
        textCmp.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textCmp.getText().equals(hintText)) {
                    textCmp.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                //do nothing
            }
        });
    }
}
