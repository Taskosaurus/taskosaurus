package at.htlleonding.repository;

import at.htlleonding.model.Groups;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class GroupRepository {
    @Inject
    private EntityManager em;

    public List<Groups> getAllGroups() {
        return em.createNamedQuery(Groups.GET_ALL_GROUPS, Groups.class).getResultList();
    }
}
