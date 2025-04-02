package at.htlleonding.resource;

import at.htlleonding.dto.DailyQuestionRequestDto;
import at.htlleonding.dto.ErrorMessageDto;
import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.model.Player;
import at.htlleonding.model.Question;
import at.htlleonding.repository.GroupRepository;
import at.htlleonding.repository.PlayerRepository;
import at.htlleonding.repository.QuestionRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Path("/api/question/")
public class QuestionResource {
    @Inject
    QuestionRepository questionRepository;
    @Inject
    PlayerRepository playerRepository;
    @Inject
    GroupRepository groupRepository;
    @Inject
    GroupResource groupResource;

    @POST
    @Path("getDailyQuestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion(DailyQuestionRequestDto request) {
        LocalDate requestedDate = request.date() == null ? LocalDate.now() : request.date();

        if (requestedDate.isAfter(LocalDate.now())) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessageDto("Date can't be later than today!")).build();
        }

        EntityGroup group;
        try {
            Player player = playerRepository.getPlayerById(request.id());
            group = (EntityGroup) groupResource.getJoinedGroup(player).getEntity();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessageDto("Player or Group couldn't be found!")).build();
        }

        Question question = questionRepository.getQuestionForDate(requestedDate, group);

        /*
         * TODO: get question from database and return it
         * TODO: also get the answers of the question that are already present
         * TODO: if date is today and there is no entry for it yet, get a random question that hasn't been answered yet
         * TODO: next to questions and answers, also return if the user has already answered this question
          */

        return Response.status(Response.Status.OK).entity(question).build();
    }

    @POST
    @Path("answerDailyQuestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response answerQuestion(DailyQuestionRequestDto request) {
        LocalDate requestedDate = request.date() == null ? LocalDate.now() : request.date();

        /*
         * TODO: check if there is a question for the group at the requested date
         */

        String question = "Successful! Localdate: " + requestedDate;

        /*
         * TODO: create entry in the question answers table
         * TODO: return the new answers that are already present for this question
         */

        return Response.status(Response.Status.OK).entity(question).build();
    }
}
