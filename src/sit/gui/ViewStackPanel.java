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


    private final ArrayList<ViewStackPanelEntry<T>> viewStack = new ArrayList();

    public ViewStackPanel() {

        this.setLayout(new GridBagLayout());
    }

     public synchronized void push(final T panel, final String caption) {
         push(panel, caption, new ViewStackPanelHandler() {

             public void onLoadAfterPop() {
                 //do nothing
             }

             public void onLoadAfterPush() {
                 //do nothing
             }
         });
    }

    public synchronized void push(final T panel, final String caption, final ViewStackPanelHandler handler) {
        ViewStackPanelEntry<T> stackEntry = new ViewStackPanelEntry<T>() {

            public T getPanel() {
                return panel;
            }

            public String getCaption() {
                return caption;
            }

            public void onLoadAfterPop() {
                handler.onLoadAfterPop();
            }

            public void onLoadAfterPush() {
                handler.onLoadAfterPush();
            }
        };
        push(stackEntry);
    }

   

    public synchronized void push(ViewStackPanelEntry<T> stackEntry) {
        viewStack.add(stackEntry);
        stackEntry.onLoadAfterPush();
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
        return viewStack.get(stackSize - 1).getPanel();
    }

    public synchronized String getTopViewCaption() {
        int stackSize = viewStack.size();
        if (stackSize == 0) {
            return null;
        }
        return viewStack.get(stackSize - 1).getCaption();
    }

    public synchronized T popView() {
        int stackSize = viewStack.size();
        if (stackSize == 0) {
            return null;
        }
        ViewStackPanelEntry<T>  result = viewStack.remove(stackSize - 1);

        if (stackSize>1){ //after pop there is still the at least one panel available
            //stacksize was not updated after removing the top most panel
            viewStack.get(stackSize - 2).onLoadAfterPop();
        }

        updateView();
        return result.getPanel();
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


    public int getStackSize(){
        return viewStack.size();
    }
}
