package tests;

import models.login.LoginBodyModel;
import models.login.LoginSuccessfulResponseModel;
import models.login.LoginUnsuccessfulResponseModel;
import org.junit.jupiter.api.Test;
import tests.testdata.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {

    @Test
    public void successfulLoginTest() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);

        LoginSuccessfulResponseModel loginRsponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginSpec)
                .extract().as(LoginSuccessfulResponseModel.class);

        String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginRsponse.access();
        String actualRefresh = loginRsponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPath);
        assertThat(actualRefresh).startsWith(expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongPassword() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.wrongPassword);

        LoginUnsuccessfulResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongLoginSpec)
                .extract().as(LoginUnsuccessfulResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetailError = loginResponse.detail();

        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }
}
