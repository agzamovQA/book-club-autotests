package tests;

import data.TestData;
import models.login.request.LoginBodyModel;
import models.login.response.LoginSuccessfulResponseModel;
import models.registration.request.RegistrationBodyModel;
import models.registration.response.SuccessfulRegistrationResponseModel;
import models.update.put.request.PutUpdateBodyModel;
import models.update.put.response.PutUpdateSuccessfulResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.*;
import static specs.login.LoginSpec.successfulLoginSpec;
import static specs.registration.RegistrationSpec.*;
import static specs.update.put.PutUpdateSpec.putUpdateRequestSpec;
import static specs.update.put.PutUpdateSpec.successfulPutUpdateSpec;

@DisplayName("Обновление данных пользователей через метод PUT")
public class PutUpdateUserTests extends TestBase {

    String randomUserName, randomUserPassword, newRandomUserName, newRandomFirstName, newRandomLastName, newRandomEmail;

    @BeforeEach
    public void generateRandomData() {
        randomUserName = TestData.returnRandomUsername();
        randomUserPassword = TestData.returnRandomPassword();
        newRandomUserName = TestData.randomUsername;
        newRandomFirstName = TestData.randomFirstName;
        newRandomLastName = TestData.randomLastName;
        newRandomEmail = TestData.randomEmail;
    }

    @Test
    @DisplayName("E2E. Регистрация -> Обновление пользователя (PUT)")
    public void successfulPutUpdateUserTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(randomUserName, randomUserPassword);

        SuccessfulRegistrationResponseModel registrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        LoginBodyModel loginData = new LoginBodyModel(randomUserName, randomUserPassword);

        LoginSuccessfulResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginSpec)
                .extract().as(LoginSuccessfulResponseModel.class);

        String accessToken = loginResponse.access();

        PutUpdateBodyModel updateData = new PutUpdateBodyModel(newRandomUserName,
                newRandomFirstName, newRandomLastName, newRandomEmail);

        PutUpdateSuccessfulResponseModel putUpdateResponse = given(putUpdateRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulPutUpdateSpec)
                .extract().as(PutUpdateSuccessfulResponseModel.class);

        assertThat(putUpdateResponse.username()).isNotEqualTo(registrationResponse.username());
        assertThat(putUpdateResponse.firstName()).isNotEqualTo(registrationResponse.firstName());
        assertThat(putUpdateResponse.lastName()).isNotEqualTo(registrationResponse.lastName());
        assertThat(putUpdateResponse.email()).isNotEqualTo(registrationResponse.email());
    }
}
