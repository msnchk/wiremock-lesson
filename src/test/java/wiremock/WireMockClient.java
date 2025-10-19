package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockClient {
    private static WireMockServer wireMockServer;

    public static void startMockServer() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.options().port(WireMockConfig.MOCK_SERVER_PORT));
        wireMockServer.start();
        WireMock.configureFor("localhost", WireMockConfig.MOCK_SERVER_PORT);

    }

    public static void stopMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    public static void startProxyServer() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.options()
                        .port(WireMockConfig.PROXY_SERVER_PORT)
                        .usingFilesUnderDirectory(WireMockConfig.WIREMOCK_RESOURCES_DIR)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", WireMockConfig.PROXY_SERVER_PORT);
        wireMockServer.startRecording(WireMockConfig.TARGET_API_URL);
    }

    public static void stopProxyServer() {
        if (wireMockServer != null) {
            wireMockServer.stopRecording();
            wireMockServer.stop();
        }
    }

    public static void resetAll() {
        WireMock.reset();
    }

    public static void createStubs() {
        createPostSuccessStub();
        createDeleteErrorStub();
    }

    private static void createPostSuccessStub() {
        stubFor(post(urlEqualTo("/user/createWithList"))
                .withRequestBody(equalToJson("[{\"id\":8,\"username\":\"ivanivanov\",\"email\":\"ivanov@mail.ru\"}]",
                        true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"code\":200,\"type\":\"unknown\",\"message\":\"ok\"}")));
    }
    private static void createDeleteErrorStub() {
        stubFor(delete(urlEqualTo("/user/5555"))
                .willReturn(aResponse()
                        .withStatus(404)));
    }


}
