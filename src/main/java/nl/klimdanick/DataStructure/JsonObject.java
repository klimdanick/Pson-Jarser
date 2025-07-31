package nl.klimdanick.DataStructure;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

import nl.klimdanick.Parser.Pson;

public class JsonObject {
    private static final Set<Class<?>> allowedTypes = Set.of(String.class, Integer.class, Double.class, Boolean.class, JsonArray.class, JsonObject.class);
    public ArrayList<KeyValuePair> fields = new ArrayList<>();

    public <K> JsonObject set(String key, K value) {
    	if (value == null) {
    		for(KeyValuePair kvp : fields) if (kvp.key.equals(key)){
                kvp.value = null;
                return this;
            }
            fields.add(new KeyValuePair(key, null));
            return this;
    	}

        if (allowedTypes.contains(value.getClass())) {
            for(KeyValuePair kvp : fields) if (kvp.key.equals(key)){
                kvp.value = value;
                return this;
            }
            fields.add(new KeyValuePair(key, value));
            return this;
        }
        System.err.println("Invalid datatype: "+ value.getClass()+". Item not added to object.");
        return this;
    }

    public Object get(String key){
        for(KeyValuePair kvp : fields){
            if( kvp.key.equals(key)) return kvp.value;
        }
        return null;

    }

    public JsonObject getObject(String key){
        for(KeyValuePair kvp : fields){
            if( kvp.key.equals(key) && kvp.value.getClass() == JsonObject.class) return (JsonObject) kvp.value;
        }
        return null;

    }

    public String getString(String key){
        for(KeyValuePair kvp : fields){
            if( kvp.key.equals(key) && kvp.value.getClass() == String.class) return (String) kvp.value;
        }
        return null;

    }

    public Integer getInt(String key){
        for(KeyValuePair kvp : fields){
            if( kvp.key.equals(key) && kvp.value.getClass() == Integer.class) return (int) kvp.value;
        }
        return null;

    }

    public Double getDouble(String key){
        for(KeyValuePair kvp : fields){
            if( kvp.key.equals(key) && kvp.value.getClass() == Double.class) return (double) kvp.value;
        }
        return null;

    }

    public Boolean getBoolean(String key){
        for(KeyValuePair kvp : fields){
            if( kvp.key.equals(key) && kvp.value.getClass() == Boolean.class) return (boolean) kvp.value;
        }
        return null;
    }

    public JsonArray getArray(String key){
        for(KeyValuePair kvp : fields){
            if( kvp.key.equals(key) && kvp.value.getClass() == JsonArray.class) return (JsonArray) kvp.value;
        }
        return null;
    }

    public String toString() {
        StringBuilder retString = new StringBuilder("{\n");
        for (KeyValuePair kvp : fields)
            retString.append("\t").append(kvp.value == null ? "\"" + kvp.key + "\": null" : kvp.value.getClass() == JsonObject.class ? kvp.toString().replaceAll("\n", "\n\t") : kvp.toString()).append(fields.indexOf(kvp) == fields.size() - 1 ? "\n" : ",\n");


        retString.append("}");
        return retString.toString();
    }
    public boolean writeToFile(String path) {return writeToFile(path, false);}
    public boolean writeToFile(String path, boolean Override) {
        if (!Override && new File(path).exists()) {
            System.err.println("[Pson] File does already exist, file not overwriten. If you want to overwrite the file please use writeToFile(path, true);");
            return false;
        }
        try {

            PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
            writer.println(this.toString());
            writer.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
