/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        FontMetrics fm = g.getFontMetrics();

        Dimension result = new Dimension(0,0);
        result.height = text.size() * fm.getHeight();

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
