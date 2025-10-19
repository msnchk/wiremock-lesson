package tests.mock;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import wiremock.WireMockClient;
import wiremock.WireMockConfig;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class MockUserTests {

    @BeforeAll
    static void setup() {
        WireMockClient.startMockServer();
        WireMockClient.createStubs();
        RestAssured.baseURI = WireMockConfig.MOCK_SERVER_URL;
    }

    @AfterAll
    static void tearDown() {
        WireMockClient.stopMockServer();
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
