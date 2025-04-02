package at.htlleonding.repository;

import at.htlleonding.model.EntityGroup;
import at.htlleonding.model.GroupQuestion;
import at.htlleonding.model.Player;
import at.htlleonding.model.Question;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class QuestionRepository {

    @Inject
    EntityManager entityManager;

    public List<Question> getAllQuestions() {
        return entityManager.createNamedQuery(Question.GET_ALL_QUESTIONS, Question.class).getResultList();
    }

    @Transactional
    public Question getQuestionForDate(LocalDate date, EntityGroup group) {
        List<Question> questions = entityManager.createQuery(
                        "SELECT q FROM Question q " +
                                "JOIN GroupQuestion gq ON q.id = gq.question.id " +
                                "WHERE gq.group = :group " +
                                "AND gq.date = :date", Question.class)
                .setParameter("group", group)
                .setParameter("date", date)
                .getResultList();

        return questions.isEmpty() ? createRandomQuestionForGroup(group, date) : questions.getFirst();
    }


    private Question createRandomQuestionForGroup(EntityGroup group, LocalDate date) {
        List<Question> possibleQuestions = entityManager.createQuery("SELECT q " +
                "FROM Question q " +
                "WHERE q NOT IN (" +
                "    SELECT gq.question " +
                "    FROM GroupQuestion gq " +
                "    WHERE gq.group = :group" +
                ")", Question.class).setParameter("group", group).getResultList();

        System.out.println(possibleQuestions);

        int randomIndex = (int) (Math.random() * possibleQuestions.size());
        createGroupQuestion(group, possibleQuestions.get(randomIndex), date);
        return possibleQuestions.get(randomIndex);
    }

    private void createGroupQuestion(EntityGroup group, Question question, LocalDate date) {
        GroupQuestion groupQuestion = new GroupQuestion(question, group, date);
        entityManager.persist(groupQuestion);
    }

    public Question getQuestionById(Long id) throws NotFoundException {
        Question requestedQuestion = entityManager.find(Question.class, id);
        if(requestedQuestion == null) {
            throw new NotFoundException("Question with id " + id + " not found");
        }
        return requestedQuestion;
    }
}
