package ch.zli.m223.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.service.ApplicationUserService;

@Path("/users")
@Tag(name = "Users", description = "Handling of users")
@RolesAllowed({ "Mitglied", "Admin" })
public class ApplicationUserController {

    @Inject
    ApplicationUserService userService;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all users.", description = "Returns a list of all users.")
    public List<ApplicationUser> index() {
        String name = securityContext.getUserPrincipal().getName();

        return userService.findAll(name);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new user. Also known as registration.", description = "Creates a new user and returns the newly added user.")
    @PermitAll
    public Response create(ApplicationUser user) {
        if (!isUserValid(user) || userService.findByEmail(user.getEmail()).isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("incorrect or incomplete registration data")
                    .build();
        }

        userService.createUser(user);

        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes an user.", description = "Deletes an user by its id.")
    public void delete(@PathParam("id") Long id) {
        String name = securityContext.getUserPrincipal().getName();

        userService.deleteUser(id, name);
    }

    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates an user.", description = "Updates an user by its id.")
    public ApplicationUser update(@PathParam("id") Long id, ApplicationUser user) {
        String name = securityContext.getUserPrincipal().getName();

        return userService.updateUser(id, user, name);
    }

    private boolean isUserValid(ApplicationUser user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return false;
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return false;
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            return false;
        }
        if (user.getSurname() == null || user.getSurname().isEmpty()) {
            return false;
        }
        return true;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find a user by ID", description = "Returns the details of a user with the specified ID.")
    public Response findUserById(@PathParam("id") Long id) {

        String name = securityContext.getUserPrincipal().getName();

        Optional<ApplicationUser> user = userService.findById(id, name);

        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

}
