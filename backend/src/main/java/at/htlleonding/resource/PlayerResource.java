package at.htlleonding.resource;

import at.htlleonding.dto.ErrorMessageDto;
import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.model.Player;
import at.htlleonding.repository.GroupRepository;
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
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.OK).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(PlayerNameDto player) {
        try {
            Player loggedInPlayer = playerRepository.login(player);
            return Response.status(Response.Status.OK).entity(loggedInPlayer).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.OK).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Player player = playerRepository.getPlayerById(id);

        return Response.status(Response.Status.OK).entity(player).build();
    }
}
