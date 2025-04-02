package at.htlleonding.resource;

import at.htlleonding.repository.GroupQuestionAnswerRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/groupQuestionAnswer/")
public class GroupQuestionAnswerResource {

    @Inject
    GroupQuestionAnswerRepository groupQuestionAnswerRepository;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGroupQuestionAnswers() {
        return Response.ok(groupQuestionAnswerRepository.getAllGroupQuestionsAnswers()).build();
    }
}
