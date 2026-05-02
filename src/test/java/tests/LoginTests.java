package tests;

import models.login.*;
import org.junit.jupiter.api.Test;
import tests.testdata.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.*;
import static tests.testdata.TestData.prefix_jwt;

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

        String expectedTokenPath = prefix_jwt;
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

    @Test
    public void loginWithoutUserName() {

        LoginBodyModel loginData = new LoginBodyModel("", TestData.wrongPassword);

        LoginWithoutUsernameResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutUserNameSpec)
                .extract().as(LoginWithoutUsernameResponseModel.class);

        String expectedUsernameError = "This field may not be blank.";
        String actualDetailError = loginResponse.username().get(0);

        assertThat(actualDetailError).isEqualTo(expectedUsernameError);
    }

    @Test
    public void loginWithoutPassword() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, "");

        LoginWithoutPasswordResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(loginWithoutPasswordSpec)
                .extract().as(LoginWithoutPasswordResponseModel.class);

        String expectedUsernameError = "This field may not be blank.";
        String actualDetailError = loginResponse.password().get(0);

        assertThat(actualDetailError).isEqualTo(expectedUsernameError);
    }
}
