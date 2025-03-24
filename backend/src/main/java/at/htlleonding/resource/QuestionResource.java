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

@Path("/api/question/")
public class QuestionResource {
    @Inject
    PlayerRepository playerRepository;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(playerRepository.getAllPlayers()).build();
    }

    @POST
    @Path("getDailyQuestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(PlayerNameDto player) {
        Player createdPlayer = playerRepository.createPlayerFromDto(player);

        return Response.status(Response.Status.OK).entity(createdPlayer).build();
    }
}
