package api;

import io.qameta.allure.Step;
import models.login.request.LoginBodyModel;
import models.logout.request.LogoutBodyModel;
import models.logout.response.LogoutWithInvalidTokenBodyModel;
import models.logout.response.LogoutWithoutTokenBodyModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApiClient {
    @Step("Авторизация и получение Refresh токена")
    public static String loginAndGetRefreshToken(LoginBodyModel loginData) {
        return
                given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginSpec)
                .extract().path("refresh");
    }

    @Step("Отправка запроса logout c использованием Refresh токена")
    public static void logoutWithRefreshToken(LogoutBodyModel logoutBody) {
        given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutSpec);
    }

    @Step("Отправка запроса logout c использованием невалидного токена")
    public static LogoutWithInvalidTokenBodyModel logoutWithInvalidToken (LogoutBodyModel logoutData) {
        return
                given(logoutRequestSpec)
                        .body (logoutData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(invalidTokenLogoutSpec)
                        .extract().as(LogoutWithInvalidTokenBodyModel.class);
    }

    @Step("Отправка запроса logout c использованием невалидного токена")
    public static LogoutWithoutTokenBodyModel logoutWithoutToken (LogoutBodyModel logoutData) {
        return
                given(logoutRequestSpec)
                        .body (logoutData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(withoutTokenLogoutSpec)
                        .extract().as(LogoutWithoutTokenBodyModel.class);
    }



}
