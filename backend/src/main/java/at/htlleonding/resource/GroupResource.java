package at.htlleonding.resource;

import at.htlleonding.dto.*;
import at.htlleonding.model.Player;
import at.htlleonding.repository.GroupRepository;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.repository.PlayerRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.annotations.Fetch;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Path("/api/group/")
public class GroupResource {
    @Inject
    GroupRepository groupRepository;
    @Inject
    PlayerRepository playerRepository;
    @Inject
    QuestionResource questionResource;

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
        try {
            Player player = playerRepository.getPlayerById(group.playerId());
            EntityGroup createdGroup = groupRepository.createGroupFromDto(group, player);
            return Response.status(Response.Status.OK).entity(createdGroup).build();
        } catch (NotFoundException e) {
            return Response.status(400).entity(new ErrorMessageDto(e.getMessage())).build();
        }

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
    @Path("getJoinedGroups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJoinedGroups(Player player) {
        try {
            Player validatedPlayer = playerRepository.getPlayerById(player.getId());
            List<EntityGroup> groups = validatedPlayer.getGroups();

            List<FetchedGroupDto> fetchedGroups = new LinkedList<>();
            for (EntityGroup group : groups) {
                DailyQuestionRequestDto request = new DailyQuestionRequestDto(validatedPlayer.getId(), validatedPlayer.getName(),
                        group.getId(), LocalDate.now());
                DailyQuestionResponseDto response = (DailyQuestionResponseDto) questionResource.getQuestion(request).getEntity();
                fetchedGroups.add(new FetchedGroupDto(group.getId(), group.getName(), group.getLink(), group.getPlayers(),
                        response.answers().size(), response.answered()));
            }

            return Response.status(Response.Status.OK).entity(fetchedGroups).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessageDto(e.getMessage())).build();
        }
    }
}
