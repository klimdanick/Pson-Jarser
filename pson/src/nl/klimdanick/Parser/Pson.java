package nl.klimdanick.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import nl.klimdanick.DataStructure.JsonObject;



public class Pson {
	
    public static void main(String[] args) {

//    	System.out.println("| DEMO PSON |");
    	JsonObject json = Pson.readFromFile("C://users/danick/Downloads/huge_json_example.json");
//        JsonObject json = Pson.readFromFile("C://users/danick/Downloads/message.txt");
    	System.out.println(json);
    }
    
    public static JsonObject fromString(String s) {
    	Tokenizer tk = new Tokenizer(s);
//    	System.out.println(tk.toString());
    	return tk.toJson();
    }

    public static boolean writeToFile(JsonObject obj, String path) {return writeToFile(obj, path, false);}

    public static boolean writeToFile(JsonObject obj, String path, boolean Override) {
        if (!Override && new File(path).exists()) {
            System.err.println("[Pson] File does already exist, file not overwriten. If you want to overwrite the file please use writeToFile(path, true);");
            return false;
        }
        try {
        	
        	Files.write(Paths.get(path), obj.toString().getBytes(StandardCharsets.UTF_8));
        	
        	/*
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.print(obj.toString());
            writer.close();
            */
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (IOException e) {
        	return false;
		}
    }

    public static JsonObject readFromFile(String path) {
        try {
        	String s = Files.readString(Paths.get(path));
//        	System.out.println(s);
            return Pson.fromString(s);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}