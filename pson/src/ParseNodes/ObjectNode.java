package ParseNodes;

import java.util.ArrayList;

import Parser.Token;

public class ObjectNode extends ParseNode{

	public ObjectNode(ArrayList<Token> subTable) {
		super(subTable);
		//for (Token t : subTable) {
		//	System.out.println(t);
		//}
		//System.out.println();
	}

}
