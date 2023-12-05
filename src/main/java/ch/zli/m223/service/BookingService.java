package ch.zli.m223.service;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Booking;

@ApplicationScoped
public class BookingService {

    @Inject
    EntityManager entityManager;

    @Inject
    ApplicationUserService applicationUserService;

    @Transactional
    public void createBooking(Booking booking, String name) {
        Optional<ApplicationUser> user = applicationUserService.findByEmail(name);

        booking.setUser(user.get());
        booking.setStatus("Pending");
        entityManager.persist(booking);
    }

    public List<Booking> findUserBookings(String name) {
        Optional<ApplicationUser> user = applicationUserService.findByEmail(name);

        return user.get().getBookings();
    }

    public Booking findBooking(Long id, String name) {
        Optional<ApplicationUser> user = applicationUserService.findByEmail(name);

        var entity = entityManager.find(Booking.class, id);
        if (entity.getUser().getId().equals(user.get().getId()) || user.get().getRole().getName().equals("Admin")) {
            return entity;
        }

        return null;
    }

    @Transactional
    public void deleteBooking(Long id, String name) {
        Optional<ApplicationUser> user = applicationUserService.findByEmail(name);

        var entity = entityManager.find(Booking.class, id);
        if (entity.getUser().equals(user.get()) || user.get().getRole().getName().equals("Admin")) {
            entityManager.remove(entity);
        }
    }

    @Transactional
    public Booking updateBooking(Long id, Booking booking, String name) {
        Optional<ApplicationUser> user = applicationUserService.findByEmail(name);

        booking.setId(id);
        if (user.get().getRole().getName().equals("Admin")) {
            return entityManager.merge(booking);
        }

        return null;
    }
}
