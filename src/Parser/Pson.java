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



public class Pson {
	
    public static void main(String[] args) {

    	System.out.println("| DEMO PSON |");
    	
    	JsonObject json = readFromFile("studentData.json");
    	JsonArray cijfers = json.getArray("studenten").getObject(1).getArray("behaalde_cijfers");
    	for (int i = 0; i < cijfers.size(); i++) {
    		JsonObject obj = cijfers.getObject(i);
    		System.out.print(obj.get("vakcode"));
    		System.out.print(": ");
    		System.out.println(obj.get("cijfer"));
    	}
    }

    public static JsonObject fromString(String s){
        JsonObject ret = new JsonObject();
        //System.out.println(s);
        s = s.replaceAll("\n", "");
        s = s.replaceAll("\t", "");
        //System.out.println(s);
        //System.out.println("stom");
        s = s.replaceFirst("\\{", "");
        int g = s.lastIndexOf("}");
        s = s.substring(0, g);
        
        //System.out.println(s);
        String[] fields = splitIgnore(s, ',');
        for (String f : fields) {
        	//System.out.println(f);
            String[] kvp = splitIgnore(f,':');
            kvp[0] = kvp[0].replaceAll("\"", "");
            if (kvp[1].startsWith(" ")) kvp[1]=kvp[1].replaceFirst(" ", "");
            ret.set(kvp[0].replaceAll("\"", ""), f(kvp[1]).apply(kvp[1]));
        }

        return ret;
    }

    public static Function<String, Object> f(String s) {
        if (s.startsWith("\u007b")) return Pson::parseObject;
        else if (s.startsWith("\u005b")) return Pson::parseArray;
        else if (s.contains("\"")) return Pson::parseString;
        else if (s.toLowerCase(Locale.ROOT).contains("true")) return (S) -> true;
        else if (s.toLowerCase(Locale.ROOT).contains("false")) return (S) -> false;
        else if (s.contains(".")) return Double::parseDouble;
        else if (s.length() > 0) return Pson::parseInt;
        else return (S) -> false;
    }

    private static JsonObject parseObject(String s) {
        if (s.startsWith(" ")) s = s.replaceFirst(" ", "");
        return fromString(s);
    }

    public static String parseString(String s) {
        if (s.startsWith(" ")) s = s.substring(1, s.length());
        return s.replaceAll("\"", "");
    }

    public static Integer parseInt(String s) {
        int value = Integer.parseInt(s.replaceAll(" ", ""));
        if ((value+"").equals( s.replaceAll(" ", "")))
            return value;
        else return null;
    }

    public static JsonArray parseArray(String s) {
        JsonArray a = new JsonArray();
        s = s.replaceFirst("\\[", "");
        s = s.substring(0, s.length()-1);
        //System.out.println(s);
        String[] fields2 = splitIgnore(s, ',');

        for (String x : fields2) {
            if (x.startsWith(" ")) x=x.replaceFirst(" ", "");
            a.add(f(x).apply(x));
        }
        return a;
    }
    
public static String[] splitIgnore(String s, char e) {
        ArrayList<String> fields = new ArrayList<>();
        //System.out.println("begin: " + s);
        for (int i = 0; s.length() > 0; i++) {
        	//System.out.println("pre: " + s);
            if (s.charAt(i) == '[' || s.charAt(i) == '{') {
                //System.out.println(s);
                int found = 1;
                //System.out.println(i + " " + s.charAt(i) + " " + found);
                while (found > 0 && i < s.length()-1) {


                    i++;
                    //System.out.println(s.charAt(i));
                    if (s.charAt(i) == '[' || s.charAt(i) == '{') {
                        found++;
                        //System.out.println(i + " " + s.charAt(i) + " " + found);
                    }
                    if (s.charAt(i) == ']' || s.charAt(i) == '}') {
                        found--;
                       // System.out.println(i + " " + s.charAt(i) + " " + found);
                    }

                }
            }
            if (i == s.length()-1) {
                fields.add(s);
                //System.out.println("field: " + s);
                s = "";
                break;
            }
             else if (s.charAt(i) == e) {
                fields.add(s.substring(0, i));
                s = s.substring(i+1);
                i = -1;
             }

        }
        String[] fields2 = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) fields2[i] = fields.get(i);
        return fields2;         //s.split(",");
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