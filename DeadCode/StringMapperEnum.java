/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class StringMapperEnum <ET extends Enum>{

   /*  public static enum ITEM_TYPE {Barbar, Alp, Zwerg, Zauberer,
            Goblin,Ork,Fimir,Zombie,Skelett,Mumie,Chaoskrieger,Gargoyle,Hexer,Special,
            Boulders, Table, CupBoard, Library, Rack ,Throne, Altar,
            Weapons, Chest, Fireplace, Sargophagus, AlchemyTable, Stairs,
            SecretDoor, StoneTrap, SpearTrap, PitTrap,
            greenOverLay, redOverLay, blueOverLay, darkOverLay, lightDarkOverlay,
            undefined
    };

    public final static String [] IMAGES_FILE  = {"barbarian.png", "elf.png", "dwarf.png", "Elfwiz.png",
                "Goblin.png","Ork.png", "Fimir.png", "Zombie.png" , "Skelett.png",
                "Mumie.png", "Chaoskrieger.png", "Gargoyle.png", "Hexer.png", "Special.png",
                 "Boulders.png", "Rack_6.png", "Rack_3.png",
                "Rack_3.png", "Rack_6.png", "Rack_1.png", "Rack_6.png",
                "Rack_4.png", "Rack_1.png", "Rack_3.png", "Rack_6.png", "Rack_6.png", "Stairs.png",
                "inv_secretdoor.png", "StoneTrap.png", "SpearTrap.png", "PitTrap.png",
                "greenOVL.png", "redOVL.png", "blueOVL.png", "darkOVL.png", "lightdarkOVL.png",
                "UNDEFINED_TYPE_NO_IMAGE"
    };
    * 
    * **/

    private static final Object instanceMonitor = new Object();
    private static StringMapperEnum<?> instance = null;

   

    public static boolean init(Class stringMapperEnumClass, String [] items){
        synchronized(instanceMonitor){
            if (instance!=null){
                return false;
            }
            try {
                instance = (StringMapperEnum<?>) stringMapperEnumClass.newInstance();
                instance.init(items);
                return true;
            } catch (InstantiationException ex) {
                Logger.getLogger(StringMapperEnum.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(StringMapperEnum.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }        
    }

    public static StringMapperEnum<?> getInstance(){
        synchronized(instanceMonitor){
            return instance;
        }
    }
    private String[] items;



    protected StringMapperEnum() {
       
    }

    private void init(String[] items) {
        this.items = items;
    }


    public Enum<E> getValue(String item){
        for (int i=0;i<items.length;i++){
            if (item.equals(items[i])){
                return ;
            }
        }
    }


}
