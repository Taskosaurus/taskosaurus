package at.htlleonding.repository;

import at.htlleonding.model.Player;
import at.htlleonding.model.Question;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class QuestionRepository {

    @Inject
    EntityManager entityManager;

    public List<Question> getAllQuestions() {
        return entityManager.createNamedQuery(Question.GET_ALL_QUESTIONS, Question.class).getResultList();
    }

    public Player getQuestionById(Long id) throws NotFoundException {
        Player requestedQuestion = entityManager.find(Player.class, id);
        if(requestedQuestion == null) {
            throw new NotFoundException("Question with id " + id + " not found");
        }
        return requestedQuestion;
    }
}
