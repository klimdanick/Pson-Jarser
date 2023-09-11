package Parser;

import DataStructure.JsonObject;

public class testClass {
	int x, y, z;
	String t = "test";
	public JsonObject json = new JsonObject();
	
	public testClass() {
		x = (int) (Math.random() * 100);
		y = (int) (Math.random() * 100);
		z = (int) (Math.random() * 100);
		json.set("a", 5);
		json.set("b", 6);
		json.set("c", 7);
	}
}
