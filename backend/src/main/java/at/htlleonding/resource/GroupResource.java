package at.htlleonding.resource;

import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.Player;
import at.htlleonding.repository.GroupRepository;
import at.htlleonding.model.Groups;
import at.htlleonding.repository.PlayerRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.swing.*;

@Path("/api/group/")
public class GroupResource {
    @Inject
    GroupRepository groupRepository;
    @Inject
    PlayerRepository playerRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.status(400).entity(groupRepository.getAllGroups()).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(GroupNameDto group) {
        Groups createdGroup = groupRepository.createGroupFromDto(group);

        return Response.status(Response.Status.OK).entity(createdGroup).build();
    }

    @POST
    @Path("joinGroup/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinGroup(PlayerNameDto player, @PathParam("id") Long id) {
        try {
            Groups group = groupRepository.getGroupById(id);
            Player createdPlayer = playerRepository.createPlayerFromDto(player, group);
            //createdPlayer.setGroup(group);

            return Response.status(Response.Status.OK).entity(createdPlayer).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
