import com.thoughtworks.gauge.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

public class PositiveCases {

    String BASE_URL = "http://localhost:3000/allGrocery";

    JSONObject jsonObject;


    @Step("Tüm ürünlerin get servisi ile çekilip response kontrol edilmesi")
    public void shouldGetAll() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = request.get("/");
        JSONArray productList = new JSONArray(response.getBody().asString());
        JSONObject product = productList.getJSONObject(0);

        assertEquals(200, response.getStatusCode());
        assertFalse(productList.isEmpty());
        assertTrue(product.has("name"));
        assertEquals("apple", product.getString("name"));
    }

    @Step("İsme göre bir ürünün get ile çekilip dataların kontrol edilmesi")
    public void shouldGetProduct() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = request.get("?name=apple");
        JSONArray productList = new JSONArray(response.getBody().asString());
        JSONObject product = productList.getJSONObject(0);

        assertEquals(200, response.getStatusCode());
        assertFalse(productList.isEmpty());
        assertEquals(1, productList.length());
        assertEquals("apple", product.getString("name"));
        assertEquals(0, product.getInt("id"));
        assertEquals(4, product.getInt("price"));
        assertEquals(100, product.getInt("stock"));
    }

    @Step("Yeni bir ürünün başarılı bir şeklilde eklenmesi")
    public void shouldPostNewProduct() {
        jsonObject = new JSONObject();
        jsonObject.put("name", "banana");
        jsonObject.put("price", 250);
        jsonObject.put("stock", 20);

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString());

        Response response = request.post("/");
        JSONObject product = new JSONObject(response.getBody().asString());

        assertEquals(201, response.getStatusCode());
        assertEquals(jsonObject.getString("name"), product.getString("name"));
        assertTrue(product.has("id"));
        assertEquals(jsonObject.getInt("price"), product.getInt("price"));
        assertEquals(jsonObject.getInt("stock"), product.getInt("stock"));

        jsonObject.put("id", product.getInt("id"));
    }

    @Step("Yeni eklenen ürünün get servisi üzerinden datalarının kontrol edilmesi")
    public void shouldGetNewProduct() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = request.get("?name=" + jsonObject.getString("name"));
        JSONArray productList = new JSONArray(response.getBody().asString());
        JSONObject product = productList.getJSONObject(0);

        assertEquals(200, response.getStatusCode());
        assertEquals(1, productList.length());
        assertEquals(jsonObject.getString("name"), product.getString("name"));
        assertEquals(jsonObject.getInt("id"), product.getInt("id"));
        assertEquals(jsonObject.getInt("price"), product.getInt("price"));
        assertEquals(jsonObject.getInt("stock"), product.getInt("stock"));
    }

    @Step("Yeni eklenen ürünün put servisi ile datalarının güncellenmesi")
    public void shouldUpdateProduct() {
        jsonObject.put("price", 400);
        jsonObject.put("stock", 50);

        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString());

        Response response = request.put("/" + jsonObject.getInt("id"));
        JSONObject product = new JSONObject(response.getBody().asString());

        assertEquals(200, response.getStatusCode());
        assertEquals(jsonObject.getString("name"), product.getString("name"));
        assertEquals(jsonObject.getInt("id"), product.getInt("id"));
        assertEquals(jsonObject.getInt("price"), product.getInt("price"));
        assertEquals(jsonObject.getInt("stock"), product.getInt("stock"));
    }

    @Step("Yeni ürünün delete servisi silinmesi ve get servisi ile kontrolü")
    public void deleteProductAndCheckNotExist() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json");

        Response response = request.delete("/" + jsonObject.getInt("id"));

        assertEquals(200, response.getStatusCode());

        response = request.get("/" + jsonObject.getInt("id"));
        assertEquals(404, response.getStatusCode());
    }
}
