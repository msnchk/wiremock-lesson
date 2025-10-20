package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import config.TestsConfig;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockClient {
    private static WireMockServer wireMockServer;

    public static void startMockServerWithStubs() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.options()
                        .port(TestsConfig.MOCK_SERVER_PORT)
                        .notifier(new ConsoleNotifier(true)));
        wireMockServer.start();
        WireMock.configureFor("localhost", TestsConfig.MOCK_SERVER_PORT);
        createStubs();
    }

    public static void startProxyServerWithStubs() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.options()
                        .port(TestsConfig.PROXY_SERVER_PORT)
                        .usingFilesUnderDirectory(TestsConfig.WIREMOCK_RESOURCES_DIR)
                        .notifier(new ConsoleNotifier(true))
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", TestsConfig.PROXY_SERVER_PORT);
        createStubs();

        wireMockServer.stubFor(any(anyUrl())
                .willReturn(aResponse()
                        .proxiedFrom(TestsConfig.TARGET_API_URL)
                        .withTransformers("response-template")));
    }

    public static void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private static void createStubs() {
        createPostUserSuccessStub();
        getUserSuccessStub();
        getUserNotFoundStub();
        updatePutUserSuccessStub();
        deleteUserSuccessStub();
    }

    private static void createPostUserSuccessStub() {
        wireMockServer.stubFor(post(urlEqualTo("/users"))
                .atPriority(1)
                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(matchingJsonPath("$.job"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withTransformers("response-template")
                        .withBody("{\"name\":\"{{jsonPath request.body '$.name'}}\"," +
                                "\"job\":\"{{jsonPath request.body '$.job'}}\"," +
                                "\"id\":\"123\"," +
                                "\"createdAt\":\"data\"}")));

    }

    private static void getUserSuccessStub() {
        wireMockServer.stubFor(get(urlEqualTo("/users/2"))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody("{\"data\":{\"id\":2," +
                                "\"email\":\"janet.weaver@reqres.in\"," +
                                "\"first_name\":\"Janet\"," +
                                "\"last_name\":\"Weaver\"}}")));
    }

    private static void getUserNotFoundStub() {
        wireMockServer.stubFor(get(urlEqualTo("/users/23"))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody("{}")));
    }

    private static void updatePutUserSuccessStub() {
        wireMockServer.stubFor(put(urlEqualTo("/users/2"))
                .atPriority(1)
                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(matchingJsonPath("$.job"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withTransformers("response-template")
                        .withBody("{\"name\": \"{{jsonPath request.body '$.name'}}\", " +
                                "\"job\": \"{{jsonPath request.body '$.job'}}\", " +
                                "\"updatedAt\": \"data\"}")));
    }

    private static void deleteUserSuccessStub() {
        wireMockServer.stubFor(delete(urlEqualTo("/users/2"))
                .atPriority(1)
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withStatus(204)));
    }
}
