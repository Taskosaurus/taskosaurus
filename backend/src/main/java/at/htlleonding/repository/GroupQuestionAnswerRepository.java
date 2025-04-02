package at.htlleonding.repository;

import at.htlleonding.model.GroupQuestionAnswer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class GroupQuestionAnswerRepository {

    @Inject
    EntityManager entityManager;

    public List<GroupQuestionAnswer> getAllGroupQuestionsAnswers() {
        return entityManager.createNamedQuery(GroupQuestionAnswer.GET_ALL_GROUPQUESTIONANSWER, GroupQuestionAnswer.class).getResultList();
    }
}
