package tests;

import config.TestsConfig;
import io.restassured.mapper.ObjectMapperType;
import models.UsersMethodsData;
import models.get.UserResponseGet;
import models.post.UserRequestPost;
import models.post.UserResponsePost;
import models.put.UserRequestPut;
import models.put.UserResponsePut;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.Matchers.equalTo;
import org.assertj.core.api.SoftAssertions;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class BaseUserTests {

    @Test
    void createUserSuccessTest() {
        UserRequestPost userRequest = UserRequestPost.builder().build();
        UserResponsePost userResponse = given()
                .body(userRequest)
                .when()
                .post(TestsConfig.USERS_PATH)
                .then()
                .statusCode(201)
                .extract().as(UserResponsePost.class, ObjectMapperType.JACKSON_2);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(userRequest.getName())
                .as("Expected name to match the sent request").
                isEqualTo(userResponse.getName());
        softly.assertThat(userRequest.getJob())
                .as("Expected job to match the sent request")
                .isEqualTo(userResponse.getJob());
        softly.assertThat(userResponse.getId()).
                as("Expected generated ID to be not null")
                .isNotNull();
        softly.assertThat(userResponse.getCreatedAt())
                .as("CreatedAt should be close to current time")
                .isCloseTo(OffsetDateTime.now(), within(5, ChronoUnit.SECONDS));
        softly.assertAll();
    }

    @Test
    void getUserSuccessTest() {
        UserResponseGet expectedUser = UserResponseGet.builder().build();
        UserResponseGet userResponse = given()
                .pathParam("id", expectedUser.getData().getId())
                .when()
                .get(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .extract().as(UserResponseGet.class, ObjectMapperType.JACKSON_2);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(expectedUser.getData().getFirst_name())
                .as("Expected first_name in db to match response first_name").
                isEqualTo(userResponse.getData().getFirst_name());
        softly.assertThat(expectedUser.getData().getLast_name())
                .as("Expected last_name in db to match response last_name").
                isEqualTo(userResponse.getData().getLast_name());
        softly.assertThat(expectedUser.getData().getEmail())
                .as("Expected email in db to match response email").
                isEqualTo(userResponse.getData().getEmail());
        softly.assertThat(expectedUser.getData().getAvatar())
                .as("Expected avatar link in db to match response avatar link").
                isEqualTo(userResponse.getData().getAvatar());
        softly.assertAll();
    }

    @Test
    void getUserNotFoundTest() {
        given()
                .pathParam("id", UsersMethodsData.NON_EXISTENT_USER_ID_FOR_GET)
                .when()
                .get(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    void updateUserSuccessTest() {
        UserRequestPut userRequest = UserRequestPut.builder().build();
        UserResponsePut userResponse = given()
                .pathParam("id", userRequest.getId())
                .body(userRequest)
                .when()
                .put(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .extract().as(UserResponsePut.class, ObjectMapperType.JACKSON_2);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(userRequest.getName())
                .as("Expected name to match the sent request").
                isEqualTo(userResponse.getName());
        softly.assertThat(userRequest.getJob())
                .as("Expected job to match the sent request")
                .isEqualTo(userResponse.getJob());
        softly.assertThat(userResponse.getUpdatedAt())
                .as("UpdatedAt should be close to current time")
                .isCloseTo(OffsetDateTime.now(), within(5, ChronoUnit.SECONDS));
        softly.assertAll();
    }

    @Test
    void deleteUserSuccessTest() {
        given()
                .pathParam("id", UsersMethodsData.EXISTING_USER_ID_FOR_GET_PUT_DELETE)
                .when()
                .delete(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(204);
    }
}
