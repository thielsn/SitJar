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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author simon
 * @param <T>
 */
public class ViewStackPanel<T extends JPanel>{

    //the root panel
    private final JPanel panel = new JPanel();

    private final ArrayList<T> viewStack = new ArrayList();

    public ViewStackPanel() {

        panel.setLayout(new GridBagLayout());
    }



    public synchronized void push(T panel){
        viewStack.add(panel);
    }

    public synchronized void updateView() {
        panel.removeAll();

        if (!viewStack.isEmpty()) {
            panel.add(getTopView(), getGridBackConstraints());
        }

        //repaint panel
        panel.revalidate();
        panel.repaint();
    }

    public synchronized T getTopView() {
        int stackSize = viewStack.size();
        if (stackSize==0){
            return null;
        }
        return viewStack.get(stackSize-1);
    }

    public synchronized T popView(){
        int stackSize = viewStack.size();
        if (stackSize==0){
            return null;
        }
        T result = viewStack.remove(stackSize-1);
        return result;
    }

    public synchronized void clear(){
        viewStack.clear();
    }

    public JPanel getPanel() {
        return panel;
    }


    private GridBagConstraints getGridBackConstraints() {
        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.ipadx=5;
        gridBagConstraints.ipady=5;
        return gridBagConstraints;
    }
}
