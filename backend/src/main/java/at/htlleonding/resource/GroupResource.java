package at.htlleonding.resource;

import at.htlleonding.dto.ErrorMessageDto;
import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.Player;
import at.htlleonding.repository.GroupRepository;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.repository.PlayerRepository;
import jakarta.inject.Inject;
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
        return Response.status(400).entity(groupRepository.getAllGroups()).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(GroupNameDto group) {
        EntityGroup createdGroup = groupRepository.createGroupFromDto(group);

        return Response.status(Response.Status.OK).entity(createdGroup).build();
    }

    @POST
    @Path("join/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinGroup(Player player, @PathParam("id") Long id) {
        try {
            EntityGroup validatedGroup = groupRepository.getGroupById(id);
            Player validatedPlayer = playerRepository.getPlayerById(player.getId());
            Player mergedPlayer = groupRepository.addPlayerToGroup(validatedPlayer, validatedGroup);

            return Response.status(Response.Status.OK).entity(mergedPlayer).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }

    @POST
    @Path("getJoinedGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJoinedGroup(Player player) {
        try {
            Player validatedPlayer = playerRepository.getPlayerById(player.getId());
            EntityGroup validatedGroup = groupRepository.getGroupById(validatedPlayer.getGroup().getId());

            return Response.status(Response.Status.OK).entity(validatedGroup).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }

    @POST
    @Path("getJoinedGroups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJoinedGroups(Player[] players) {
        try {
            List<EntityGroup> groups = new LinkedList<>();

            for (Player player : players) {
                Player validatedPlayer = playerRepository.getPlayerById(player.getId());
                EntityGroup validatedGroup = groupRepository.getGroupById(validatedPlayer.getGroup().getId());
                groups.add(validatedGroup);
            }
            
            return Response.status(Response.Status.OK).entity(groups).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }
}
