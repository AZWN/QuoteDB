package nl.azwaan.quotedb.integration.api;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class AuthenticatedTest extends APITest {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private String userName;
    private String password;
    private String token;

    @Before
    public void createUserAndToken() {
        userName = randomString((int) (Math.random() * 4 + 4));
        password = randomString((int) (Math.random() * 8 + 8));

        String userBody = String.format("{\"userName\": \"%s\", \"password\": \"%s\"}", userName, password);

        given()
                .body(userBody)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/auth/register")
                .then()
                .statusCode(201);

        JsonPath body = given()
                .body(userBody)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post("/auth/login")
                .then()
                .statusCode(202)
                .extract()
                .body()
                .jsonPath();

        token = body.getString("token");
    }

    private String randomString(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();
    }

    public String getToken() {
        return this.token;
    }

    @Override
    protected RequestSpecification postBase() {
        return super.postBase()
                .header("Authorization", getToken());
    }

    protected RequestSpecification getBase() {
        return given()
                .header("Authorization", getToken());
    }

    protected RequestSpecification patchBase() {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", getToken());
    }
}
