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

package sit.gui.lists;

import java.awt.Component;
import java.util.List;
import javax.swing.JList;

/**
 *
 * @author simon
 * @param <DATATYPE>
 * @param <WIDGET>
 */
public class DefaultListViewModel<DATATYPE, WIDGET extends Component> {

    private final DefaultListModel<DATATYPE> model;
    private final ListCellRenderer<DATATYPE, WIDGET> renderer;


    public DefaultListViewModel(List<DATATYPE> items, ListCellRenderer<DATATYPE, WIDGET> renderer) {
        this.model = new DefaultListModel(items);
        this.renderer = renderer;
    }

    public DefaultListViewModel(List<DATATYPE> items, 
            ListCellRenderer<DATATYPE, WIDGET> renderer,  JList<DATATYPE> list) {
        this(items, renderer);
        list.setModel(model);
        list.setCellRenderer(renderer);
    }




    /**
     * @return the model
     */
    public DefaultListModel<DATATYPE> getModel() {
        return model;
    }

    /**
     * @return the renderer
     */
    public ListCellRenderer<DATATYPE, WIDGET> getRenderer() {
        return renderer;
    }





}
