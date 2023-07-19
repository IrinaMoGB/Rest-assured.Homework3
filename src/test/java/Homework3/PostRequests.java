package Homework3;

import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostRequests extends AbstractTest {
    @Test
    void testOne()
    {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .then()
                .statusCode(200);
    }

    @Test
    void testTwo()
    {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .then()
                .time(lessThan(800L), TimeUnit.MILLISECONDS);
    }

    @Test
    void testThree()
    {
        String request = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .jsonPath().toString();

        assertThat(request, notNullValue());
    }

    @Test
    void testFour()
    {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .expect()
                .body("cuisine", equalTo("European"))
                .when()
                .post(getBaseUrl() + "recipes/cuisine");
    }

    @Test
    void testFive()
    {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .expect()
                .body("confidence", equalTo(0.1))
                .when()
                .post(getBaseUrl() + "recipes/cuisine");
    }

    @Test
    void addMealTest()
    {
        String responseID =
                given()
                        .queryParam("apiKey", getApiKey())
                        .queryParam("hash", getHash())
                        .pathParams("user_name", getUserName())
                        .body("{\n"
                                + " \"item\": \"5 package sugar\",\n"
                                + " \"aisle\": \"Baking\",\n"
                                + " \"parse\": true\n"
                                + "}"
                        )
                        .log().all()
                        .when()
                        .post(getBaseUrl() + "mealplanner/{user_name}/shopping-list/items")
                        .prettyPeek()
                        .jsonPath()
                        .get("id")
                        .toString();


        given()
                .queryParam("apiKey", getApiKey())
                .pathParams("user_name", getUserName())
                .queryParam("hash", getHash())
                .pathParams("id", responseID)
                .log().all()
                .when()
                .delete(getBaseUrl() + "mealplanner/{user_name}/shopping-list/items/{id}")
                .prettyPeek();
    }
}
