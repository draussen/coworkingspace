package ch.zli.m223;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Booking;
import ch.zli.m223.model.Credential;
import ch.zli.m223.model.Role;

import static io.restassured.RestAssured.given;

import io.quarkus.test.h2.H2DatabaseTestResource;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
@TestSecurity(user = "test@example.com", roles = "Besucher")
public class ApplicationUserControllerTest {

    @Test
    @Order(1)
    public void testCreateUser() {
        ApplicationUser newUser = new ApplicationUser();
        newUser.setName("Test");
        newUser.setSurname("User");
        newUser.setEmail("hallo@test.com");
        newUser.setPassword("test123");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    @TestSecurity(user = "user2@example.com", roles = "Mitglied")
    public void testGetUser() {

        given()
                .when().get("/users/1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @TestSecurity(user = "user2@example.com", roles = "Mitglied")
    public void testDeleteUser() {

        given()
                .when().delete("/users/1")
                .then()
                .statusCode(204);

        given()
                .when().get("/users/1")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    @TestSecurity(user = "testuser@test.com", roles = "Besucher")
    public void testVisitorForbiddenGettingUsers() {
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(403);
    }

    @Test
    @Order(5)
    public void testUpdateUserIsForbiddenForNotExistingAdmin() {
        Long userId = 1L; // Beispiel-ID
        Role role = new Role();
        role.setName("Admin");
        ApplicationUser updatedUser = new ApplicationUser();
        updatedUser.setEmail("testuser@example.com");
        updatedUser.setPassword("testPassword123");
        updatedUser.setName("TestName");
        updatedUser.setSurname("TestSurname");
        RestAssured.given().auth().basic("admin", "password")
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when().put("/users/" + userId)
                .then().statusCode(403);
    }

    @Test
    @Order(6)
    public void testGetAllUsersIsForbiddenForEveryOtherRoleThanAdmin() {
        RestAssured.given().auth().basic("", "")
                .when().get("/users")
                .then().statusCode(403);
    }

    @Test
    @Order(7)
    public void loginIsNotFoundWithNonExistingAccount() {
        Credential wrongCredential = new Credential();
        wrongCredential.setEmail("testUser@test.ch");
        wrongCredential.setPassword("password123");

        given()
                .contentType(ContentType.JSON)
                .body(wrongCredential)
                .when()
                .post("/session")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(8)
    @TestSecurity(user = "test@example.com", roles = "Admin")
    public void testNotFindingUserByIdThatDoesNotExist() {

        ApplicationUser newUser = new ApplicationUser();
        newUser.setName("Test");
        newUser.setSurname("User");
        newUser.setEmail("testuser@test.com");
        newUser.setPassword("test123");

        given()
                .pathParam("id", 1)
                .when().get("/{id}")
                .then()
                .statusCode(404);
    }
}