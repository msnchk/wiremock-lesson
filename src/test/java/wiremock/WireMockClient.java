package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import config.TestsConfig;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockClient {
    private static WireMockServer wireMockServer;

    public static void startMockServer() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.options().port(TestsConfig.MOCK_SERVER_PORT).notifier(new ConsoleNotifier(true)));
        wireMockServer.start();
        WireMock.configureFor("localhost", TestsConfig.MOCK_SERVER_PORT);
        createStubs();
    }

    public static void stopMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    public static void startProxyServer() {
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

        wireMockServer.startRecording(
                WireMock.recordSpec()
                        .forTarget(TestsConfig.TARGET_API_URL)
                        .makeStubsPersistent(true));
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
        resetAll();
        createPostUserSuccessStub();
        getUserSuccessStub();
        getUserNotFoundStub();
        updatePutUserSuccessStub();
        deleteUserSuccessStub();
    }

    public static void createPostUserSuccessStub() {
        wireMockServer.stubFor(post(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withTransformers("response-template")
                        .withBody("{\"name\":\"{{jsonPath request.body '$.name'}}\"," +
                                "\"job\":\"{{jsonPath request.body '$.job'}}\"," +
                                "\"id\":\"123\"," +
                                "\"createdAt\":\"data\"}")));

    }

    public static void getUserSuccessStub() {
        wireMockServer.stubFor(get(urlEqualTo("/users/2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody("{\"data\":{\"id\":2," +
                                "\"email\":\"janet.weaver@reqres.in\"," +
                                "\"first_name\":\"Janet\"," +
                                "\"last_name\":\"Weaver\"}}")));
    }

    public static void getUserNotFoundStub() {
        wireMockServer.stubFor(get(urlEqualTo("/users/23"))
                .willReturn(aResponse()
                        .withStatus(404)));
    }

    public static void updatePutUserSuccessStub() {
        wireMockServer.stubFor(put(urlEqualTo("/users/2"))
                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(matchingJsonPath("$.job"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withTransformers("response-template") // если хочешь динамическую подстановку
                        .withBody("{\"name\": \"{{jsonPath request.body '$.name'}}\", " +
                                "\"job\": \"{{jsonPath request.body '$.job'}}\", " +
                                "\"updatedAt\": \"data\"}")));
    }

    public static void deleteUserSuccessStub() {
        wireMockServer.stubFor(delete(urlEqualTo("/users/2"))
                .willReturn(aResponse()
                        .withStatus(204)));
    }
}
