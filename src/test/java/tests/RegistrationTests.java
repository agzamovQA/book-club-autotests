package tests;

import io.restassured.http.ContentType;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.Test;
import tests.testdata.TestData;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests extends TestBase {

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(TestData.getRandomUserName, TestData.getRandomUserPassword);

        SuccessfulRegistrationResponseModel registrationResponse = given()
                .body(registrationData)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .basePath("/api/v1")
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/registration/registration_successful_response_schema.json"))
                .body("id", notNullValue())
                .body("username", notNullValue())
                .body("remoteAddr", notNullValue())
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        String actualUsername = registrationResponse.username();
        assertThat(actualUsername).isEqualTo(TestData.getRandomUserName);
        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");
    }

    @Test
    public void existingUserTest() {

        RegistrationBodyModel data = new RegistrationBodyModel(TestData.getRandomUserName, TestData.getRandomUserPassword);

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .basePath("/api/v1")
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(TestData.getRandomUserName))
                .body("id", notNullValue());

        ExistingUserResponseModel response = given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .basePath("/api/v1")
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registration/registration_unsuccessful_response_schema.json"))
                .extract()
                .as(ExistingUserResponseModel.class);

        String expectedError = ("A user with that username already exists.");
        assertEquals(expectedError, response.username().get(0));
    }
}
