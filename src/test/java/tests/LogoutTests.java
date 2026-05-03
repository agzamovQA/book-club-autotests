package tests;

import models.login.request.LoginBodyModel;
import models.login.response.LoginSuccessfulResponseModel;
import models.logout.request.LogoutBodyModel;
import models.logout.response.LogoutSuccessfulBodyModel;
import models.logout.response.LogoutWithInvalidTokenBodyModel;
import models.logout.response.LogoutWithoutTokenBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.testdata.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

@DisplayName("Проверка выхода из профиля на стайте book-club")
public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Успешный выход из профиля")
    public void successfulLogoutTest() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);

        LoginSuccessfulResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginSpec)
                .extract().as(LoginSuccessfulResponseModel.class);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).isNotEqualTo(actualRefresh);

        LogoutBodyModel logoutData = new LogoutBodyModel(actualRefresh);

        LogoutSuccessfulBodyModel logoutResponse = given(logoutRequestSpec)
                .body (logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutSpec)
                .extract().as(LogoutSuccessfulBodyModel.class);
    }

    @Test
    @DisplayName("Выход с невалидным токеном")
    public void logoutWithInvalidToken() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);

        LoginSuccessfulResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginSpec)
                .extract().as(LoginSuccessfulResponseModel.class);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).isNotEqualTo(actualRefresh);

        LogoutBodyModel logoutData = new LogoutBodyModel(actualRefresh + 222);

        LogoutWithInvalidTokenBodyModel logoutResponse = given(logoutRequestSpec)
                .body (logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(invalidTokenLogoutSpec)
                .extract().as(LogoutWithInvalidTokenBodyModel.class);

        String actualDetail = logoutResponse.detail();
        String actualCode = logoutResponse.code();

        String expectedDetail = "Token is invalid";
        String expectedCode = "token_not_valid";

        assertThat(actualDetail).isEqualTo(expectedDetail);
        assertThat(actualCode).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("Выход без использования токена")
    public void logoutWithoutToken() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);

        LoginSuccessfulResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginSpec)
                .extract().as(LoginSuccessfulResponseModel.class);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).isNotEqualTo(actualRefresh);

        LogoutBodyModel logoutData = new LogoutBodyModel("");

        LogoutWithoutTokenBodyModel logoutResponse = given(logoutRequestSpec)
                .body (logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(withoutTokenLogoutSpec)
                .extract().as(LogoutWithoutTokenBodyModel.class);

        String expectedUsernameError = "This field may not be blank.";
        String actualDetailError = logoutResponse.refresh().get(0);

        assertThat(actualDetailError).isEqualTo(expectedUsernameError);
    }
}
