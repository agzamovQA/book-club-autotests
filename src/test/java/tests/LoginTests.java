package tests;

import models.login.request.LoginBodyModel;
import models.login.response.LoginSuccessfulResponseModel;
import models.login.response.LoginWithoutPasswordResponseModel;
import models.login.response.LoginWithoutUsernameResponseModel;
import models.login.response.LoginWrongPasswordResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.testdata.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.*;
import static tests.testdata.TestData.prefix_jwt;

@DisplayName("Проверка авторизации на стайте book-club")
public class LoginTests extends TestBase {

    @Test
    @DisplayName("Успешная авторизация")
    public void successfulLoginTest() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);

        LoginSuccessfulResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginSpec)
                .extract().as(LoginSuccessfulResponseModel.class);

        String expectedTokenPath = prefix_jwt;
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPath);
        assertThat(actualRefresh).startsWith(expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    public void wrongPassword() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.wrongPassword);

        LoginWrongPasswordResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongLoginSpec)
                .extract().as(LoginWrongPasswordResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetailError = loginResponse.detail();

        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

    @Test
    @DisplayName("Авторизация без Username")
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
    @DisplayName("Авторизация без Password")
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
