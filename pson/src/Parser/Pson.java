package Parser;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Function;

import DataStructure.*;
import ParseNodes.ParseNode;



public class Pson {
	
    public static void main(String[] args) {

    	System.out.println("| DEMO PSON |");
    	String s = "{\n"
    			+ "	\"dom\": \"tijmen speekenbrink\",\n"
    			+ "	\"slim\": \"danick imholz\",\n"
    			+ "	\"test\": [{}, {}, {}]\n"
    			+ "}";
    	System.out.println(s+"\n");
    	fromString(s);
    	
    }
    
    public static JsonObject fromString(String s) {
    	Tokenizer tk = new Tokenizer(s);
    	ParseNode tree = new ParseNode(tk.tokens);
    	System.out.println(tree.toJSON());
    	return null;
    }
    
    
    private static Class[] allowedTypes = {String.class, Integer.class, Double.class, Boolean.class, JsonObject.class, JsonArray.class};
    public static JsonObject fromObject(Object o) {return fromObject(o, 2);}
    public static JsonObject fromObject(Object o, int levels) {
        if (levels == 0) {
            return new JsonObject();
        }
        JsonObject json = new JsonObject();
        Field[] superFields = o.getClass().getSuperclass().getFields();
        Field[] fields = o.getClass().getDeclaredFields();
        ArrayList<Field> allfields = new ArrayList<>();
        for (Field f : fields) allfields.add(f);
        for (Field f : superFields) allfields.add(f);
        for (Field f : allfields) {
            if (Modifier.isStatic(f.getModifiers())) continue;
            try {
                f.setAccessible(true);
                String name = f.getName();
                Object value = f.get(o);
                if (value == null) continue;
                boolean foundType = false;
                for (Class t : allowedTypes) {
                    if (value != null && t == value.getClass()) {

                        /*--------*\
                        |valid type|
                        \*--------*/

                        json.set(name, value);
                        foundType = true;
                    }
                }

                /*----------*\
                |invalid type|
                \*----------*/

                if (!foundType) {
                    JsonObject jsonValue;
                    if (value.getClass() == Color.class) {
                        int hashCode = value.hashCode();
                        String HexString = Integer.toHexString(hashCode);
                        json.set(name, "#"+HexString);
                        continue;
                    }
                    if (value.getClass().isArray() && value.getClass().getComponentType().isPrimitive()) {
                        JsonArray a = new JsonArray();
                        if (value instanceof int[]) for (int i : (int[]) value) a.addItem(i);
                        if (value instanceof double[]) for (double i : (double[]) value) a.addItem(i);
                        if (value instanceof float[]) for (float i : (float[]) value) a.addItem(i);
                        if (value instanceof boolean[]) for (boolean i : (boolean[]) value) a.addItem(i);
                        if (value instanceof char[]) for (char i : (char[]) value) a.addItem(i);
                        if (value instanceof byte[]) for (byte i : (byte[]) value) a.addItem(i);
                        if (value instanceof short[]) for (short i : (short[]) value) a.addItem(i);
                        if (value instanceof long[]) for (long i : (long[]) value) a.addItem(i);
                        json.set(name, a);
                        continue;
                    }
                    if (value instanceof Object[]) {
                        Object[] objectArr = (Object[]) value;
                        JsonArray a = new JsonArray();
                        for (Object obj : objectArr) {
                            a.addItem(obj);
                        }
                        json.set(name, a);
                        continue;
                    }
                    jsonValue = fromObject(value, levels-1);
                    if (jsonValue.fields.size() < 1) continue;
                    else json.set(name, jsonValue);
                }
            } catch (java.lang.reflect.InaccessibleObjectException e) {
                continue;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return json;
    }

    public static boolean writeToFile(JsonObject obj, String path) {return writeToFile(obj, path, false);}

    public static boolean writeToFile(JsonObject obj, String path, boolean Override) {
        if (!Override && new File(path).exists()) {
            System.err.println("[Pson] File does already exist, file not overwriten. If you want to overwrite the file please use writeToFile(path, true);");
            return false;
        }
        try {

            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.print(obj.toString());
            writer.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static JsonObject readFromFile(String path) {
        try {
        	String s = Files.readString(Paths.get(path));
            return Pson.fromString(s);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}