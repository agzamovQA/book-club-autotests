package tests.examples;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import models.registration.examples.lombok.RegistrationBodyLombokModel;
import models.registration.examples.lombok.RegistrationResponseLombokModel;
import models.registration.examples.pojo.RegistrationBodyPojoModel;
import models.registration.examples.pojo.RegistrationResponsePojoModel;
import models.registration.examples.records.ExistingUserResponseRecordsModel;
import models.registration.examples.records.RegistrationBodyRecordsModel;
import models.registration.examples.records.RegistrationResponseRecordsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTestsWithModelsExample {

    String userName;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        userName = faker.internet().domainWord();
        password = faker.internet().password(4, 6);
    }

    @Test
    public void successfulRegistrationTest_BadPractice() {

        String data = "{\"username\": \"" + userName + "\",\"password\":\"" + password + "\"}";

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());
    }

    @Test
    public void successfulRegistrationTest() {

        String data = "{\"username\": \"" + userName + "\",\"password\":\"" + password + "\"}";

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());
    }

    @Test
    public void successfulRegistrationTest_withPojo() {

        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(userName);
        data.setPassword(password);

//Constructor example
//RegistrationBodyPojoModel data = new RegistrationBodyPojoModel(userName, password);

        RegistrationResponsePojoModel registrationResponse = given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponsePojoModel.class);

        assertEquals(userName, registrationResponse.getUsername());
    }

    @Test
    public void successfulRegistrationTest_withLombok() {

        RegistrationBodyLombokModel data = new RegistrationBodyLombokModel();
        data.setUsername(userName);
        data.setPassword(password);

//Constructor example
//RegistrationBodyLombokModel data = new RegistrationBodyLombokModel(userName, password);

        RegistrationResponseLombokModel registrationResponse = given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseLombokModel.class);

        assertEquals(userName, registrationResponse.getUsername());
    }

    @Test
    public void successfulRegistrationTest_withRecords() {
        RegistrationBodyRecordsModel data = new RegistrationBodyRecordsModel(userName, password);

        RegistrationResponseRecordsModel registrationResponse = given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseRecordsModel.class);

        assertEquals(userName, registrationResponse.username());
    }

    @Test
    public void existingUserTest_withRecords() {

        RegistrationBodyRecordsModel data = new RegistrationBodyRecordsModel(userName, password);

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());

        ExistingUserResponseRecordsModel response = given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ExistingUserResponseRecordsModel.class);

        String expectedError = ("A user with that username already exists.");
        assertEquals(expectedError, response.username().get(0));
//                .body("username[0]", is("A user with that username already exists."));
    }

    @Test
    public void negativeRegistrationTest415() {

        String data = "{\"username\": \"" + userName + "\",\"password\":\"" + password + "\"}";

        given()
                .body(data)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());
    }

    @Test
    public void jsonParseError() {

        String data = "{\"username\": \"" + userName + "\",\"password\":\"" + password + "\"}";

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());
    }

    @Test
    public void contentTypeError() {

        String data = "{\"username\": \"" + userName + "\",\"password\":\"" + password + "\"}";


        given()
                .body(data)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());
    }

    @Test
    public void invalidUserName() {

        String data = "{\"username\": \"" + userName + "\",\"password\":\"" + password + "\"}";

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(userName))
                .body("id", notNullValue());
    }
}
