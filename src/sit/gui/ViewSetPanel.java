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
import javax.swing.JPanel;
import sit.sstl.HashMapSet;

/**
 *
 * @author simon
 * @param <K>
 * @param <T>
 */
public class ViewSetPanel<K extends Enum,T extends ViewSetEntry<K>>{


    //the root panel
    private final JPanel panel = new JPanel();

    private final HashMapSet<K, T> views = new HashMapSet();

    private T currentView = null;

    public ViewSetPanel() {
        panel.setLayout(new GridBagLayout());
    }

    public synchronized void registerView(T view){
        views.add(view);
    }

    public synchronized void registerViews(T [] views){
        for (T view : views){
            this.views.add(view);
        }
    }

    public K getCurrentViewType(){
        return currentView.getKey();
    }

    public synchronized void switchToViewType(K viewType){
        updateView(viewType);
    }



    private void updateView(K viewType) {
        if (currentView.getKey()==viewType){
            return;
        }
        if (!views.contains(viewType)){
            throw new RuntimeException("View for type: "+viewType+ " has not been registered!");
        }

        this.currentView = views.get(viewType);

        panel.removeAll();
        panel.add(currentView.getComponent(), getGridBackConstraints());

        //repaint panel
        panel.revalidate();
        panel.repaint();
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
