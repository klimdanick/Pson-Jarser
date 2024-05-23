package nl.klimdanick.DataStructure;

public class KeyValuePair <T> {
    public String key;
    public T value;

    public KeyValuePair(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        if (value.getClass() == String.class) return "\""+key+"\""+": \""+value.toString()+"\"";
        if (value.getClass() == JsonArray.class) return "\""+key+"\""+": "+value.toString().replaceAll("\n", "\n\t");
        return "\""+key+"\""+": "+value.toString();
    }
}
