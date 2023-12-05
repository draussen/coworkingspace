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

import java.time.LocalDate;

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
@TestSecurity(user = "user1@example.com", roles = "Admin")
public class BookingControllerTest {

    @Test
    @Order(1)
    public void testCreateUser() {
        Role role = new Role();

        role.setName("Mitglied");

        ApplicationUser newUser = new ApplicationUser();
        newUser.setName("Test");
        newUser.setSurname("User");
        newUser.setEmail("hallo@test.com");
        newUser.setPassword("test123");
        newUser.setRole(role);

        Booking newBook = new Booking();
        newBook.setDate(LocalDate.now());
        newBook.setStatus("pending");
        newBook.setType("halber Tag");
        newBook.setUser(newUser);

        given()
                .contentType(ContentType.JSON)
                .body(newBook)
                .when()
                .post("/bookings/request")
                .then()
                .statusCode(200);
    }

}