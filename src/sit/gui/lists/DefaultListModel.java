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

import java.util.List;
import java.util.Vector;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author simon
 */
public class DefaultListModel<T> implements ListModel<T> {

    private final List<T> items;
    private final List<ListDataListener> listeners = new Vector();

    public DefaultListModel(List<T> items) {
        this.items = items;
    }


    public int getSize() {
        return items.size();
    }

    public T getElementAt(int index) {
        return items.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

}
