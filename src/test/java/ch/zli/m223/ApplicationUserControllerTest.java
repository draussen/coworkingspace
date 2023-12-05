package ch.zli.m223;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import ch.zli.m223.model.ApplicationUser;

import static io.restassured.RestAssured.given;

import io.quarkus.test.h2.H2DatabaseTestResource;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
@TestSecurity(user = "test@example.com", roles = "Besucher")
public class ApplicationUserControllerTest {


@Test
@Order(1)
public void testCreateUserEndpoint() {
    ApplicationUser newUser = new ApplicationUser();
    newUser.setName("Neuer");
    newUser.setSurname("Benutzer");
    newUser.setEmail("neu@benutzer.ch");
    newUser.setPassword("pass123");

    given()
        .contentType(ContentType.JSON)
        .body(newUser)
    .when()
        .post("/users")
    .then()
        .statusCode(201);
}

}