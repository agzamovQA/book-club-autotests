package tests;

import data.TestData;
import models.login.request.LoginBodyModel;
import models.login.response.LoginSuccessfulResponseModel;
import models.registration.request.RegistrationBodyModel;
import models.registration.response.SuccessfulRegistrationResponseModel;
import models.update.patch.request.PatchUpdateBodyModel;
import models.update.patch.response.PatchUpdateSuccessfulResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginSpec;
import static specs.registration.RegistrationSpec.registrationRequestSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationSpec;
import static specs.update.patch.PatchUpdateSpec.patchUpdateRequestSpec;
import static specs.update.patch.PatchUpdateSpec.successfulPatchUpdateSpec;

@DisplayName("Обновление данных пользователей через метод PUT")
public class PatchUpdateUserTests extends TestBase {

    String randomUserName, randomUserPassword, newRandomUserName, newRandomFirstName, newRandomLastName, newRandomEmail;

    @BeforeEach
    public void generateRandomData() {
        randomUserName = TestData.returnRandomUsername();
        randomUserPassword = TestData.returnRandomPassword();
        newRandomUserName = TestData.returnRandomUsername();
        newRandomFirstName = TestData.returnRandomFirstName();
        newRandomLastName = TestData.returnRandomLastName();
        newRandomEmail = TestData.returnRandomEmail();
    }

    @Test
    @DisplayName("E2E. Регистрация -> Обновление НЕ всех пользователя (PATCH)")
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

        PatchUpdateBodyModel updateData = new PatchUpdateBodyModel(newRandomUserName,
                newRandomFirstName, "", newRandomEmail);

        PatchUpdateSuccessfulResponseModel patchUpdateResponse = given(patchUpdateRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulPatchUpdateSpec)
                .extract().as(PatchUpdateSuccessfulResponseModel.class);

        assertThat(patchUpdateResponse.username()).isNotEqualTo(registrationResponse.username());
        assertThat(patchUpdateResponse.firstName()).isNotEqualTo(registrationResponse.firstName());
        assertThat(patchUpdateResponse.lastName()).isEqualTo(registrationResponse.lastName());
        assertThat(patchUpdateResponse.email()).isNotEqualTo(registrationResponse.email());
    }

    @Test
    @DisplayName("E2E. Регистрация -> Обновление ВСЕХ пользователя (PATCH)")
    public void successfulPutUpdateUserAllFieldsTest() {
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

        PatchUpdateBodyModel updateData = new PatchUpdateBodyModel(newRandomUserName,
                newRandomFirstName, newRandomLastName, newRandomEmail);

        PatchUpdateSuccessfulResponseModel patchUpdateResponse = given(patchUpdateRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulPatchUpdateSpec)
                .extract().as(PatchUpdateSuccessfulResponseModel.class);

        assertThat(patchUpdateResponse.username()).isNotEqualTo(registrationResponse.username());
        assertThat(patchUpdateResponse.firstName()).isNotEqualTo(registrationResponse.firstName());
        assertThat(patchUpdateResponse.lastName()).isNotEqualTo(registrationResponse.lastName());
        assertThat(patchUpdateResponse.email()).isNotEqualTo(registrationResponse.email());
    }
}
