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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import sit.sstl.HashMapSet;
import sit.sstl.ObjectWithKey;

/**
 *
 * @author simon
 * @param <K>
 * @param <T>
 */
public class ViewSetPanel<K extends Enum,T extends ViewSetEntry<K>>{

    public static final int MODE_CREATE_ON_VIEW=0;
    public static final int MODE_CREATE_ONCE=0;

    

    

    public static class ViewSetPanelException extends RuntimeException {

        public ViewSetPanelException(String message) {
           super(message);
        }
    }



    class ViewSetEntryContainer implements ObjectWithKey<K>{
        private final Class entryClass;
        private final K key;
        private T viewEntry;
        private boolean firstView = true;

        public ViewSetEntryContainer(K key, Class viewEntryClass){
            this.entryClass = viewEntryClass;
            this.key = key;
            this.viewEntry = null;
        }

        public ViewSetEntryContainer(K key, T viewEntry) {
            this.key = key;
            this.viewEntry = viewEntry;
            this.entryClass = viewEntry.getClass();
        }

        public void setFirstView(boolean firstView) {
            this.firstView = firstView;
        }

        public boolean isFirstView() {
            return firstView;
        }

        public T getViewEntry() {
            return viewEntry;
        }

        public K getKey() {
            return key;
        }

        private void checkAndInstantiate() {
            if (viewEntry==null){
                try {
                    viewEntry = (T) entryClass.newInstance();
                } catch (InstantiationException ex) {
                    Logger.getLogger(ViewSetPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ViewSetPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //the root panel
    private final JPanel panel;

    private final HashMapSet<K, ViewSetEntryContainer> views = new HashMapSet();

    private ViewSetEntryContainer currentView = null;
    private final int mode;

    public ViewSetPanel(JPanel panel, int mode) {
        this.panel = panel;
        this.mode = mode;
    }

    public ViewSetPanel(int mode) {
        this(new JPanel(), mode);
    }


    public ViewSetPanel(JPanel panel) {
        this(panel, MODE_CREATE_ONCE);
    }

    public ViewSetPanel() {
        this(new JPanel());
    }

    public synchronized void registerView(K key, Class<? extends T> viewClass){
        views.add(new ViewSetEntryContainer(key, viewClass));
    }

    public synchronized void registerView(Map<K, Class<? extends T>> viewMap){
        for (Map.Entry<K, Class<? extends T>> entry : viewMap.entrySet()){
            this.views.add(new ViewSetEntryContainer(entry.getKey(), entry.getValue()));
        }
    }


    public synchronized void registerView(T view){
        views.add(new ViewSetEntryContainer(view.getKey(), view));
    }

    public synchronized void registerViews(T [] views){
        for (T view : views){
            this.views.add(new ViewSetEntryContainer(view.getKey(), view));
        }
    }

    public K getCurrentViewType(){
        return currentView.getKey();
    }

    public synchronized void switchToViewType(K viewType){
        updateView(viewType);
    }


    private void updateView(K viewType) {

        if ((currentView!=null) && (currentView.getKey()==viewType)){
            return; //view was shown already
        }
        if (!views.contains(viewType)){
            throw new RuntimeException("View for type: "+viewType+ " has not been registered!");
        }
        
        ViewSetEntryContainer newView = getViewForType(viewType);

        //call onHide on previous view
        if (this.currentView!=null){
            this.currentView.getViewEntry().onHide();
        }

        this.currentView = newView;

        //call onShow for new view
        this.currentView.getViewEntry().onShow(this.currentView.isFirstView());
        this.currentView.setFirstView(false);

        panel.removeAll();
        panel.add(currentView.getViewEntry().getComponent(), getGridBackConstraints());

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


    /**
     * ATTENTION this method returns the actual component - changes will be reflected accordingly
     * @param viewType
     * @return
     */
    public synchronized T getView(K viewType) {
        if (currentView.getKey()==viewType){
            return currentView.getViewEntry();
        }
        if (mode==MODE_CREATE_ON_VIEW){
            throw new ViewSetPanelException("In MODE_CREATE_ON_VIEW mode: getView only defined for current view");
        }

        return views.get(viewType).getViewEntry();
    }

    private ViewSetEntryContainer getViewForType(K viewType) {
        ViewSetEntryContainer result = views.get(viewType);

        //in case of mode == MODE_CREATE_ON_VIEW make a copy so, it can be garbaged after hiding the view;
        if (mode==MODE_CREATE_ON_VIEW){
            result = getInstancedViewSetEntryContainer(result);
        }else{            
            //intantiate view if not already
            result.checkAndInstantiate();
        }

        return result;
    }

    private ViewSetEntryContainer getInstancedViewSetEntryContainer(ViewSetEntryContainer viewContainer) {
        ViewSetEntryContainer result = new ViewSetEntryContainer(viewContainer.key, viewContainer.entryClass);
        result.checkAndInstantiate();
        return result;
    }
}
