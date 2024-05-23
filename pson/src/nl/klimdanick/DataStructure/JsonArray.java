package nl.klimdanick.DataStructure;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonArray extends ArrayList{

	private static Class[] allowedTypes = {String.class, Integer.class, Double.class, Boolean.class, JsonArray.class, JsonObject.class};
	
    public JsonArray addItem(Object o) {
        super.add(o);
        return this;
    }

    public String toString() {
        String s = "[";
        for (int i = 0; i < this.size(); i++) {
            Object o = this.get(i);
            if (o == null) {
            	s+="null";
            	if (i<this.size()-1) s+=", ";
            	continue;
            }
            else if (o.getClass() == allowedTypes[5]) s += "\n\t"+o.toString().replaceAll("\n", "\n\t");
            else if (o.getClass() == allowedTypes[0]) s += "\"" + o.toString() + "\"";
            else s+=o.toString();
            if (i<this.size()-1) s+=", ";
            if (o.getClass() == allowedTypes[5]) s += "\n\t";
        }
        return s+"]";
    }

    public JsonObject getObject(int i) {
        return (JsonObject) this.get(i);
    }

    public String getString(int i) {
        return (String) this.get(i);
    }

    public int getInt(int i) {
        return (int) this.get(i);
    }

    public double getDouble(int i) {
        return (double) this.get(i);
    }

    public JsonArray getArray(int i) {
        return (JsonArray) this.get(i);
    }

    public boolean getBoolean(int i) {
        return (boolean) this.get(i);
    }

    public <T> T get(int i, Type T){
        return (T) get(i);
    }
}
