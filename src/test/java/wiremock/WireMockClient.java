package wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import config.TestsConfig;
import data.UserModel;
import data.UsersData;

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
        int someId = 123;
        String someDate = "date";

        wireMockServer.stubFor(post(urlEqualTo(TestsConfig.USERS_PATH))
                .atPriority(1)
                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(matchingJsonPath("$.job"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withTransformers("response-template")
                        .withBody("{\"name\":\"{{jsonPath request.body '$.name'}}\"," +
                                "\"job\":\"{{jsonPath request.body '$.job'}}\"," +
                                "\"id\":\"" + someId + "\"," +
                                "\"createdAt\":\"" + someDate + "\"}")));

    }

    private static void getUserSuccessStub() {
        UserModel user = UsersData.EXISTING_USER;

        wireMockServer.stubFor(get(urlEqualTo(TestsConfig.USERS_PATH + "/" + UsersData.EXISTING_USER.getId()))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody("{\"data\":{" +
                                "\"id\":\"" + user.getId() + "\"," +
                                "\"email\":\"" + user.getEmail() + "\"," +
                                "\"first_name\":\"" + user.getFirstName() + "\"," +
                                "\"last_name\":\"" + user.getLastName() + "\"" +
                                "}}")));
    }

    private static void getUserNotFoundStub() {
        wireMockServer.stubFor(get(urlEqualTo(TestsConfig.USERS_PATH + "/" + UsersData.NON_EXISTENT_USER.getId()))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody("{}")));
    }

    private static void updatePutUserSuccessStub() {
        String someDate = "date";

        wireMockServer.stubFor(put(urlEqualTo(TestsConfig.USERS_PATH + "/" + UsersData.EXISTING_USER.getId()))
                .atPriority(1)
                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(matchingJsonPath("$.job"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withTransformers("response-template")
                        .withBody("{\"name\": \"{{jsonPath request.body '$.name'}}\", " +
                                "\"job\": \"{{jsonPath request.body '$.job'}}\", " +
                                "\"updatedAt\": \"" + someDate + "\"}")));
    }

    private static void deleteUserSuccessStub() {
        wireMockServer.stubFor(delete(urlEqualTo(TestsConfig.USERS_PATH + "/" + UsersData.EXISTING_USER.getId()))
                .atPriority(1)
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withStatus(204)));
    }
}
