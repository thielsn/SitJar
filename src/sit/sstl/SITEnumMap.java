/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sit.sstl;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
public class SITEnumMap <T extends Enum<T>, Q> extends EnumMap<T,Q>  {


    public SITEnumMap(Class type, final Enum[] keys, final Q[] values)  {

        super(type);
        assert(keys.length == values.length);
        

        for (int i=0;i<keys.length;i++){
            this.put((T)keys[i], values[i]);
        }

    }

    public T lookUp(Q value){

        for ( Entry<T, Q> entry : entrySet()){
            if (entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for ( Entry<T, Q> entry : entrySet()){
            result.append(entry.getKey())
                    .append(" <--> ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return result.toString();

    }
}
