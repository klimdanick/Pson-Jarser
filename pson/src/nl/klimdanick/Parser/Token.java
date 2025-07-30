package nl.klimdanick.Parser;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Token {
	public abstract String toString();
	public abstract void parse(String str);

	public static class Obj extends Token{
		public ArrayList<Key> Keys = new ArrayList<>();

		public Obj() {}

		public void parse(String str) {
			StringBuilder strBuf = new StringBuilder();
			char[] charBuf = str.toCharArray();
			int quoteCount = 0;
			String keyBuf;
			for (int i = 0; i < charBuf.length; i++) {
				if (charBuf[i] == '"') {
					quoteCount++;
				}
				else if (quoteCount == 1) {
					strBuf.append(charBuf[i]);
				}
				if (quoteCount > 1) { // key found
					String keyName = strBuf.toString();

					int braceCount = 0;
					int bracketCount = 0;

					for (; i < charBuf.length; i++) {
						strBuf.append(charBuf[i]);
						if (charBuf[i] == '{') braceCount++;
						if (charBuf[i] == '}') braceCount--;
						if (charBuf[i] == '[') bracketCount++;
						if (charBuf[i] == ']') bracketCount--;

						// when end of object or end of key-value pair, parse the key-value pair
						if (braceCount == -1 || (braceCount == 0 && bracketCount == 0 && charBuf[i] == ',')) {
							Token.Key key = new Token.Key(keyName);
							key.parse(strBuf.toString());
							Keys.add(key);
							break;
						}
					}

					quoteCount = 0;
					strBuf.setLength(0);
				}
			}
		}

		public String toString() {
			StringBuilder str = new StringBuilder("Obj {\n");
			for (Key k : Keys) str.append(k.toString()).append(",\n");
			return str + "}";
		}
	}

	public static class Arr extends Token{
		public Value[] values;
		public Arr() {}
		public void parse(String str) {
			ArrayList<Value> valBuf = new ArrayList<>();
			str = str.substring(1);

			StringBuilder strBuf = new StringBuilder();
			char[] charBuf = str.toCharArray();
			int CBcount = 0;
			int SQBcount = 0;

			for (int i = 0; i < charBuf.length; i++) {
				if (charBuf[i] == '{') CBcount++;
				if (charBuf[i] == '}') CBcount--;
				if (charBuf[i] == '[') SQBcount++;
				if (charBuf[i] == ']') SQBcount--;
				if (SQBcount == -1 || (SQBcount == 0 && CBcount == 0 && charBuf[i] == ',')) {
					if (strBuf.isEmpty()) continue;
					Value val = new Value();
					val.parse(strBuf.toString());
					valBuf.add(val);
					strBuf = new StringBuilder();
					continue;
				}
				strBuf.append(charBuf[i]);
			}
			values = new Value[valBuf.size()];
			for (int i = 0; i < valBuf.size(); i++) {
				values[i] = valBuf.get(i);
			}
		}

		public String toString() {
			StringBuilder str = new StringBuilder("Arr [");
			for (Value v : values) str.append(v.toString()).append(",");
			return str + "]";
		}
	}

	public static class Key extends Token{
		public String key;
		public Value value;
		public Key(String key) {this.key = key;}
		public void parse(String str) {
			int firstCol = str.indexOf(':');
			str = str.substring(firstCol+1, str.length()-1).stripLeading();
			value = new Value();
			if (!str.isEmpty())
				value.parse(str);
			else {
				System.err.append("FIX");
			}
		}

		public String toString() {
			StringBuilder str = new StringBuilder("Key {" + key + "}:");
			if (value != null) str.append(value.toString());
			else str.append("null");
			return str.toString();
		}
	}

	public static class Value extends Token{
		public Object value;
		public Class<?> type;
		public Value() {}
		public void parse(String str) {
			str = str.stripLeading().stripTrailing();

			if (str.startsWith("{")) {
				Obj obj = new Obj();
				obj.parse(str);
				value = obj;
				type = Obj.class;
			}
			else if (str.startsWith("[")) {
				Arr arr = new Arr();
				arr.parse(str);
				value = arr;
				type = Arr.class;
			}
			else if (str.startsWith("\"")) {
				value = str.replace("\"", "").replace("\"", "");
				type = String.class;
			}
			else if (str.equals("true")) {
				value = true;
				type = Boolean.class;
			}
			else if (str.equals("false")) {
				value = false;
				type = Boolean.class;
			}
			else if (str.contains(".") || str.contains("e") || str.contains("E")) {
				value = Double.parseDouble(str);
				type = Double.class;
			}
			else if (str.contains("null")) {
				value = null;
				type = null;
			}
			else {
				value = Integer.parseInt(str);
				type = Integer.class;
			}
		}

		public String toString() {
			StringBuilder str = new StringBuilder("Value {");
			if (value != null) str.append(value.toString());
			else str.append("null");
			str.append("}");
			return str.toString();
		}
	}
}
