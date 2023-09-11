package ParseNodes;

import java.util.ArrayList;

import Parser.Token;

public class KeyValuePairNode extends ParseNode{

	Token.Key k;
	Token.Value v;
	public KeyValuePairNode(Token.Key k, Token.Value v) {
		super(new ArrayList<>());
		this.k = k;
		this.v = v;
		System.out.println(k.s + " -> " + v.s);
	}
}
