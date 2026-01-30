package at.htlleonding.repository;

import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.model.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class PlayerRepository {
    @Inject
    EntityManager em;

    public List<Player> getAllPlayers() {
        return em.createNamedQuery(Player.GET_ALL_PLAYERS, Player.class).getResultList();
    }

    public Player getPlayerById(Long id) throws NotFoundException {
        Player requestedPlayer = em.find(Player.class, id);
        if(requestedPlayer == null) {
            throw new NotFoundException("Player with id " + id + " not found");
        }
        return requestedPlayer;
    }

    public Player getPlayerByName(String name) {
        List<Player> players = em.createQuery("SELECT p FROM Player p WHERE p.name = :name", Player.class)
                .setParameter("name", name).getResultList();
        if (players.isEmpty()) throw new NotFoundException("Player with name " + name + " not found");
        return players.getFirst();
    }

    @Transactional
    public Player createPlayerFromDto(PlayerNameDto player) {
        Player createdPlayer = new Player(player.name());
        createdPlayer.setPassword(player.password());
        em.persist(createdPlayer);

        return createdPlayer;
    }
}
