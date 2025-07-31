package nl.klimdanick.Parser;

import nl.klimdanick.DataStructure.JsonArray;
import nl.klimdanick.DataStructure.JsonObject;

public class Tokenizer {

	private final Token rootToken;

	public Tokenizer(String input) {
		int firstBrace = input.indexOf('{');
		int firstBracket = input.indexOf('[');

		if (firstBrace == -1 && firstBracket == -1) {
			throw new IllegalArgumentException("[Tokenizer] Invalid JSON-begin: \"" + input.substring(0, Math.min(10, input.length())) + "\", JSON-Object -> {} or JSON-Array -> [] expected.");
		}
		else if (firstBracket == -1 || (firstBrace != -1 && firstBrace < firstBracket)) {
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

		if (braceCount != 0) {
			throw new IllegalStateException("[Tokenizer] JSON-object not closed properly.");
		}

		try {
			Token.Obj obj = new Token.Obj();
			obj.parse(strBuf.toString());
			return obj;
		} catch (Exception e) {
			throw new RuntimeException("[Tokenizer] Error while parsing JSON-object: " + e.getMessage(), e);
		}
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

		if (bracketCount != 0) {
			throw new IllegalStateException("[Tokenizer] JSON-array not closed properly.");
		}

		try {
			Token.Arr arr = new Token.Arr();
			arr.parse(strBuf.toString());
			return arr;
		} catch (Exception e) {
			throw new RuntimeException("[Tokenizer] Error while parsing JSON-array: " + e.getMessage(), e);
		}
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

			try {
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
			} catch (Exception e) {
				System.err.println("[Tokenizer] Error at key '" + key.key + "': " + e.getMessage());
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

			try {
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
			} catch (Exception e) {
				System.err.println("[Tokenizer] Error at array-element: " + e.getMessage());
			}
		}
		return jsonArr;
	}

	public String toString() {
		return rootToken != null ? rootToken.toString() : "null";
	}
}
