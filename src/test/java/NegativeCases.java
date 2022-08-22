import com.thoughtworks.gauge.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

public class NegativeCases {

    String BASE_URL = "http://localhost:3000/allGrocery";
    JSONObject jsonObject;


    @Step("Post servisi ile 500 hata kodunun simüle edilmesi")
    public void shouldResponseCode500() {
        jsonObject = new JSONObject();
        jsonObject.put("name", "banana");
        jsonObject.put("price", 250);
        jsonObject.put("stock", 20);
        jsonObject.put("id", "0");

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString());

        Response response = request.post("/");

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Error: Insert failed, duplicate id"));
    }

    @Step("Post servisi ile 400 hata kodunun simüle edilmesi")
    public void shouldResponseCode400() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString().substring(1));

        Response response = request.post("/");

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("SyntaxError: Unexpected token"));
    }

    @Step("Get servisi ile 404 hata kodunun simüle edilmesi")
    public void shouldResponseCode404() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = request.get("/100");
        assertEquals(404, response.getStatusCode());
    }
}
