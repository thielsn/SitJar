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

import java.awt.Component;
import java.awt.Dialog;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author simon
 */
public class SwingHelper {

    public static int PADDING = 1;

    public static Window getParentWindowForComponent(Component comp){
        return  SwingUtilities.windowForComponent(comp);
    }

    public static Frame getParentFrameForCompomponent(Component comp){
        Window window = getParentWindowForComponent(comp);

        // or pass 'this' if you are inside the panel
        Frame parentFrame = null;
        if (window instanceof Frame) {
            parentFrame = (Frame) window;
        }
        return parentFrame;
    }

    public static Dialog getParentDialogForCompomponent(Component comp){
        Window window = getParentWindowForComponent(comp);

        // or pass 'this' if you are inside the panel
        Dialog dialog = null;
        if (window instanceof Dialog) {
            dialog = (Dialog) window;
        }
        return dialog;
    }

    public static JDialog getNewDialogForParentComponent(Component parent, String title, boolean modal){
        return getNewDialogForParentComponent(parent, title,
                modal?Dialog.ModalityType.APPLICATION_MODAL:Dialog.ModalityType.MODELESS);
    }

    public static JDialog getNewDialogForParentComponent(Component parent, String title, Dialog.ModalityType modal){
        Frame frame = getParentFrameForCompomponent(parent);
        if (frame!=null){
            return new JDialog(frame, title, modal);
        }//else
        Dialog dialog = getParentDialogForCompomponent(parent);
        if (dialog!=null){
            return new JDialog(dialog, title, modal);
        }//else
        throw new RuntimeException("Parent window of component is neither a frame nor a dialog!");
    }


    public static void setBoundsForJLabel(JLabel label, int xOffset, int yOffset){
        setBoundsForComponent(label, label.getText(), xOffset, yOffset);
    }

    public static void setBoundsForComponent(JComponent comp, String text, int xOffset, int yOffset){
        Rectangle bounds = calculateBoundsForComponent(comp, text, xOffset, yOffset);
        comp.setBounds(bounds);
    }

    public static Rectangle calculateBoundsForComponent(JComponent comp, String text, int xOffset, int yOffset){

        FontMetrics fm = comp.getFontMetrics(comp.getFont());
        Rectangle2D bounds = fm.getStringBounds(text, comp.getGraphics());

        return new Rectangle(
                xOffset+(int)bounds.getX(),
                yOffset,
                (int)Math.ceil(bounds.getWidth())+(PADDING*2),
                (int)Math.ceil(bounds.getHeight()+fm.getDescent())+(PADDING*2));
    }

    public static void setWidthAndHeightForTextComponent(JComponent comp, String text){
        Rectangle bounds = calculateBoundsForComponent(comp, text, 0, 0);
        comp.setSize(bounds.width, bounds.height);
    }

}
