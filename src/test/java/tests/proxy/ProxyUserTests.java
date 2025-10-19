package tests.proxy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import helpers.ApiHelper;
import tests.BaseUserTests;
import wiremock.WireMockClient;
import config.TestsConfig;

public class ProxyUserTests extends BaseUserTests {
    @BeforeAll
    static void setup() {
        WireMockClient.startProxyServer();
        ApiHelper.setUpRestAssured(TestsConfig.PROXY_SERVER_URL);
    }

    @AfterAll
    static void tearDown() {
        WireMockClient.stopProxyServer();
    }
}
