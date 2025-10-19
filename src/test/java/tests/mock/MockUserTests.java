package tests.mock;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import tests.BaseUserTests;
import wiremock.WireMockClient;
import config.TestsConfig;

public class MockUserTests extends BaseUserTests {

    @BeforeAll
    static void setup() {
        WireMockClient.startMockServer();
        RestAssured.baseURI = TestsConfig.MOCK_SERVER_URL;
    }

    @AfterAll
    static void tearDown() {
        WireMockClient.stopMockServer();
    }
}
