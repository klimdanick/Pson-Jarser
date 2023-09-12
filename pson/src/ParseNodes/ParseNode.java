package ParseNodes;

import java.util.ArrayList;

import Parser.Token;
import Parser.Token.Cb;

public class ParseNode {
	ArrayList<Token> tokenTable;
	ArrayList<ParseNode> children = new ArrayList<>();
	
	public ParseNode(ArrayList<Token> tokenTable) {
		this.tokenTable = tokenTable;
		build();
	}
	
	public void build() {
		while(tokenTable.size() > 0) {
			if (tokenTable.get(0) instanceof Token.Cb) {
				ArrayList<Token> subTable = getSubTable(Token.Cb.class, Token.BCb.class, getSubTable(1, tokenTable.size(), tokenTable));
				this.children.add(new ObjectNode(subTable));
				//System.out.println(tokenTable.size());
				tokenTable = getSubTable(subTable.size(), tokenTable.size(), tokenTable);
			} 
			if (tokenTable.get(0) instanceof Token.Key && tokenTable.get(1) instanceof Token.Col && tokenTable.get(2) instanceof Token.Value) {
				ArrayList<Token> subTable = new ArrayList<>();
				subTable.add(tokenTable.get(0));
				subTable.add(tokenTable.get(1));
				subTable.add(tokenTable.get(2));
				this.children.add(new KeyValuePairNode((Token.Key) tokenTable.get(0), (Token.Value) tokenTable.get(2)));
				tokenTable = getSubTable(subTable.size(), tokenTable.size(), tokenTable);
			}
			tokenTable = getSubTable(1, tokenTable.size(), tokenTable);
		}
	}
	
	public ArrayList<Token> getSubTable(Class tk, Class ctk, ArrayList<Token> tab) {
		ArrayList<Token> sub = new ArrayList<>();
		int tkCount = 1;
		for (int i = 0; i < tab.size() && tkCount != 0; i++) {
			Token t = tab.get(i);
			if (t.getClass() == tk) tkCount++;
			if (t.getClass() == ctk) tkCount--;
			if (tkCount != 0) {
				sub.add(t);
				//System.out.println("tk: " + t);
			}
		}
		//System.out.println();
		return sub;
	}
	
	public ArrayList<Token> getSubTable(int a, int b, ArrayList<Token> tab) {
		ArrayList<Token> sub = new ArrayList<>();
		for (int i = a; i < b; i++) {
			Token t = tab.get(i);
			sub.add(t);
		}
		return sub;
	}
	
	public String toString() {
		return "{" + toJSON() + "}";
	}
	
	public String toJSON() {
		String s = "";
		for (int i = 0; i < children.size()-1; i++) s += children.get(i).toString() + ",";
		return s + children.get(children.size()-1).toString();
	}
}
