package config;

public class TestsConfig {
        public static final String SERVER_URL = "http://localhost";
        public static final int MOCK_SERVER_PORT = 8081;
        public static final int PROXY_SERVER_PORT = 8082;
        public static final String MOCK_SERVER_URL = SERVER_URL + ":" + MOCK_SERVER_PORT;
        public static final String PROXY_SERVER_URL = SERVER_URL + ":" + PROXY_SERVER_PORT;
        public static final String TARGET_API_URL = "https://reqres.in/api";
        public static final String USERS_PATH = "/users";
        public static final String WIREMOCK_RESOURCES_DIR = "src/test/resources/wiremock";

        private TestsConfig() {}
}