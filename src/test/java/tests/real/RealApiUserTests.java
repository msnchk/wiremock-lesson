package tests.real;

import helpers.ApiHelper;
import config.TestsConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import tests.BaseUserTests;

public class RealApiUserTests extends BaseUserTests {
    @BeforeAll
    static void setup() {
        ApiHelper.setUpRestAssured(TestsConfig.TARGET_API_URL);
    }

    @AfterAll
    static void tearDown() {
        ApiHelper.resetRestAssured();
    }
}
