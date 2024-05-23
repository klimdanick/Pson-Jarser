package nl.klimdanick.Parser;

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

import nl.klimdanick.DataStructure.*;



public class Pson {
	
    public static void main(String[] args) {

    	System.out.println("| DEMO PSON |");
    	JsonObject json = Pson.readFromFile("res/demo.json");
    	System.out.println(json);
    	
    }
    
    public static JsonObject fromString(String s) {
    	Tokenizer tk = new Tokenizer(s);
    	System.out.println(tk.toString());
    	return tk.toJson();
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