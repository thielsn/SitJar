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
 * Helper tool for rendering text on java.awt.Graphics
 *
 * @version $Revision: $
 */
package sit.graphics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class GraphicText {

    public int getTextWidth(Graphics g, String text){
        FontMetrics fm = g.getFontMetrics();
        return fm.stringWidth(text);
    }

    public Dimension getTextDimension(Graphics g, Vector<String> text){
        return getTextDimension(g, text.toArray(new String[text.size()]));
    }

    public Dimension getTextDimension(Graphics g, String[] text){
        FontMetrics fm = g.getFontMetrics();

        Dimension result = new Dimension(0,0);
        result.height = text.length * fm.getHeight();

        for (String line : text){
            result.width = Math.max(result.width, fm.stringWidth(line));                       
        }

        return result;
    }

    public int getTextHeight(Graphics g){
        FontMetrics fm = g.getFontMetrics();
        return fm.getHeight();
    }

    private int drawIntoRect(Graphics g, String[] words, Point position, Dimension size, boolean doDrawing) {

        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getHeight();

        int curX = position.x;
        int curY = position.y + lineHeight;
        
        for (String word : words) {
            // Find out the width of the word.
            int wordWidth = fm.stringWidth(word + " ");

            // If text exceeds the width, then move to next line.
            if ((curX + wordWidth) >= (position.x + size.width)) {
                curY += lineHeight;
                curX = position.x;

            }

            if (doDrawing) {
                g.drawString(word, curX, curY);
            }

            // Move over to the right for next word.
            curX += wordWidth;
        }
        return curY;
    }

    public void drawIntoRect(Graphics g, String text, Point position, Dimension size, Dimension margin) {

        //backup old font
        Font oldFont = g.getFont();

        Point myPos = new Point(position);
        Dimension mySize = new Dimension(size);

        myPos.x = myPos.x+margin.width;
        myPos.y = myPos.y+margin.height;
        mySize.width = mySize.width - (2*margin.width);
        mySize.height = mySize.height - (2*margin.height);
                
        String[] words = text.split(" ");
        int endY = myPos.y + mySize.height;

        while (endY >= myPos.y + mySize.height) {

            if (g.getFont().getSize()<2){ //we do not paint such a small size
                return;
            }
            endY = drawIntoRect(g, words, myPos, mySize, false);
            //as long as the text is not fitting, decrease the text size
            g.setFont(g.getFont().deriveFont((float) g.getFont().getSize() - 1));
        }
        //actually write it down
        drawIntoRect(g, words, myPos, mySize, true);

        //restore font
        g.setFont(oldFont);

    }

    
}
