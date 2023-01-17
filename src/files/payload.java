package files;

public class payload {
	
	public static String AddPlace() {
		
		return "{\r\n"
				+ "  \"location\": {\r\n"
				+ "    \"lat\": -39.383494,\r\n"
				+ "    \"lng\": 34.427362\r\n"
				+ "  },\r\n"
				+ "  \"accuracy\": 50,\r\n"
				+ "  \"name\": \"Dimona Practice\",\r\n"
				+ "  \"phone_number\": \"(+91) 555 983 893 3937\",\r\n"
				+ "  \"address\": \"299, side layout, cohen 09\",\r\n"
				+ "  \"types\": [\r\n"
				+ "    \"teaching park\",\r\n"
				+ "    \"shopping\"\r\n"
				+ "  ],\r\n"
				+ "  \"website\": \"http://google.com\",\r\n"
				+ "  \"language\": \"Deuch-IN\"\r\n"
				+ "}";
	}
	
	public static String UpdatePlace(String placeId, String newAddress) {
		return "{\r\n"
				+ "\"place_id\":\""+ placeId +"\",\r\n"
				+ "\"address\":\""+ newAddress +"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}";		
	}
	
	public static String CourcePrice() 
	{
		return "{\r\n"
				+ "  \"dashboard\": {\r\n"
				+ "    \"purchaseAmount\": 910,\r\n"
				+ "    \"website\": \"rahulshettyacademy.com\"\r\n"
				+ "  },\r\n"
				+ "  \"courses\": [\r\n"
				+ "    {\r\n"
				+ "      \"title\": \"Selenium Python\",\r\n"
				+ "      \"price\": 50,\r\n"
				+ "      \"copies\": 6\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"title\": \"Cypress\",\r\n"
				+ "      \"price\": 40,\r\n"
				+ "      \"copies\": 4\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"title\": \"RPA\",\r\n"
				+ "      \"price\": 45,\r\n"
				+ "      \"copies\": 10\r\n"
				+ "    }\r\n"
				+ "  ]\r\n"
				+ "}";
	}
	
	public static String AddBook(String isbn, String aisle) 
	{
		 String bookBody = "{\r\n"
				+ "\"name\":\"DV Test book\",\r\n"
				+ "\"isbn\":\""+ isbn +"\",\r\n"
				+ "\"aisle\":\""+ aisle +"\",\r\n"
				+ "\"author\":\"Dima Vlas\"\r\n"
				+ "}";
				
		return bookBody;
	} 

}
