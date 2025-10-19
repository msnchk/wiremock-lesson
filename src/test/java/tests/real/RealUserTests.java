package tests.real;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wiremock.WireMockConfig;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class RealUserTests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = WireMockConfig.TARGET_API_URL;
    }

    @Test
    void shouldCreateUserSuccessfully() {
        given()
                .body("[{\"id\":8,\"username\":\"ivanivanov\",\"email\":\"ivanov@mail.ru\"}]")
                .header("Content-Type", "application/json")
                .when()
                .post("/user/createWithList")
                .then()
                .statusCode(200)
                .body("message", equalTo("ok"));
    }

    @Test
    void shouldReturn404ForDelete() {
        when()
                .delete("/user/5555")
                .then()
                .statusCode(404);
    }
}
