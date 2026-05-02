package tests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests {

    String userName;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        userName = faker.internet().domainWord();
        password = faker.internet().password(4, 6);
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel data = new RegistrationBodyModel(userName, password);

        SuccessfulRegistrationResponseModel registrationResponse = given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        assertEquals(userName, registrationResponse.username());
    }

    @Test
    public void existingUserTest() {

        RegistrationBodyModel data = new RegistrationBodyModel(userName, password);

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());

        ExistingUserResponseModel response = given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ExistingUserResponseModel.class);

        String expectedError = ("A user with that username already exists.");
        assertEquals(expectedError, response.username().get(0));
    }
}
