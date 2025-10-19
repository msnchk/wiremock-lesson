package tests.real;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import config.TestsConfig;
import tests.BaseUserTests;

public class RealUserTests extends BaseUserTests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = TestsConfig.TARGET_API_URL;
    }

}
