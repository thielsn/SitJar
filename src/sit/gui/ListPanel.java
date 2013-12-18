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

package sit.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


/**
 *
 * @author simon
 * @param <T> Class of the objects to be added to the list
 */
public class ListPanel<T> implements ListDataListener {

    private final ListPanelCellRenderer<T> cellRenderer;
    private final JPanel panel = new JPanel();

    private ListModel<T> listModel = null;

    public ListPanel(ListPanelCellRenderer<T> cellRenderer) {
        this.cellRenderer = cellRenderer;
        this.panel.setLayout(new GridBagLayout());

    }

    public void setModel(ListModel<T> listModel){
        this.listModel = listModel;
        //remove first to make sure only the model has only be added once
        listModel.removeListDataListener(this);
        listModel.addListDataListener(this);
        refreshPanel();
    }

    public void intervalAdded(ListDataEvent e) {
        refreshPanel();
    }

    public void intervalRemoved(ListDataEvent e) {
        refreshPanel();
    }

    public void contentsChanged(ListDataEvent e) {
        refreshPanel();
    }


    public JPanel getPanel(){
        return panel;
    }

    private void refreshPanel() {
        int maxCol=0;
        panel.removeAll();
        for (int i=0; i<listModel.getSize(); i++){
            T element = listModel.getElementAt(i);
            Component [] comps = cellRenderer.getListCellRendererComponent(element, i, false, false);
            for (int j=0; j<comps.length;j++){
                Component comp = comps[j];
                panel.add(comp, getGridBackConstraints(i, j));
            }
            maxCol=Math.max(maxCol, comps.length);
        }
        //add spacer panel to fill the remaining Y-space
        panel.add(new JPanel(), getGridBackConstraints(listModel.getSize(), maxCol, 1.0, 1.0));
        //repaint panel
        panel.revalidate();
        panel.repaint();
    }

     private GridBagConstraints getGridBackConstraints(int row, int column) {
         return getGridBackConstraints(row, column, 0.5, 0.001);
     }

    private GridBagConstraints getGridBackConstraints(int row, int column, double weightx, double weighty) {
        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = column;
        gridBagConstraints.gridy = row;

        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;

        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.weighty = weighty;
        gridBagConstraints.ipadx=12;
        gridBagConstraints.ipady=5;
        return gridBagConstraints;
    }




}
