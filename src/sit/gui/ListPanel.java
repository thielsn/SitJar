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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author simon
 * @param <T> Class of the objects to be added to the list
 */
public class ListPanel<T> implements ListDataListener {

    private final ListPanel<T> listPanelRef = this;
    private final ListPanelCellRenderer<T> cellRenderer;
    private final JPanel panel = new JPanel();

    private ListModel<T> listModel = null;

    class ComponentProps{
        final Component comp;
        final Color oldColor;
        final boolean wasOpaque;
        final boolean isJComponent;

        public ComponentProps(Component comp) {
            this.comp = comp;
            this.isJComponent = JComponent.class.isAssignableFrom(comp.getClass());
            if (this.isJComponent){
                this.wasOpaque = ((JComponent)comp).isOpaque();
            }else{
                this.wasOpaque=true;
            }
            this.oldColor = comp.getBackground();
        }

        private void restore() {
            setOpaque(wasOpaque);
            comp.setBackground(oldColor);
            comp.revalidate();
            comp.repaint();
        }

        private void setOpaque(boolean opaque){
            if (isJComponent){
                ((JComponent)comp).setOpaque(opaque);
            }
        }

        private void setSelected() {
            setOpaque(true);
            comp.setBackground(backGroundSelection);
        }
    }

    private final Vector<ComponentProps> selection = new Vector();

    private Color backGroundSelection = Color.GRAY;
    private Color separationLineColor = Color.BLUE;

    public ListPanel(ListPanelCellRenderer<T> cellRenderer) {
        this.cellRenderer = cellRenderer;
        this.panel.setLayout(new GridBagLayout());

    }

    public void setModel(ListModel<T> listModel) {
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

    public JPanel getPanel() {
        return panel;
    }

    private void refreshPanel() {
        int maxCol = getMaxCol();
        int row = 1;
        panel.removeAll();
        for (int i = 0; i < listModel.getSize(); i++) {
            T element = listModel.getElementAt(i);
            Component[] comps = cellRenderer.getListCellRendererComponent(element, i);
            for (int j = 0; j < comps.length; j++) {
                Component comp = comps[j];
                addMouseListener(comp, createMouseAdapter(element, comps));
                panel.add(comp, getGridBackConstraints(row, j));
            }
            row += 2;//the row counter is always increased by 2 to have space left for adding lines in later - as required
        }
        //add lines
        addLines(maxCol);

        //add spacer panel to fill the remaining Y-space
        addSpacePanel(panel, row + 1, maxCol);
        //repaint panel
        panel.revalidate();
        panel.repaint();
    }

    private GridBagConstraints getGridBackConstraints(int row, int column) {
        return getGridBackConstraints(row, column, 0.5, 0.01);
    }

    private GridBagConstraints getGridBackConstraints(int row, int column, double weightx, double weighty) {
        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = column;
        gridBagConstraints.gridy = row;

        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;

        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.weighty = weighty;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.ipady = 5;
        return gridBagConstraints;
    }

    private void addMouseListener(Component comp, final MouseAdapter mouseListener) {
        comp.addMouseListener(mouseListener);
        //register also subcomponents if comp is a container
        if (Container.class.isAssignableFrom(comp.getClass())) {
            Container compC = (Container) comp;
            for (Component compChild : compC.getComponents()) {
                addMouseListener(compChild, mouseListener);
            }
        }
    }

    private MouseAdapter createMouseAdapter(final T element, final Component[] comps) {
        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                listPanelRef.showSelected(comps);
                cellRenderer.mouseClicked(element, e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }

        };
    }

    private void addSpacePanel(final JPanel root, final int row, final int maxCol) {
        JPanel result = new JPanel();
        result.setOpaque(false);
        root.add(result, getGridBackConstraints(row, maxCol, 1.0, 1.0));
    }

    private void addLine(final JPanel panel, final int row, final int maxCol) {
        JLabel line = new JLabel();

        line.setBorder(BorderFactory.createMatteBorder(1, -1, -1, -1, Color.BLUE));

        GridBagConstraints gbc = getGridBackConstraints(row, 0);
        gbc.gridwidth = maxCol;
        gbc.ipady = 0;
        panel.add(line, gbc);
    }

    private void addLines(final int maxCol) {
        for (int i = 0; i < listModel.getSize() + 1; i++) {
            addLine(panel, i * 2, maxCol);
        }
    }

    private int getMaxCol() {
        return cellRenderer.getColumnSize(); //currently this is fix set by the cell renderer
        //for future releases it might be possible to have different cellRenderers

    }

    
    private void showSelected(Component[] newSelection) {
        //restore old fields        
        for (ComponentProps comp : selection){
            comp.restore();
        }
        //update selection with new selection
        this.selection.clear();
        for (Component comp : newSelection){
            ComponentProps comProp = new ComponentProps(comp);
            this.selection.add(comProp);
            comProp.setSelected();            
        }
    }

    /**
     * @return the backGroundSelection
     */
    public Color getBackGroundSelection() {
        return backGroundSelection;
    }

    /**
     * @return the separationLineColor
     */
    public Color getSeparationLineColor() {
        return separationLineColor;
    }

    /**
     * @param backGroundSelection the backGroundSelection to set
     */
    public void setBackGroundSelection(Color backGroundSelection) {
        this.backGroundSelection = backGroundSelection;
    }

    /**
     * @param separationLineColor the separationLineColor to set
     */
    public void setSeparationLineColor(Color separationLineColor) {
        this.separationLineColor = separationLineColor;
    }

}
