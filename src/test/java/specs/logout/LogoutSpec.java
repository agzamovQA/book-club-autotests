package specs.logout;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class LogoutSpec {
    public static RequestSpecification logoutRequestSpec = with()
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification successfulLogoutSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification invalidTokenLogoutSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath(
            "schemas/logout/logout_invalid_token_schema.json"))
            .build();

    public static ResponseSpecification withoutTokenLogoutSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/logout/logout_invalid_token_schema.json"))
            .build();
}
