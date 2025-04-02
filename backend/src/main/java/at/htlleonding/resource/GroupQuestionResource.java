package at.htlleonding.resource;

import at.htlleonding.repository.GroupQuestionRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/groupquestion/")
public class GroupQuestionResource {

    @Inject
    GroupQuestionRepository groupQuestionRepository;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGroupQuestions() {
        return Response.ok(groupQuestionRepository.getAllGroupQuestions()).build();
    }
}

