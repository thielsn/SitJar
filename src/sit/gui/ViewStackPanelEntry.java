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

import javax.swing.JPanel;

/**
 *
 * @author simon
 * @param <T>
 */
public interface ViewStackPanelEntry <T extends JPanel> extends ViewStackPanelHandler{
    public T getPanel();
    public String getCaption();
    /**
     * this call can be overwritten as a general interface to refresh 
     * content-based view items when some data might have been changed
     * this is typically called from a controller
     */
    public void refreshView();
}
