package Parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Parser.Token.Value;

public class Tokenizer {
	
	private String buffer = new String();
	private static Token[] tokensAvailable = {new Token.Col(), new Token.Cb(), new Token.BCb(), new Token.Brac(), new Token.BBrac(), new Token.Com(), new Token.StringT()};
	public ArrayList<Token> tokens = new ArrayList<>();
	private String Whitespace = " \t\r\n";

	public Tokenizer(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Whitespace.contains(s.charAt(i) + "") || buffer.length() > 0) buffer += s.charAt(i);
			//System.out.println(buffer);
			for (Token t : tokensAvailable) {
				if (t.identify(buffer)) {
					try {
						tokens.add(t.getClass().getConstructor(String.class).newInstance(buffer));
						buffer = new String();
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					if (t instanceof Token.Col) {
						if (tokens.get(tokens.size()-2) instanceof Token.StringT) {
							Token sT = tokens.get(tokens.size()-2);
							tokens.remove(sT);
							tokens.add(tokens.size()-1, new Token.Key(sT.toString()));
						}
						for (i++; i < s.length() && !(new Token.Com().identify(s.charAt(i)+"") || new Token.BCb().identify(s.charAt(i)+"")); i++) {
							if (!Whitespace.contains(s.charAt(i) + "") || buffer.length() > 0) buffer += s.charAt(i);
							if (new Token.Brac().identify(buffer)) {
								tokens.add(new Token.Brac());
								buffer = new String();
							}
						}
						tokens.add(new Token.Value(buffer));
						buffer = new String();
						i--;
					}
				}
			}
		}
		for (int i = 0; i < tokens.size(); i++) if (tokens.get(i) instanceof Token.StringT) {
			Token OldToken = tokens.get(i);
			tokens.remove(i);
			tokens.add(i, new Value(OldToken.toString()));
		}
		for (Token t : tokens) System.out.println(t);
		System.out.println();
	}

}
