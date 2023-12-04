package ch.zli.m223.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import ch.zli.m223.model.Role;

@ApplicationScoped
public class RoleService {
    @Inject
    EntityManager entityManager;

    public Role findByName(String name) {
        return entityManager
                .createNamedQuery("Role.findByName", Role.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
