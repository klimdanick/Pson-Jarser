package nl.klimdanick.Parser;

import nl.klimdanick.DataStructure.JsonArray;
import nl.klimdanick.DataStructure.JsonObject;

public class Tokenizer {

	private final Token rootToken;

	public Tokenizer(String input) {
		int firstBrace = input.indexOf('{');
		int firstBracket = input.indexOf('[');
		
		if (firstBrace != -1 && (firstBrace < firstBracket || firstBracket == -1)) {
			this.rootToken = parseObject(input);
		} else {
			this.rootToken = parseArray(input);
		}
	}
	
	private Token.Obj parseObject(String input) {
		StringBuilder strBuf = new StringBuilder();
		char[] charBuf = input.toCharArray();
		int braceCount = 0;
		for (char c : charBuf) {
			if (c == '{') braceCount++;
			if (braceCount > 0) strBuf.append(c);
			if (c == '}') {
				braceCount--;
				if (braceCount == 0) break;
			}
		}

		Token.Obj obj = new Token.Obj();
		obj.parse(strBuf.toString());
		return obj;
	}

	private Token.Arr parseArray(String input) {
		StringBuilder strBuf = new StringBuilder();
		char[] charBuf = input.toCharArray();
		int bracketCount  = 0;
		for (char c : charBuf) {
			if (c == '[') bracketCount++;
			if (bracketCount > 0) strBuf.append(c);
			if (c == ']') {
				bracketCount--;
				if (bracketCount == 0) break;
			}
		}

		Token.Arr arr = new Token.Arr();
		arr.parse(strBuf.toString());
		return arr;
	}

	public JsonObject toJson() {
		return toJson(rootToken);
	}

	public JsonObject toJson(Token token) {
		if (token instanceof Token.Obj objToken) {
			return objectToJson(objToken);
		}
		if (token instanceof Token.Arr arrToken) {
			JsonObject wrapper = new JsonObject();
			JsonArray jsonArr = arrayToJson(arrToken);
			wrapper.set("array", jsonArr);
			return wrapper;
		}
		return null;
	}

	JsonObject objectToJson(Token.Obj objToken) {
		JsonObject jsonObj = new JsonObject();
		for (Token.Key key : objToken.Keys) {
			Object val = key.value.value;
			Class<?> type = key.value.type;

			if (type == Token.Obj.class) {
				jsonObj.set(key.key, toJson((Token.Obj) val));
			} else if (type == Token.Arr.class) {
				jsonObj.set(key.key, toJson((Token.Arr)val).getArray("array"));
			}
			else {
				jsonObj.set(key.key, val);
			}
		}
		return jsonObj;
	}

	JsonArray toJsonArray() {
		if (rootToken instanceof Token.Arr arrToken) {
			return arrayToJson(arrToken);
		} else {
			return null;
		}
	}

	JsonArray arrayToJson(Token.Arr arrToken) {
		JsonArray jsonArr = new JsonArray();
		for (Token.Value value : arrToken.values) {
			Object val = value.value;
			Class<?> type = value.type;

			if (type == Token.Obj.class) {
				jsonArr.add(toJson((Token.Obj) val));
			} else if (type == Token.Arr.class) {
				jsonArr.add(toJson((Token.Arr) val).getArray("array"));
			}
			else {
				jsonArr.add(val);
			}
		}
		return jsonArr;
	}

	public String toString() {
		return rootToken != null ? rootToken.toString() : "null";
	}
}
