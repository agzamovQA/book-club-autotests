package tests.examples;

import io.restassured.http.ContentType;
import models.login.request.LoginBodyModel;
import models.login.response.LoginSuccessfulResponseModel;
import models.login.response.LoginWrongPasswordResponseModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTestsWithoutSpecs extends TestBase {

    String userName = "Agzamurai";
    String password = "Qwer1234";
    String wrongPassword = "Qwer4321";

    @Test
    @Disabled
    public void successfulLoginTest1() {

        LoginBodyModel loginData = new LoginBodyModel(userName, password);

        LoginSuccessfulResponseModel loginRsponse = given()
                .body(loginData)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .basePath("/api/v1")
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/login/login_response_schema.json"))
                .body("refresh", notNullValue())
                .body("access", notNullValue())
                .extract().as(LoginSuccessfulResponseModel.class);

        String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginRsponse.getAccess();
        String actualRefresh = loginRsponse.getRefresh();

        assertThat(actualAccess).startsWith(expectedTokenPath);
        assertThat(actualRefresh).startsWith(expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    @Disabled
    public void wrongPassword() {

        LoginBodyModel loginData = new LoginBodyModel(userName, wrongPassword);

        LoginWrongPasswordResponseModel loginResponse = given()
                .body(loginData)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .basePath("/api/v1")
                .post("/auth/token/")
                .then()
                .log().all()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("schemas/login/login_unsuccessful_schema.json"))
                .body("detail", containsString("Invalid username or password."))
                .extract().as(LoginWrongPasswordResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetailError = loginResponse.getDetail();

        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }
}
