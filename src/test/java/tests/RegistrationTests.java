package tests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import models.lombok.RegistrationBodyLombokModel;
import models.lombok.RegistrationResponseLombokModel;
import models.pojo.RegistrationBodyPojoModel;
import models.pojo.RegistrationResponsePojoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests {

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
    public void existingUserTest() {

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

        given()
                .body(data)
                .contentType(ContentType.JSON)
                .when()
                .log().all()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body("username[0]", is("A user with that username already exists."));
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
