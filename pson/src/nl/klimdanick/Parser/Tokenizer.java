package nl.klimdanick.Parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import nl.klimdanick.DataStructure.JsonArray;
import nl.klimdanick.DataStructure.JsonObject;

public class Tokenizer {
	
	private Token tk;

	public Tokenizer(String s) {
		
		int firstCB = s.indexOf('{');
		int firstSQB = s.indexOf('[');
		
		if (firstCB != -1 && (firstCB < firstSQB || firstSQB == -1)) {
			StringBuilder strBuf = new StringBuilder();
			char[] charBuf = s.toCharArray();
			int CBcount = 0;
			for (int i = 0; i < charBuf.length; i++) {
				if (charBuf[i] == '{') CBcount++;
				if (CBcount > 0) strBuf.append(charBuf[i]);
				if (charBuf[i] == '}') {
					CBcount--;
					if (CBcount == 0) {
						tk = new Token.Obj();
						tk.parse(strBuf.toString());
					}
				}
			}
		} else {
			String strBuf = "";
			char[] charBuf = s.toCharArray();
			int SQBcount = 0;
			for (int i = 0; i < charBuf.length; i++) {
				if (charBuf[i] == '[') SQBcount++; 
				if (SQBcount > 0) strBuf+=charBuf[i];
				if (charBuf[i] == ']') {
					SQBcount--;
					if (SQBcount == 0) {
						tk = new Token.Arr();
						tk.parse(strBuf);
					}
				}
			}
		}
		
//		System.out.println("tk: " + tk);
	}
	
	
	public JsonObject toJson(Token tok) {
		if (tok instanceof Token.Obj) {
			JsonObject jsonObj = new JsonObject();
			for (Token.Key k : ((Token.Obj) tok).Keys) {
				if (k.value.type == Token.Obj.class) {
					jsonObj.set(k.key, toJson((Token.Obj)k.value.value));
				}
				else if (k.value.type == Token.Arr.class) {
					jsonObj.set(k.key, toJson((Token.Arr)k.value.value).getArray("array"));
				}
				else {
					jsonObj.set(k.key, k.value.value);
				}
			}
			return jsonObj;
		}
		if (tok instanceof Token.Arr) {
			JsonObject jsonObj = new JsonObject();
			JsonArray jsonArr = new JsonArray();
			Token.Arr arr = (Token.Arr) tok;
			for (Token.Value v : arr.values) {
				if (v.type == Token.Obj.class) {
					jsonArr.add(toJson((Token.Obj)v.value));
				}
				else if (v.type == Token.Arr.class) {
					jsonArr.add(toJson((Token.Arr)v.value).getArray("array"));
				}
				else {
					jsonArr.add(v.value);
				}
			}
			
			jsonObj.set("array", jsonArr);
			return jsonObj;
		}
		return null;
	}
	
	public JsonObject toJson() {
		return toJson(tk);
	}
	
	public String toString() {
		String str = "";
		return str;
	}
}
