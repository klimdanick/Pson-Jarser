package nl.klimdanick.Parser;

import nl.klimdanick.DataStructure.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Pson {
	
    public static void main(String[] args) {
    	JsonObject json = Pson.readFromFile("res/demo.json");

        if (json != null) {
            System.out.println("JSON loaded!");
            System.out.println(json);
        } else {
            System.err.println("Error while loading JSON-file");
        }
    }
    
    public static JsonObject fromString(String s) {
    	Tokenizer tk = new Tokenizer(s);
    	return tk.toJson();
    }

    public static boolean writeToFile(JsonObject obj, String path) {
        return writeToFile(obj, path, false);
    }

    public static boolean writeToFile(JsonObject obj, String path, boolean overwrite) {
        Path filePath = Path.of(path);

        if (!overwrite && Files.exists(filePath)) {
            System.err.println("[Pson] File already exists. Use overwrite=true to overwrite the file.");
            return false;
        }

        try {
            Files.writeString(filePath, obj.toString(), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            System.err.println("[Pson] Error while writing file: " + e.getMessage());
            return false;
        }
    }

    public static JsonObject readFromFile(String path) {
        Path filePath = Path.of(path);

        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            return fromString(content);
        } catch (IOException e) {
            System.err.println("[Pson] Error while reading file: " + e.getMessage());
            return null;
        }
    }
}