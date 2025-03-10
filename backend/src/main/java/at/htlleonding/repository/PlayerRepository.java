package at.htlleonding.repository;

import at.htlleonding.dto.GroupNameDto;
import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.Groups;
import at.htlleonding.model.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PlayerRepository {
    @Inject
    EntityManager em;

    public List<Player> getAllPlayers() {
        return em.createNamedQuery(Player.GET_ALL_PLAYERS, Player.class).getResultList();
    }

    @Transactional
    public Player createPlayerFromDto(PlayerNameDto player, Groups group) {
        Player createdPlayer = new Player(player.name());
        createdPlayer.setGroup(group);
        em.persist(createdPlayer);

        return createdPlayer;
    }
}
