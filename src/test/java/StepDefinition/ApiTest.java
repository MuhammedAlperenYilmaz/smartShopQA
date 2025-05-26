
package StepDefinition;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.json.JSONObject;

import static org.junit.Assert.*;

public class ApiTest {

    Response response;
    String baseUrl = "https://1e209070-8e0b-471f-968d-d0842cf07b0e.mock.pstmn.io";

    @Given("I send a POST request to {string} with headers {string} as {string} and {string} as {string}")
    public void i_send_a_post_request_with_headers(String path, String key1, String value1, String key2, String value2) {
        response = RestAssured.given()
                .header(key1, value1)
                .header(key2, value2)
                .when()
                .post(baseUrl + path);
    }

    @When("I send a GET request to {string} with query param {string} as {string}")
    public void i_send_a_get_request_with_query_param(String path, String param, String value) {
        response = RestAssured.given()
                .queryParam(param, value)
                .when()
                .get(baseUrl + path);
    }

    @Given("I send a POST request to {string} with header {string} as {string} and body:")
    public void i_send_a_post_request_with_body(String path, String headerKey, String headerValue, String body) {
        response = RestAssured.given()
                .header(headerKey, headerValue)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(baseUrl + path);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertEquals(expectedStatusCode.intValue(), response.getStatusCode());
    }

    @Then("the response should contain {string} as true")
    public void the_response_should_contain_as_true(String key) {
        JSONObject json = new JSONObject(response.asString());
        assertTrue(json.getJSONObject("Result").getBoolean(key));
    }
}

