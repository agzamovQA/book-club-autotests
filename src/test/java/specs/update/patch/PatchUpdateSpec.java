package specs.update.patch;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PatchUpdateSpec {
    public static RequestSpecification patchUpdateRequestSpec = with()
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification successfulPatchUpdateSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/update/patch/patch_update_user_successful.json"))
            .build();
}
