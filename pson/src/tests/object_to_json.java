package tests;


import org.junit.Test;

import DataStructure.JsonArray;
import DataStructure.JsonObject;
import Parser.Pson;

public class object_to_json {

	@Test
	public void simpleObjectToJson() {
		System.out.println("\n-------------------------------------\n");
		JsonObject json = new JsonObject().set("name", "klimdanick").set("github", "https://github.com/klimdanick").set("repos", 4);
		String jsonString = "{\n\t\"name\": \"klimdanick\",\n\t\"github\": \"https://github.com/klimdanick\",\n\t\"repos\": 4\n}";
		System.out.println(json.toString());
		System.out.println(jsonString);
		System.out.println("\n-------------------------------------\n");
		assert(json.toString().equals(jsonString));
	}
	
	@Test
	public void jsonToSimpleObject() {
		System.out.println("\n-------------------------------------\n");
		JsonObject json = new JsonObject().set("name", "klimdanick").set("github", "https://github.com/klimdanick").set("repos", 4);
		String str = json.toString();
		System.out.println(str);
		System.out.println("\n++++\n");
		JsonObject json2 = Pson.fromString(str);
		System.out.println("\n-------------------------------------\n");
		assert(json2.equals(json));
	}
	
	@Test
	public void jsonToComplexObject() {
		System.out.println("\n-------------------------------------\n");
		JsonObject json = new JsonObject().set("user", new JsonObject().set("name", "danick imholz").set("nickname", "klimdanick")).set("github", "https://github.com/klimdanick").set("repos", 4);
		String str = json.toString();
		System.out.println(str);
		System.out.println("\n++++\n");
		JsonObject json2 = Pson.fromString(str);
		System.out.println("\n-------------------------------------\n");
		assert(json2.equals(json));
	}
	
	@Test
	public void jsonToSimpleObjectArray() {
		System.out.println("\n-------------------------------------\n");
		JsonObject json = new JsonObject().set("name", new JsonArray().addItem("danick Imholz").addItem("klimdanick")).set("github", "https://github.com/klimdanick").set("repos", 4);
		String str = json.toString();
		System.out.println(str);
		System.out.println("\n++++\n");
		JsonObject json2 = Pson.fromString(str);
		System.out.println("\n-------------------------------------\n");
		assert(json2.equals(json));
	}
	
	@Test
	public void jsonStringToJsonObject() {
		System.out.println("\n-------------------------------------\n");
		String str = "{\"studenten\": [{\"studiejaar\": \"2\", \"behaalde_cijfers\": [{\"vakcode\": \"INF-UI\", \"cijfer\": 5.0}], \"vakkenpakket\": [{\"vakcode\": \"INF-UI\", \"naam\": \"User Interface Design\"}]}]}";
		System.out.println(str);
		System.out.println("\n++++\n");
		JsonObject json2 = Pson.fromString(str);
		System.out.println("\n-------------------------------------\n");
		assert(true);
	}

	
	//{"studenten": [{"studiejaar": "2", "behaalde_cijfers": [{"vakcode": "INF-UI", "cijfer": 5.0}], "vakkenpakket": [{"vakcode": "INF-UI", "naam": "User Interface Design"}]}]}
}
