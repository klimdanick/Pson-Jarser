package Parser;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Token {
	public abstract String toString();
	public abstract void parse(String str);
	
	public static class Obj extends Token{
		public ArrayList<Key> Keys = new ArrayList<>();
		public Obj() {}
		public void parse(String str) {
			String strBuf = "";
			char[] charBuf = str.toCharArray();
			int APcount = 0;
			String keyBuf;
			for (int i = 0; i < charBuf.length; i++) {
				if (charBuf[i] == '"') {
					APcount++;
				}
				else if (APcount == 1) {
					strBuf+=charBuf[i];
				}
				if (APcount > 1) {
					keyBuf = new String(strBuf);
					
					// key found | now find end of key-value pair
					int CBcount = 0;
					int SQBcount = 0;
					
					for (; i < charBuf.length; i++) {
						strBuf+=charBuf[i];
						if (charBuf[i] == '{') CBcount++;
						if (charBuf[i] == '}') CBcount--;
						if (charBuf[i] == '[') SQBcount++;
						if (charBuf[i] == ']') SQBcount--;
						if (CBcount == -1 || (CBcount == 0 && SQBcount == 0 && charBuf[i] == ',')) {
							// end of key-value pair found
							Token.Key key = new Token.Key(keyBuf);
							key.parse(strBuf);
							Keys.add(key);
							break;
						}
					}
					
					APcount = 0;
					strBuf = "";
				}
			}
		}
		
		public String toString() {
			String str = "Obj {\n";
			for (Key k : Keys) str+=k.toString()+",\n";
			return str + "}";
		}
	}
	
	public static class Arr extends Token{
		public Value[] values;
		public Arr() {}
		public void parse(String str) {
			ArrayList<Value> valBuf = new ArrayList<>();
			str = str.substring(1);
			
			String strBuf = "";
			char[] charBuf = str.toCharArray();
			int CBcount = 0;
			int SQBcount = 0;
			
			for (int i = 0; i < charBuf.length; i++) {
				if (charBuf[i] == '{') CBcount++;
				if (charBuf[i] == '}') CBcount--;
				if (charBuf[i] == '[') SQBcount++;
				if (charBuf[i] == ']') SQBcount--;
				if (SQBcount == -1 || (SQBcount == 0 && CBcount == 0 && charBuf[i] == ',')) {
					Value val = new Value();
					val.parse(strBuf);
					valBuf.add(val);
					strBuf = "";
					continue;
				}
				strBuf+=charBuf[i];
			}
			values = new Value[valBuf.size()];
			for (int i = 0; i < valBuf.size(); i++) {
				values[i] = valBuf.get(i);
			}
		}
		
		public String toString() {
			String str = "Arr [";
			for (Value v : values) str+=v.toString()+",";
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
			
			value.parse(str);
		}
		
		public String toString() {
			String str = "Key {" + key + "}:"+value.toString();
			return str;
		}
	}
	
	public static class Value <T> extends Token{
		public Object value;
		public Class type;
		public Value() {}
		public void parse(String str) {
			str = str.stripLeading().stripTrailing();
			
			if (str.startsWith("{")) {
				Obj obj = new Obj();
				obj.parse(str);
				setValue(obj);
				type = Obj.class;
			}
			else if (str.startsWith("[")) {
				Arr arr = new Arr();
				arr.parse(str);
				setValue(arr);
				type = Arr.class;
			} 
			else if (str.startsWith("\"")) {
				setValue(str.replace("\"", "").replace("\"", ""));
				type = String.class;
			}
			else if (str.contains(".")) {
				setValue(Double.parseDouble(str));
				type = Double.class;
			}
			else {
				setValue(Integer.parseInt(str));
				type = Integer.class;
			}
		}
		
		private <K> void setValue(K value) {
			this.value = value;
		}
		
		public String toString() {
			String str = "Value {" + value.toString() + "}";
			return str;
		}
	}
}
