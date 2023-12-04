package ch.zli.m223.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.Booking;

@ApplicationScoped
public class BookingService {

    @Inject
    EntityManager entityManager;

    @Inject
    ApplicationUserService applicationUserService;

    @Transactional
    public void createBooking(Booking booking ){
    
        booking.setStatus("Pending");
        entityManager.merge(booking);
    }

}
