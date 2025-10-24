package tests.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import helpers.ApiHelper;
import tests.BaseUserTests;
import wiremock.WireMockClient;
import config.TestsConfig;

public class ProxyUserTests extends BaseUserTests {
    @BeforeAll
    static void setup() throws JsonProcessingException {
        WireMockClient.startProxyServerWithStubs();
        ApiHelper.setUpRestAssured(TestsConfig.PROXY_SERVER_URL);
    }

    @AfterAll
    static void tearDown() {
        WireMockClient.stopServer();
        ApiHelper.resetRestAssured();
    }
}
