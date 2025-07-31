package nl.klimdanick.DataStructure;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

public class JsonArray extends ArrayList{

    private static final Set<Class<?>> allowedTypes = Set.of(String.class, Integer.class, Double.class, Boolean.class, JsonArray.class, JsonObject.class);
	
    public JsonArray addItem(Object o) {
    	if (o == null) {
    		super.add(null);
    		return this;
    	}
    	boolean legal = allowedTypes.contains(o.getClass());

    	if (legal)
    		super.add(o);

        return this;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < this.size(); i++) {
            Object o = this.get(i);
            if (o == null) {
            	s.append("null");
            	if (i<this.size()-1) s.append(", ");
            	continue;
            }
            else if (o.getClass() == JsonObject.class) s.append("\n\t").append(o.toString().replaceAll("\n", "\n\t"));
            else if (o.getClass() == String.class) s.append("\"").append(o.toString()).append("\"");
            else s.append(o.toString());
            if (i<this.size()-1) s.append(", ");
            if (o.getClass() == JsonObject.class) s.append("\n\t");
        }
        return s.append("]").toString();
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
