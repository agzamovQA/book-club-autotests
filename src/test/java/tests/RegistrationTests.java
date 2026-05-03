package tests;

import models.login.response.LoginWithoutPasswordResponseModel;
import models.login.response.LoginWithoutUsernameResponseModel;
import models.registration.response.ExistingUserResponseModel;
import models.registration.request.RegistrationBodyModel;
import models.registration.response.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.testdata.TestData;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.login.LoginSpec.*;
import static specs.registration.RegistrationSpec.*;

@DisplayName("Проверка регистрации на стайте book-club")
public class RegistrationTests extends TestBase {

    String randomUserName, randomUserPassword;

    @BeforeEach
    public void generateRandomData() {
        randomUserName = TestData.returnRandomUsername();
        randomUserPassword = TestData.returnRandomPassword();
    }

    @Test
    @DisplayName("Успешная регистрация")
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(randomUserName, randomUserPassword);

        SuccessfulRegistrationResponseModel registrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        String actualUsername = registrationResponse.username();

        assertThat(actualUsername).isEqualTo(randomUserName);
        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");
    }

    @Test
    @DisplayName("Повторная регистрация пользователя")
    public void existingUserTest() {
        RegistrationBodyModel data = new RegistrationBodyModel(randomUserName, randomUserPassword);

        given(registrationRequestSpec)
                .body(data)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationSpec)
                .body("username", is(randomUserName));

        ExistingUserResponseModel response = given(registrationRequestSpec)
                .body(data)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingRegistrationSpec)
                .extract()
                .as(ExistingUserResponseModel.class);

        String expectedError = ("A user with that username already exists.");
        assertEquals(expectedError, response.username().get(0));
    }

    @Test
    @DisplayName("Регистрация без Username")
    public void registrationWithoutUsername() {
        RegistrationBodyModel data = new RegistrationBodyModel("", randomUserPassword);

        LoginWithoutUsernameResponseModel registrationResponse = given(registrationRequestSpec)
                .body(data)
                .when()
                .post("/users/register/")
                .then()
                .spec(loginWithoutUserNameSpec)
                .extract().as(LoginWithoutUsernameResponseModel.class);

        String expectedUsernameError = "This field may not be blank.";
        String actualDetailError = registrationResponse.username().get(0);

        assertThat(actualDetailError).isEqualTo(expectedUsernameError);
    }

    @Test
    @DisplayName("Регистрация без Password")
    public void registrationWithoutPassword() {
        RegistrationBodyModel data = new RegistrationBodyModel(randomUserName, "");

        LoginWithoutPasswordResponseModel registrationResponse = given(registrationRequestSpec)
                .body(data)
                .when()
                .post("/users/register/")
                .then()
                .spec(loginWithoutPasswordSpec)
                .extract().as(LoginWithoutPasswordResponseModel.class);

        String expectedUsernameError = "This field may not be blank.";
        String actualDetailError = registrationResponse.password().get(0);

        assertThat(actualDetailError).isEqualTo(expectedUsernameError);
    }
}
