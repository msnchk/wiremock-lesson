package tests;

import config.TestsConfig;
import data.UserModel;
import data.UsersData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BaseUserTests {
    @ParameterizedTest
    @MethodSource("data.UsersData#newUsersProvider")
    void createUserSuccessTest(UserModel newUser) {
        given()
                .body("{\"name\": \"" + newUser.getFirstName() + "\", \"job\": \"" + newUser.getJob() + "\"}")
                .when()
                .post(TestsConfig.USERS_PATH)
                .then()
                .statusCode(201)
                .body("name", equalTo(newUser.getFirstName()))
                .body("job", equalTo(newUser.getJob()))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    void getUserSuccessTest() {
        UserModel user = UsersData.EXISTING_USER;

        given()
                .pathParam("id", user.getId())
                .when()
                .get(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .body("data.email", equalTo(user.getEmail()))
                .body("data.first_name", equalTo(user.getFirstName()))
                .body("data.last_name", equalTo(user.getLastName()));
    }

    @Test
    void getUserNotFoundTest() {
        given()
                .pathParam("id", UsersData.NON_EXISTENT_USER.getId())
                .when()
                .get(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    void updateUserSuccessTest() {
        String newName = "updatedName";
        String newJob = "updatedJob";

        given()
                .pathParam("id", UsersData.EXISTING_USER.getId())
                .body("{\"name\": \"" + newName + "\", \"job\": \"" + newJob + "\"}")
                .when()
                .put(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo(newName))
                .body("job", equalTo(newJob))
                .body("updatedAt", notNullValue());
    }

    @Test
    void deleteUserSuccessTest() {
        given()
                .pathParam("id", UsersData.EXISTING_USER.getId())
                .when()
                .delete(TestsConfig.USERS_PATH + "/{id}")
                .then()
                .statusCode(204);
    }
}
