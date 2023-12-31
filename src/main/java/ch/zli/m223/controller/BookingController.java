package ch.zli.m223.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.model.Booking;
import ch.zli.m223.service.ApplicationUserService;
import ch.zli.m223.service.BookingService;

@Path("/bookings")
@Tag(name = "Bookings", description = "Handling of bookings")
@RolesAllowed({ "Mitglied", "Admin" })
public class BookingController {

    @Inject
    BookingService bookingService;

    @Inject
    ApplicationUserService applicationUserService;

    @Context
    SecurityContext securityContext;

    @POST
    @Path("/request")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Request a new booking.", description = "Requests a new booking and returns the request status.")
    public Response requestBooking(@Valid Booking booking) {
        // user einfügen
        String name = securityContext.getUserPrincipal().getName();

        bookingService.createBooking(booking, name);

        return Response.ok(booking).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all users bookings.", description = "Returns a list of all users bookings.")
    public List<Booking> index() {
        String name = securityContext.getUserPrincipal().getName();
        return bookingService.findUserBookings(name);
    }

    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes a booking.", description = "Deletes a booking by its id.")
    public void delete(@PathParam("id") Long id) {
        String name = securityContext.getUserPrincipal().getName();
        bookingService.deleteBooking(id, name);
    }

    @Path("/status/{id}")
    @GET
    @Operation(summary = "Gets a booking.", description = "Gets a booking by its id.")
    public Booking findBooking(@PathParam("id") Long id) {
        String name = securityContext.getUserPrincipal().getName();
        return bookingService.findBooking(id, name);
    }

    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates a booking.", description = "Updates a booking by its id.")
    public Booking update(@PathParam("id") Long id, @Valid Booking booking) {
        String name = securityContext.getUserPrincipal().getName();

        return bookingService.updateBooking(id, booking, name);
    }
}