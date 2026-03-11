package at.htlleonding.resource;

import at.htlleonding.dto.ErrorMessageDto;
import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.Player;
import at.htlleonding.repository.PlayerRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/player/")
public class PlayerResource {

    @Inject
    PlayerRepository playerRepository;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(playerRepository.getAllPlayers()).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PlayerNameDto player) {
        try {
            Player createdPlayer = playerRepository.createPlayerFromDto(player);
            return Response.status(Response.Status.OK).entity(createdPlayer).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessageDto("Registrierung fehlgeschlagen: " + e.getMessage())).build();
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPlayer(PlayerNameDto player) {
        // validateLogin handles both lookup and BCrypt verification
        Player validatedPlayer = playerRepository.validateLogin(player);
        if (validatedPlayer == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorMessageDto("Ungültiger Benutzername oder Passwort")).build();
        }
        return Response.status(Response.Status.OK).entity(validatedPlayer).build();
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        try {
            Player player = playerRepository.getPlayerById(id);
            return Response.status(Response.Status.OK).entity(player).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }
}
