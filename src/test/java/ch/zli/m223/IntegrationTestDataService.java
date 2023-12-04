package ch.zli.m223;

import java.time.LocalDate;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Booking;
import ch.zli.m223.model.Role;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;

@IfBuildProfile("test")
@ApplicationScoped
public class IntegrationTestDataService {

  @Inject
  EntityManager entityManager;

  @Transactional
  void generateTestData(@Observes StartupEvent event) {

    // Create roles
    var roleMember = new Role();
    roleMember.setName("Mitglied");
    entityManager.persist(roleMember);

    var roleAdmin = new Role();
    roleAdmin.setName("Admin");
    entityManager.persist(roleAdmin);

    var roleVisitor = new Role();
    roleVisitor.setName("Besucher");
    entityManager.persist(roleVisitor);

    // Bookings
    var bookingA = new Booking();
    bookingA.setDate(LocalDate.now().plusDays(5));
    bookingA.setStatus("angenommen");
    bookingA.setType("halber Tag");
    entityManager.persist(bookingA);

    var bookingB = new Booking();
    bookingA.setDate(LocalDate.now().plusDays(2));
    bookingA.setStatus("offen");
    bookingA.setType("ganzer Tag");
    entityManager.persist(bookingB);

    var bookingC = new Booking();
    bookingA.setDate(LocalDate.now().plusDays(10));
    bookingA.setStatus("abgelehnt");
    bookingA.setType("ganzer Tag");
    entityManager.persist(bookingC);

    // ApplicationUser
    var user1 = new ApplicationUser();
    user1.setEmail("user1@example.com");
    user1.setPassword("password1");
    user1.setName("John");
    user1.setSurname("Doe");
    user1.setRole(roleMember);
    user1.setBookings(Arrays.asList(bookingA, bookingB));
    entityManager.persist(user1);

    var user2 = new ApplicationUser();
    user2.setEmail("user2@example.com");
    user2.setPassword("password2");
    user2.setName("Jane");
    user2.setSurname("Smith");
    user2.setRole(roleAdmin);
    user2.setBookings(Arrays.asList(bookingC));
    entityManager.persist(user2);

  }
}
