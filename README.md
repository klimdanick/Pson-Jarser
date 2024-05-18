# Json Parser

Json is a verry powerfull data format, and it is used in combination with a lot of programming languages. However, in java it has always been a little funky. Many json parsers have been made for java, but all of them are more complicated then they have to be, so therefor **Pson Jarser** has been made. It is made to be easy for every programmer and lets you write clean and good to read code.

## Table of contents



## Features

* json string parsing: usefull in e.g. an HTTP API to handle json request body's
* json file parsing: usefull for reading config files or loading complex data structures
* jsonObject to json string conversion: Create an json object in your code and convert it to valid json format to store the data or send the data via an http response.

## Installation

To include this library in your project, follow these steps:

> 1: download the latest release from the [Release](https://github.com/klimdanick/Pson-Jarser/releases) page

> 2: add the jar file to your classpath

Now Pson jarser is ready to use

## Usage

Here are 3 examples of how to use the library:

**Example 1: making a json object**:
```java
import DataStructure.JsonArray;
import DataStructure.JsonObject;
import Parser.Pson;

public class Main { 
	public static void main(String[] args) { 
		JsonObject json = new JsonObject()
			.set("user", new JsonObject()
				.set("name", "Danick Imholz")
				.set("nickname", "klimdanick")
		);
		System.out.println(json);
	}
}
```

**Example 2: parsing a json string**:
```java
import DataStructure.JsonArray;
import DataStructure.JsonObject;
import Parser.Pson;

public class Main { 
	public static void main(String[] args) { 
		JsonObject json = Pson.fromString(JSON STRING);
		int name = json.getInt("name");
	}
}
```

**Example 3: parsing a json file**:
```java
import DataStructure.JsonArray;
import DataStructure.JsonObject;
import Parser.Pson;

public class Main { 
	public static void main(String[] args) { 
		JsonObject json = Pson.readFromFile("filename.json");
		int name = json.getInt("name");
	}
}
```

## License

This project is licensed under the MIT License. See the [LICENSE](https://github.com/klimdanick/Pson-Jarser/blob/main/LICENSE) file for details
