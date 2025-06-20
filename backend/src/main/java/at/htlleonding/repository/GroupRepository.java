package at.htlleonding.repository;

import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.model.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GroupRepository {
    @Inject
    EntityManager em;

    public List<EntityGroup> getAllGroups() {
        return em.createNamedQuery(EntityGroup.GET_ALL_GROUPS, EntityGroup.class).getResultList();
    }

    public EntityGroup getGroupById(Long id) throws NotFoundException {
        EntityGroup group = em.find(EntityGroup.class, id);
        if (group == null) {
            throw new NotFoundException("Group with id " + id + " not found");
        }
        return group;
    }


    @Transactional
    public EntityGroup createGroupFromDto(GroupNameDto group) {
        EntityGroup createdGroup = new EntityGroup(group.name());

        em.persist(createdGroup);

        String link = "http://192.168.137.135:8080/api/group/join/" + createdGroup.getId();
        createdGroup.setLink(link);

        return createdGroup;
    }


    @Transactional
    public Player addPlayerToGroup(Player player, EntityGroup group) throws NotFoundException {
        player.getGroups().add(group);
        return em.merge(player);
    }
}
