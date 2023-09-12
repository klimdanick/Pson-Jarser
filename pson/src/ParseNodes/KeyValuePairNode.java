package ParseNodes;

import java.util.ArrayList;

import DataStructure.JsonArray;
import Parser.Token;

public class KeyValuePairNode extends ParseNode{

	Token.Key k;
	Token.Value v;
	
	public String key;
    public Object value;
	
	public KeyValuePairNode(Token.Key k, Token.Value v) {
		super(new ArrayList<>());
		this.k = k;
		if (v.s.charAt(v.s.length()-1) == '\n') v.s = v.s.substring(0, v.s.length()-1);
		this.v = v;
		if (v.s.contains("\"")) value = v.s.substring(1, v.s.length()-1);
		else if (v.s.contains(".")) value = Double.parseDouble(v.s);
		else if (v.s.equals("true")) value = true;
		else if (v.s.equals("false")) value = false;
		else value = Integer.parseInt(v.s);
		key = k.s;
	}
	
	//public String toString() {
	//	return "\"" + k.s + "\": " + v.s;
	//}

    public KeyValuePairNode(String key, Object value) {
    	super(new ArrayList<>());
        this.key = key;
        this.value = value;
    }

    public String toString() {
        if (value.getClass() == String.class) return "\""+key+"\""+": \""+value.toString()+"\"";
        if (value.getClass() == JsonArray.class) return "\""+key+"\""+": "+value.toString().replaceAll("\n", "\n\t");
        return "\""+key+"\""+": "+value.toString();
    }
}
