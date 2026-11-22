package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiHelper {
    private static final String apiKeyName = PropertyProvider.getProperty("api.key.name");
    private static final String apiKeyValue = PropertyProvider.getProperty("api.key.value");

    public static void setUpRestAssured(String baseUrl) {
        ObjectMapper mapper = JsonHelper.getMapper();
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> mapper)
        );
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .addHeader(apiKeyName, apiKeyValue)
                .build();
        RestAssured.requestSpecification = requestSpec;
    }

    public static void resetRestAssured() {
        RestAssured.reset();
    }
}
