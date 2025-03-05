package at.htlleonding.repository;

import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.model.Groups;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class GroupRepository {
    @Inject
    EntityManager em;

    public List<Groups> getAllGroups() {
        return em.createNamedQuery(Groups.GET_ALL_GROUPS, Groups.class).getResultList();
    }

    public Groups getGroupById(Long id) throws NotFoundException {
        Groups group = em.find(Groups.class, id);
        if (group == null) {
            throw new NotFoundException("Group with id " + id + " not found");
        }
        return group;
    }


    @Transactional
    public Groups createGroupFromDto(GroupNameDto group) {
        Groups createdGroup = new Groups(group.name());
        em.persist(createdGroup);

        String link = "/api/group/joinGroup/" + createdGroup.getId();
        createdGroup.setLink(link);

        return createdGroup;
    }
}
