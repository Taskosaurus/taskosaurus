package at.htlleonding.repository;

import at.htlleonding.model.GroupQuestion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class GroupQuestionRepository {

    @Inject
    EntityManager entityManager;

    public List<GroupQuestion> getAllGroupQuestions() {
        return entityManager.createNamedQuery(GroupQuestion.GET_ALL_GROUPQUESTIONS, GroupQuestion.class).getResultList();
    }
}
