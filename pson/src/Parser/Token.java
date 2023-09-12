package Parser;

public abstract class Token {
	public String toString() {return this.getClass().toString();}
	public abstract boolean identify(String s);
	
	public static class Col extends Token{

		public Col(String s) {
		}
		public Col() {
		}
		
		public boolean identify(String s) {
			return s.equals(":");
		}
		
		//public String toString() {return ":";}
		
	}
	
	public static class Cb extends Token{

		public Cb(String s) {
		}
		public Cb() {
		}
		
		public boolean identify(String s) {
			return s.equals("{");
		}
		
		//public String toString() {return "{";}
		
	}
	
	public static class BCb extends Token{

		public BCb(String s) {
		}
		public BCb() {
		}
		
		public boolean identify(String s) {
			return s.equals("}");
		}
		
		//public String toString() {return "}";}
		
	}
	
	public static class Brac extends Token{

		public Brac(String s) {
		}
		public Brac() {
		}
		
		public boolean identify(String s) {
			return s.equals("[");
		}
		
		//public String toString() {return "[";}
		
	}
	
	public static class BBrac extends Token{

		public BBrac(String s) {
		}
		public BBrac() {
		}
		
		public boolean identify(String s) {
			return s.equals("]");
		}
		
		//public String toString() {return "]";}
		
	}
	
	public static class Com extends Token{

		public Com(String s) {
		}
		public Com() {
		}
		
		public boolean identify(String s) {
			return s.equals(",");
		}
		
		//public String toString() {return ",";}
	}
	
	public static class StringT extends Token{

		public String s;
		public StringT() {}
		public StringT(String s) {
			this.s = s.substring(1, s.length()-1);
		}
		
		public boolean identify(String s) {
			return s.startsWith("\"") && s.endsWith("\"") && s.length() > 1;
		}
		
		public String toString() {return s;}
		
	}
	
	public static class Key extends Token{

		public String s;
		public Key() {}
		public Key(String s) {
			this.s = s;
		}
		
		//public String toString() {return s;}
		@Override
		public boolean identify(String s) {
			return true;
		}
		
	}

	public static class Value extends Token{

		public String s;
		public Value() {}
		public Value(String s) {
			this.s = s;
		}
		
		//public String toString() {return s;}
		@Override
		public boolean identify(String s) {
			boolean isString = s.startsWith("\"") && s.charAt(s.length()-2)=='\"' && s.length() > 1;
			boolean isDouble = isDouble(s);
			boolean isInt = isInt(s);
			return isString;
		}
		
		private boolean isDouble(String s) {
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (!(Character.isDigit(c) || c == '.') && i != s.length()-1) return false;
				if (!(c == ',' || c == '}' || c == ']') && i == s.length()-1) return false;
			}
			return true;
		}
		
		private boolean isInt(String s) {
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (!Character.isDigit(c) && i != s.length()-1) return false;
				if (!(c == ',' || c == '}' || c == ']') && i == s.length()-1) return false;
			}
			return true;
		}
		
	}
}
