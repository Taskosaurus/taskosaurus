package at.htlleonding.resource;

import at.htlleonding.dto.ErrorMessageDto;
import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.Player;
import at.htlleonding.repository.GroupRepository;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.repository.PlayerRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.LinkedList;
import java.util.List;

@Path("/api/group/")
public class GroupResource {
    @Inject
    GroupRepository groupRepository;
    @Inject
    PlayerRepository playerRepository;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.status(200).entity(groupRepository.getAllGroups()).build();
    }

    @POST
    @Path("create/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Player player, @PathParam("name") String name) {
        try {
            Player validatedPlayer = playerRepository.getPlayerById(player.getId());
            EntityGroup createdGroup = groupRepository.createGroupFromDto(new GroupNameDto(name));

            if (!createdGroup.getPlayers().contains(validatedPlayer)) {
                groupRepository.addPlayerToGroup(validatedPlayer, createdGroup);
            }

            return Response.status(Response.Status.OK).entity(createdGroup).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessageDto(e.getMessage())).build();
        }

    }

    @GET
    @Path("join/{id}/{playerName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response joinGroup(@PathParam("id") Long id,  @PathParam("playerName") String playerName) {
        try {
            EntityGroup validatedGroup = groupRepository.getGroupById(id);
            Player validatedPlayer = playerRepository.getPlayerByName(playerName);
            if(validatedGroup.getPlayers().contains(validatedPlayer)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessageDto("Player already in group!")).build();
            }
            Player mergedPlayer = groupRepository.addPlayerToGroup(validatedPlayer, validatedGroup);

            return Response.status(Response.Status.OK).entity(mergedPlayer).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }

    @POST
    @Path("getJoinedGroups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJoinedGroups(Player player) {
        try {
            Player validatedPlayer = playerRepository.getPlayerById(player.getId());

            return Response.status(Response.Status.OK).entity(validatedPlayer.getGroups()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }
}
