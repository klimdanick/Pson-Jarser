package Parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import DataStructure.JsonObject;

public class Tokenizer {
	
	private Token tk;

	public Tokenizer(String s) {
		String strBuf = "";
		char[] charBuf = s.toCharArray();
		int CBcount = 0;
		for (int i = 0; i < charBuf.length; i++) {
			if (charBuf[i] == '{') CBcount++; 
			if (CBcount > 0) strBuf+=charBuf[i];
			if (charBuf[i] == '}') {
				CBcount--;
				if (CBcount == 0) {
					System.out.println(strBuf);
					tk = new Token.Obj();
					tk.parse(strBuf);
					System.out.println(tk.toString());
				}
			}
		}
	}
	
	
	public JsonObject toJson() {
		JsonObject jsonObj = new JsonObject();
		return jsonObj;
	}
	
	public String toString() {
		String str = "";
		return str;
	}
}
