package tests;

import models.login.request.LoginBodyModel;
import models.logout.request.LogoutBodyModel;
import models.logout.response.LogoutWithInvalidTokenBodyModel;
import models.logout.response.LogoutWithoutTokenBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import data.TestData;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Проверка выхода из профиля на стайте book-club")
public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Успешный выход из профиля")
    public void successfulLogoutTest() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);
        String actualRefresh = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(actualRefresh);
        api.auth.logoutWithRefreshToken(logoutData);
    }

    @Test
    @DisplayName("Выход с невалидным токеном")
    public void logoutWithInvalidToken() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);
        String actualRefresh = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(actualRefresh + 222);
        LogoutWithInvalidTokenBodyModel logoutResponse = api.auth.logoutWithInvalidToken(logoutData);

            String actualDetail = logoutResponse.getDetail();
            String actualCode = logoutResponse.getCode();

            String expectedDetail = "Token is invalid";
            String expectedCode = "token_not_valid";

            assertThat(actualDetail).isEqualTo(expectedDetail);
            assertThat(actualCode).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("Выход без использования токена")
    public void logoutWithoutToken() {

        LoginBodyModel loginData = new LoginBodyModel(TestData.regularUserName, TestData.regularUserPassword);
        String actualRefresh = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel("");
        LogoutWithoutTokenBodyModel logoutResponse = api.auth.logoutWithoutToken(logoutData);

            String expectedUsernameError = "This field may not be blank.";
            String actualDetailError = logoutResponse.getRefresh().get(0);

            assertThat(actualDetailError).isEqualTo(expectedUsernameError);
    }
}
