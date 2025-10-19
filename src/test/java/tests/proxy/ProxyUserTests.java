package tests.proxy;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wiremock.WireMockClient;
import wiremock.WireMockConfig;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class ProxyUserTests {
    @BeforeAll
    static void setup() {
        WireMockClient.startProxyServer();
        WireMockClient.createStubs();
        RestAssured.baseURI = WireMockConfig.PROXY_SERVER_URL;
    }

    @AfterAll
    static void tearDown() {
        WireMockClient.stopProxyServer();
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
