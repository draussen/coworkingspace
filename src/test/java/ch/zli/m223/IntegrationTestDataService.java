package ch.zli.m223;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.ApplicationUser;
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

    Role adminRole = new Role();
    adminRole.setName("Admin");

    Role userRole = new Role();
    userRole.setName("Mitglied");

    ApplicationUser user1 = new ApplicationUser();
    user1.setEmail("user1@example.com");
    user1.setPassword("password1");
    user1.setName("John");
    user1.setSurname("Doe");
    user1.setRole(adminRole);

    ApplicationUser user2 = new ApplicationUser();
    user2.setEmail("user2@example.com");
    user2.setPassword("password2");
    user2.setName("Jane");
    user2.setSurname("Smith");
    user2.setRole(userRole);

  }
}
