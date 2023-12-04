package ch.zli.m223.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    public Response requestBooking(Booking booking) {
        //user einfügen
        bookingService.createBooking(booking);

        return Response.ok(booking).build();
    }
}