package tests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import models.pojo.RegistrationBodyPojoModel;
import models.pojo.RegistrationResponsePojoModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests {

    @Test
    public void successfulRegistrationTest_BadPractice() {

        Faker faker = new Faker();
        String userName = faker.internet().domainWord();
        String password = faker.internet().password(4, 6);

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

        Faker faker = new Faker();
        String userName = faker.internet().domainWord();
        String password = faker.internet().password(4, 6);

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

        Faker faker = new Faker();
        String userName = faker.internet().domainWord();
        String password = faker.internet().password(4, 6);

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
    public void existingUserTest() {

        Faker faker = new Faker();
        String userName = faker.internet().domainWord();
        String password = faker.internet().password(4, 6);

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

        Faker faker = new Faker();
        String userName = faker.name().fullName();
        String password = faker.internet().password(4, 6);

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

        Faker faker = new Faker();
        String userName = faker.name().fullName();
        String password = faker.internet().password(4, 6);

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

        Faker faker = new Faker();
        String userName = faker.name().fullName();
        String password = faker.internet().password(4, 6);

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

        Faker faker = new Faker();
        String userName = "Maxwell Kilback";
        String password = faker.internet().password(4, 6);

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
