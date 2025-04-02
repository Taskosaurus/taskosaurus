package at.htlleonding.resource;

import at.htlleonding.dto.*;
import at.htlleonding.model.*;
import at.htlleonding.repository.GroupRepository;
import at.htlleonding.repository.PlayerRepository;
import at.htlleonding.repository.QuestionRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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
    @Inject
    Request request;

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
        Player player;
        try {
            player = playerRepository.getPlayerById(request.id());
            group = (EntityGroup) groupResource.getJoinedGroup(player).getEntity();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessageDto("Player or Group couldn't be found!")).build();
        }

        Question question = questionRepository.getQuestionForDate(requestedDate, group);
        List<GroupQuestionAnswerDto> answers = questionRepository.getAnswersForQuestion(requestedDate, group).stream()
                .map(a -> new GroupQuestionAnswerDto(
                        a.getAnswer().getId(),
                        a.getAnswer().getName()
                )).toList();

        boolean hasAnswered = questionRepository.playerHasAnsweredQuestion(player, group, requestedDate);

        DailyQuestionResponseDto response = new DailyQuestionResponseDto(hasAnswered, requestedDate, question.getQuestion(), answers);

        return Response.status(Response.Status.OK).entity(response).build();
    }

    @POST
    @Path("answerDailyQuestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response answerQuestion(DailyQuestionAnswerDto answer) {
        LocalDate requestedDate = answer.date() == null ? LocalDate.now() : answer.date();

        Player answeringPlayer;
        try {
            answeringPlayer = playerRepository.getPlayerById(answer.playerId());
            Player answeredPlayer = playerRepository.getPlayerById(answer.playerId());
            questionRepository.answerQuestion(answeringPlayer, answeredPlayer, requestedDate);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessageDto("Player or Group couldn't be found!")).build();
        }

        return getQuestion(new DailyQuestionRequestDto(answeringPlayer.getId(), answeringPlayer.getName(), requestedDate));
    }
}
