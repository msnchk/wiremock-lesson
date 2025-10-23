package wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import config.TestsConfig;
import helpers.JsonHelper;
import models.UsersMethodsData;
import models.get.UserResponseGet;
import models.post.UserResponsePost;
import models.put.UserResponsePut;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockClient {
    private static WireMockServer wireMockServer;
    private static final ObjectMapper mapper = JsonHelper.getMapper();
    private static final String CONTENT_TYPE_HEADER = "application/json; charset=utf-8";

    public static void startMockServerWithStubs() throws JsonProcessingException {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.options()
                        .port(TestsConfig.MOCK_SERVER_PORT)
                        .notifier(new ConsoleNotifier(true)));
        wireMockServer.start();
        WireMock.configureFor("localhost", TestsConfig.MOCK_SERVER_PORT);
        createStubs();
    }

    public static void startProxyServerWithStubs() throws JsonProcessingException {
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

    private static void createStubs() throws JsonProcessingException {
        createPostUserSuccessStub();
        getUserSuccessStub();
        getUserNotFoundStub();
        updatePutUserSuccessStub();
        deleteUserSuccessStub();
    }

    private static void createPostUserSuccessStub() throws JsonProcessingException {
        UserResponsePost user = UserResponsePost.builder().build();
        String userJson = mapper.writeValueAsString(user);

        wireMockServer.stubFor(post(urlEqualTo(TestsConfig.USERS_PATH))
                .atPriority(1)
                .withRequestBody(matchingJsonPath("$.name", equalTo(user.getName())))
                .withRequestBody(matchingJsonPath("$.job", equalTo(user.getJob())))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                        .withBody(userJson)));

    }

    private static void getUserSuccessStub() throws JsonProcessingException {
        UserResponseGet user = UserResponseGet.builder().build();
        String userJson = mapper.writeValueAsString(user);

        wireMockServer.stubFor(get(urlEqualTo(TestsConfig.USERS_PATH + "/" + user.getData().getId()))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                        .withBody(userJson)));
    }

    private static void getUserNotFoundStub() {
        wireMockServer.stubFor(get(urlEqualTo(TestsConfig.USERS_PATH + "/" + UsersMethodsData.NON_EXISTENT_USER_ID_FOR_GET))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                        .withBody("{}")));
    }

    private static void updatePutUserSuccessStub() throws JsonProcessingException {
        UserResponsePut user = UserResponsePut.builder().build();
        String userJson = mapper.writeValueAsString(user);

        wireMockServer.stubFor(put(urlEqualTo(TestsConfig.USERS_PATH + "/" + UsersMethodsData.EXISTING_USER_ID_FOR_GET_PUT_DELETE))
                .atPriority(1)
                .withRequestBody(matchingJsonPath("$.name", equalTo(user.getName())))
                .withRequestBody(matchingJsonPath("$.job", equalTo(user.getJob())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                        .withBody(userJson)));
    }

    private static void deleteUserSuccessStub() {
        wireMockServer.stubFor(delete(urlEqualTo(TestsConfig.USERS_PATH + "/" + UsersMethodsData.EXISTING_USER_ID_FOR_GET_PUT_DELETE))
                .atPriority(1)
                .willReturn(aResponse()
                        .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                        .withStatus(204)));
    }
}
