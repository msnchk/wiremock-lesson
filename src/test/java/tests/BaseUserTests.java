package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BaseUserTests {
    @ParameterizedTest
    @CsvSource({
            "firstName, firstJob",
            "secondName, secondJob"
    })
    void createUserSuccessTest(String name, String job) {
        given()
                .body("{\"name\": \"" + name + "\", \"job\": \"" + job + "\"}")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    void getUserSuccessTest() {
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", equalTo("janet.weaver@reqres.in"))
                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"));
    }

    @Test
    void getUserNotFoundTest() {
        given()
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void updateUserSuccessTest() {
        String name = "updatedName";
        String job = "updatedJob";

        given()
                .body("{\"name\": \"" + name + "\", \"job\": \"" + job + "\"}")
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo(name))
                .body("job", equalTo(job))
                .body("updatedAt", notNullValue());
    }

    @Test
    void deleteUserSuccessTest() {
        given()
                .when()
                .delete("/users/5")
                .then()
                .statusCode(204);
    }
}
