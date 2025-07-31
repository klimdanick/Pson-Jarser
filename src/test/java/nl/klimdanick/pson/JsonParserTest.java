package nl.klimdanick.pson;


import org.junit.jupiter.api.Test;

import nl.klimdanick.DataStructure.JsonArray;
import nl.klimdanick.DataStructure.JsonObject;
import nl.klimdanick.Parser.Pson;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {

    @Test
	public void noArrayTest() {
    	String jsonString = "{\"test\": 10}";
    	JsonObject jsonObject = Pson.fromString(jsonString);
    	jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");

		assertEquals(jsonString,parsedJsonString);
    }

	@Test
	public void testComplexJson() {
		String jsonString = """
        {
          "level1": {
            "level2": {
              "level3": {
                "level4": {
                  "level5": {
                    "level6": {
                      "level7": {
                        "array": [
                          1,
                          { "key": "value" },
                          [2, 3, { "nested_key": "nested_value" }]
                        ],
                        "boolean": true,
                        "null_value": null
                      }
                    }
                  }
                }
              }
            }
          }
         }
        """;
		JsonObject jsonObject = Pson.fromString(jsonString);

		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");

		assertEquals(jsonString,parsedJsonString);
	}

	@Test
	public void testComplexJson2() {
		String jsonString = """
		{
		  "data": {
			"string": "example",
			"number": 12345,
			"boolean": false,
			"null": null,
			"object": {
			  "nested_string": "nested example",
			  "nested_number": 6789
			},
			"array": [
			  "text",
			  42,
			  true,
			  null,
			  { "inner_object": "inner_value" },
			  [1, 2, 3]
			]
		  }
		}
		""";

		JsonObject jsonObject = Pson.fromString(jsonString);

		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");

		assertEquals(jsonString,parsedJsonString);
	}

	@Test
	public void testSpecialNumbers() {
		String jsonString = """
		{
		  "special_numbers": [
			0,
			-0,
			1e10,
			1e-10,
			-1e10,
			-1e-10,
			0.1e1,
			1.0,
			-1.0,
			1.7976931348623157E308,
			-1.7976931348623157E308,
			5E-324,
			-5E-324
		  ]
		}
		""";
		JsonObject jsonObject = Pson.fromString(jsonString);

		JsonObject expectedJson = new JsonObject().set("special_numbers", new JsonArray().addItem(0).addItem(-0).addItem(1e10).addItem(1e-10).addItem(-1e10).addItem(-1e-10).addItem(0.1e1).addItem(1.0).addItem(-1.0).addItem(1.7976931348623157E308).addItem(-1.7976931348623157E308).addItem(5e-324).addItem(-5e-324));
		String expectedJsonString = expectedJson.toString().replaceAll("\\s", "").toLowerCase();
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "").toLowerCase();

		assertEquals(expectedJsonString,parsedJsonString);
	}

	@Test
	public void testNestedMix() {
		String jsonString = """
		{
			"nested_mix":{"a":[1,{"b":[2,{"c":[3,{"d":[4,{"e":[5,{"f":[6,{"g":[7,{"h":[8,{"i":[9,{"j":[10,{"k":"deep"}]}]}]}]}]}]}]}]}]}]}
		}
		""";

		JsonObject jsonObject = Pson.fromString(jsonString);


		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");

		assertEquals(jsonString,parsedJsonString);
	}

	@Test
	public void testLargeMixedArray() {
		String jsonString = """ 
		{
			"large_mixed_array": [
				null, false, true, 123, -123, 1.23, -1.23, 1.0E20, "string",
				{"key": "value"}, [1, 2, 3], [], {},
			 	1.7976931348623157E308, -1.7976931348623157E308, 4.9E-324, -4.9E-324,\s
		 		{"complex": {"nested": {"array": [{"deep": "value"}, [null, "text"]]}}},
			 	"escaped\\\\nnewline", "\\u2028", "\\u2029"
		   ]
		}
		""";

		JsonObject jsonObject = Pson.fromString(jsonString);

		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");

		assertEquals(jsonString,parsedJsonString);
	}

	@Test
	public void testComplexStructure() {
		String jsonString = """
		{
		  "complex_structure": {
			"a": {
			  "b": {
				"c": [
				  {"d": {"e": {"f": {"g": "\\u2028special\\u2029characters"}}}},
				  {"h": [1, "two", true, false, null, {"i": [2, {"j": "end"}]}]}
				]
			  }
			}
		  }
		}
		""";
		JsonObject jsonObject = Pson.fromString(jsonString);

		jsonString=jsonString.replaceAll("\\s", "");
		String parsedJsonString = jsonObject.toString().replaceAll("\\s", "");

		assertEquals(jsonString, parsedJsonString);
	}


	@Test
	public void testReadJsonFromFile(){
		String filePath = "res/demo.json";
		String inputJson = TestUtils.loadFile(filePath);
		JsonObject jsonObject = Pson.readFromFile(filePath);

		assertNotNull(jsonObject, "The json object should not be null after parsing");

		String formattedInputJson = inputJson.replaceAll("\\s", "");
		String parsedJsonString =  jsonObject.toString().replaceAll("\\s", "");

		assertEquals(formattedInputJson,parsedJsonString,"The json from the file should be the same as the parsed json");
	}


	//invalid jsons
	@Test
	public void testMissingEndBracket(){

		String jsonString = """
				{
				"test": 10,
				
				""";

		IllegalStateException thrown = assertThrows(
				IllegalStateException.class,
				() -> Pson.fromString(jsonString), // code that should throw
				"Expected fromString(String) to throw, but it didn't"
		);
	}
}
