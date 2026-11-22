package tests.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.ApiHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import tests.BaseUserTests;
import wiremock.WireMockClient;
import config.TestsConfig;

public class MockUserTests extends BaseUserTests {

    @BeforeAll
    static void setup() throws JsonProcessingException {
        WireMockClient.startMockServerWithStubs();
        ApiHelper.setUpRestAssured(TestsConfig.MOCK_SERVER_URL);
    }

    @AfterAll
    static void tearDown() {
        WireMockClient.stopServer();
        ApiHelper.resetRestAssured();
    }
}
