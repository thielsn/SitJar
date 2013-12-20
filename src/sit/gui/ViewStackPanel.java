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
public class ViewStackPanel<T extends JPanel> extends JPanel {

    class StackEntry {

        T view;
        String caption;

        public StackEntry(T view, String caption) {
            this.view = view;
            this.caption = caption;
        }

    }

    private final ArrayList<StackEntry> viewStack = new ArrayList();

    public ViewStackPanel() {

        this.setLayout(new GridBagLayout());
    }

    public synchronized void push(T panel, String caption) {
        viewStack.add(new StackEntry(panel, caption));
        updateView();
    }

    private void updateView() {
        this.removeAll();

        if (!viewStack.isEmpty()) {
            this.add(getTopView(), getGridBackConstraints());
        }

        //repaint panel
        this.revalidate();
        this.repaint();
    }

    public synchronized T getTopView() {
        int stackSize = viewStack.size();
        if (stackSize == 0) {
            return null;
        }
        return viewStack.get(stackSize - 1).view;
    }

    public synchronized String getTopViewCaption() {
        int stackSize = viewStack.size();
        if (stackSize == 0) {
            return null;
        }
        return viewStack.get(stackSize - 1).caption;
    }

    public synchronized T popView() {
        int stackSize = viewStack.size();
        if (stackSize == 0) {
            return null;
        }
        StackEntry result = viewStack.remove(stackSize - 1);
        updateView();
        return result.view;
    }

    public synchronized void clear() {
        viewStack.clear();
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
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        return gridBagConstraints;
    }
}
