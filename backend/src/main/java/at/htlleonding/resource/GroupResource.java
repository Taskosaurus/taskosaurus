package at.htlleonding.resource;

import at.htlleonding.repository.GroupRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/group/")
public class GroupResource {
    @Inject
    GroupRepository groupRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.status(400).entity(groupRepository.getAllGroups()).build();
    }
}
