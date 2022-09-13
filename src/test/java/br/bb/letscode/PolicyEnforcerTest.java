package br.bb.letscode;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;

@QuarkusTest
public class PolicyEnforcerTest {
    static {
        RestAssured.useRelaxedHTTPSValidation();
    }

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @Test
    public void testAccessUserResource() {
        RestAssured.given().auth().oauth2(getAccessToken("alice"))
                .when().get("/api/users/me")
                .then()
                .statusCode(200);
        RestAssured.given().auth().oauth2(getAccessToken("bob"))
                .when().get("/api/users/me")
                .then()
                .statusCode(200);
    }

    @Test
    public void testAccessAdminResource() {
        RestAssured.given().auth().oauth2(getAccessToken("alice"))
                .when().get("/api/admin")
                .then()
                .statusCode(200);
    
        RestAssured.given().auth().oauth2(getAccessToken("bob"))
                .when().get("/api/admin")
                .then()
                .statusCode(403);
    }

    private String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }
}
