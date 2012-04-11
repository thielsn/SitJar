/*
 *  @author Simon Thiel <simon.thiel at gmx.de>
 *  @version $Revision: $
 */
package sit.json;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Vector;

/**
 *
 * @author simon
 */
public class JSONObject {

    public final static int JSON_TYPE_OBJECT = 0;
    public final static int JSON_TYPE_COLLECTION = 1;
    public final static int JSON_TYPE_QUOTED_VALUE = 2;
    public final static int JSON_TYPE_UNQUOTED_VALUE = 3;
    private Hashtable<String, JSONObject> children = null;
    private Vector<JSONObject> items = null;
    private String key;
    private String value = null;
    private int type = JSON_TYPE_OBJECT;

    public JSONObject(String key) {
        //System.out.println("new object:<"+key+">");
        this.key = key;
    }

    public JSONObject(String key, String value, boolean hasQuotes) {
        this.key = key;
        this.setValue(value, hasQuotes);
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isLeaf() {
        return type == JSON_TYPE_QUOTED_VALUE || type == JSON_TYPE_UNQUOTED_VALUE;
    }

    public boolean isCollection() {
        return type == JSON_TYPE_COLLECTION;
    }

    public boolean isObject() {
        return type == JSON_TYPE_OBJECT;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    public Vector<JSONObject> getItems() {
        return this.items;
    }

    public int getItemsSize(){
        if (this.items==null){
            return -1;
        }
        return this.items.size();
    }

    public JSONObject addChild(JSONObject child) {
        type = JSON_TYPE_OBJECT;
        //System.out.println("["+key+"] adding child:<"+child.getKey()+">");
        if (children==null){ //lazy instantiation
            children = new Hashtable();
        }
        return this.children.put(child.getKey(), child);
    }

    private String arrayToString(String[] stringArray) {
        String result = "[";
        for (String item : stringArray) {
            result += item + ",";
        }
        if (stringArray.length > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result + "]";
    }

    public JSONObject getChild(String key) throws JSONPathAccessException {
        String[] path = {key};
        return getChild(path);
    }

    public JSONObject getChild(String[] keySequence) throws JSONPathAccessException {
//        System.out.println("getChild for sequence:"+arrayToString(keySequence));
        if (keySequence.length < 1) {
            return this;
        }
        if (isLeaf()) {
            throw new JSONPathAccessException("Unable to proceed path at leaf:" + getKey() + " next element would have been:" + keySequence[0]);
        }
        if (isCollection()) {
            try {
                int index = Integer.parseInt(keySequence[0]);
                return items.get(index).getChild(Arrays.copyOfRange(keySequence, 1, keySequence.length));
            } catch (NumberFormatException ex) {
                throw new JSONPathAccessException("Unable to proceed path when trying to parse index of collection at collection:" + getKey() + " next element would have been:" + keySequence[0]);
            } catch (IndexOutOfBoundsException ex) {
                throw new JSONPathAccessException("Index out of bounds at collection:" + getKey() + " for index:" + keySequence[0]);
            } catch (NullPointerException ex){
                throw new JSONPathAccessException("Collection:" + getKey() + " was not instantiated!");
            }
        }//else its an object
        if ((children==null) || !children.containsKey(keySequence[0])) {
            throw new JSONPathAccessException("Unable to proceed path from:" + getKey() + " no subobject found with key:" + keySequence[0]);
        }
        return children.get(keySequence[0]).getChild(Arrays.copyOfRange(keySequence, 1, keySequence.length));
    }

    public void addItem(JSONObject item) {
        this.type = JSON_TYPE_COLLECTION;
        //System.out.println("["+key+"] adding item:<"+item.getKey()+">");
        if (items==null){
            items = new Vector(); // lazy initialization
        }
        this.items.add(item);
    }

    public void setValue(String value, boolean hasQuotes) {
        if (hasQuotes) {
            this.type = JSON_TYPE_QUOTED_VALUE;
        } else {
            this.type = JSON_TYPE_UNQUOTED_VALUE;
        }
        //System.out.println("["+key+"] setting value:<"+value+">");
        this.value = value;
    }

    public String toJson() {

        if (isLeaf()) {
            if ((value != null) && (type == JSON_TYPE_QUOTED_VALUE)) {

                return "\"" + value + "\"";

            } else {
                return value;
            }
        } else if (isCollection()) {
            if (items==null){
                return "[]";
            }
            String result = "[";
            for (JSONObject object : items) {
                result += "" + object.toJson() + ",";
            }
            //remove the last comma if we had any entries
            if (!items.isEmpty()) {
                result = result.substring(0, result.length() - 1);
            }
            return result + "]";


        } //else its assumed to be an object
        if (children==null){
            return "{}";
        }
        String result = "{";
        for (Entry<String, JSONObject> child : children.entrySet()) {
            result += "\"" + child.getKey() + "\":" + child.getValue().toJson() + ", ";
        }
        //remove the last comma if we had any entries
        if (!children.isEmpty()) {
            result = result.substring(0, result.length() - 2);
        }
        return result + "}";

    }

    @Override
    public String toString() {
        String json = toJson();
        StringBuilder result = new StringBuilder();


        int bCount = 0;
        boolean quoteFlag = false;
        boolean escapeFlag = false;
        boolean lineForwardFlag = true;

        for (int i = 0; i < json.length(); i++) {
            

            char chr = json.charAt(i);

            quoteFlag = ((!quoteFlag) && (chr == '"'))
                    || (quoteFlag && (chr != '"'))
                    || (escapeFlag && quoteFlag);

            if (!quoteFlag) {
                if (chr == '{' || chr == '[') {
                    bCount++;
                }
                if (chr == '}' || chr == ']') {
                    bCount--;
                }
            }

            if (!quoteFlag && (chr == '{' || chr == '[' || chr == '}' || chr == ']' || chr == ',')) {
                lineForwardFlag = true;

            } else {
                if (lineForwardFlag) {
                    result.append("\n");
                    for (int j = 0; j < bCount; j++) {
                        result.append("\t");
                    }
                }

                lineForwardFlag = false;
            }
            result.append(chr);
            escapeFlag = (chr == '\\');
        }

        return result.toString();
    }

    /**
     * Warning only set the key in case your sure what you're doing
     * - this can spoil the integrity of the JSON-tree -
     * e.g. the key might be used by super-objects to reference to this object
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }
}
