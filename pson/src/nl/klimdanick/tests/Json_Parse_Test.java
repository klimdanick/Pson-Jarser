package tests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import DataStructure.JsonArray;
import DataStructure.JsonObject;
import Parser.Pson;

public class Json_Parse_Test {
	
	@Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("Test failed: " + description.getMethodName());
            System.out.println("Exception message: " + e.getMessage());
            // You can print additional information or perform actions here
        }
    };

	@Test
	public void testComplexJson() {
		String jsonString = "{\r\n"
		+ "  \"level1\": {\r\n"
		+ "    \"level2\": {\r\n"
		+ "      \"level3\": {\r\n"
		+ "        \"level4\": {\r\n"
		+ "          \"level5\": {\r\n"
		+ "            \"level6\": {\r\n"
		+ "              \"level7\": {\r\n"
		+ "                \"array\": [1, {\"key\": \"value\"}, [2, 3, {\"nested_key\": \"nested_value\"}]],\r\n"
		+ "                \"boolean\": true,\r\n"
		+ "                \"null_value\": null\r\n"
		+ "              }\r\n"
		+ "            }\r\n"
		+ "          }\r\n"
		+ "        }\r\n"
		+ "      }\r\n"
		+ "    }\r\n"
		+ "  }\r\n"
		+ "}";
		JsonObject jsonObject = Pson.fromString(jsonString);
		
		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");
		assert(jsonString.equals(parsedJsonString));
	}
	
	@Test
	public void testComplexJson2() {
		String jsonString = "{\r\n"
				+ "  \"data\": {\r\n"
				+ "    \"string\": \"example\",\r\n"
				+ "    \"number\": 12345,\r\n"
				+ "    \"boolean\": false,\r\n"
				+ "    \"null\": null,\r\n"
				+ "    \"object\": {\r\n"
				+ "      \"nested_string\": \"nested example\",\r\n"
				+ "      \"nested_number\": 6789\r\n"
				+ "    },\r\n"
				+ "    \"array\": [\r\n"
				+ "      \"text\",\r\n"
				+ "      42,\r\n"
				+ "      true,\r\n"
				+ "      null,\r\n"
				+ "      {\"inner_object\": \"inner_value\"},\r\n"
				+ "      [1, 2, 3]\r\n"
				+ "    ]\r\n"
				+ "  }\r\n"
				+ "}";
		JsonObject jsonObject = Pson.fromString(jsonString);
		
		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");
		assert(jsonString.equals(parsedJsonString));
	}
	
	@Test
	public void testSpecialNumbers() {
		String jsonString = "{\r\n"
				+ "  \"special_numbers\": [0, -0, 1e10, 1e-10, -1e10, -1e-10, 0.1e1, 1.0, -1.0, 1.7976931348623157E308, -1.7976931348623157E308, 5E-324, -5E-324]\r\n"
				+ "}";
		JsonObject jsonObject = Pson.fromString(jsonString);
		
		
		JsonObject expectedJson = new JsonObject().set("special_numbers", new JsonArray().addItem(0).addItem(-0).addItem(1e10).addItem(1e-10).addItem(-1e10).addItem(-1e-10).addItem(0.1e1).addItem(1.0).addItem(-1.0).addItem(1.7976931348623157E308).addItem(-1.7976931348623157E308).addItem(5e-324).addItem(-5e-324));
		String expectedJsonString = expectedJson.toString().replaceAll("\\s", "").toLowerCase();
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "").toLowerCase();
		assert(expectedJsonString.equals(parsedJsonString));
	}
	
	@Test
	public void testNestedMix() {
		String jsonString = "{\r\n"
				+ "  \"nested_mix\": {\r\n"
				+ "    \"a\": [1, {\"b\": [2, {\"c\": [3, {\"d\": [4, {\"e\": [5, {\"f\": [6, {\"g\": [7, {\"h\": [8, {\"i\": [9, {\"j\": [10, {\"k\": \"deep\"}]}]}]}]}]}]}]}]}]}]}\r\n"
				+ "  }";
		JsonObject jsonObject = Pson.fromString(jsonString);
		
		
		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");
		assert(jsonString.equals(parsedJsonString));
	}
	
	@Test
	public void test_Large_mixed_array() {
		String jsonString = "{\r\n"
				+ "  \"large_mixed_array\": [\r\n"
				+ "    null, false, true, 123, -123, 1.23, -1.23, 1.0E20, \"string\", \r\n"
				+ "    {\"key\": \"value\"}, [1, 2, 3], [], {},\r\n"
				+ "    1.7976931348623157E308, -1.7976931348623157E308, 4.9E-324, -4.9E-324,\r\n"
				+ "    {\"complex\": {\"nested\": {\"array\": [{\"deep\": \"value\"}, [null, \"text\"]]}}},\r\n"
				+ "    \"escaped\\\\nnewline\", \"\\u2028\", \"\\u2029\"\r\n"
				+ "  ]\r\n"
				+ "}";
		JsonObject jsonObject = Pson.fromString(jsonString);
		
		
		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");
		assert(jsonString.equals(parsedJsonString));
	}
	
	@Test
	public void test_complex_structure() {
		String jsonString = "{\r\n"
				+ "  \"complex_structure\": {\r\n"
				+ "    \"a\": {\r\n"
				+ "      \"b\": {\r\n"
				+ "        \"c\": [\r\n"
				+ "          {\"d\": {\"e\": {\"f\": {\"g\": \"\\u2028special\\u2029characters\"}}}},\r\n"
				+ "          {\"h\": [1, \"two\", true, false, null, {\"i\": [2, {\"j\": \"end\"}]}]}\r\n"
				+ "        ]\r\n"
				+ "      }\r\n"
				+ "    }\r\n"
				+ "  }\r\n"
				+ "}";
		JsonObject jsonObject = Pson.fromString(jsonString);
		
		
		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");
		assert(jsonString.equals(parsedJsonString));
	}

}
