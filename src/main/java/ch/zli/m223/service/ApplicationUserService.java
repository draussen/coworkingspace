package ch.zli.m223.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Role;

@ApplicationScoped
public class ApplicationUserService {

    @Inject
    EntityManager entityManager;

    @Inject
    private RoleService roleService;

    @Transactional
    public ApplicationUser createUser(ApplicationUser user) {
        Role memberRole = roleService.findByName("Mitglied");
        user.setRole(memberRole);
        user.setPassword(hashPassword(user.getPassword()));

        return entityManager.merge(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        var entity = entityManager.find(ApplicationUser.class, id);
        entityManager.remove(entity);
    }

    @Transactional
    public ApplicationUser updateUser(Long id, ApplicationUser user) {
        user.setId(id);
        return entityManager.merge(user);
    }

    public List<ApplicationUser> findAll() {
        var query = entityManager.createQuery("FROM ApplicationUser", ApplicationUser.class);
        return query.getResultList();
    }

    public Optional<ApplicationUser> findByEmail(String email) {
        return entityManager
                .createNamedQuery("ApplicationUser.findByEmail", ApplicationUser.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Fehler beim Hashing des Passworts", e);
        }
    }
}
