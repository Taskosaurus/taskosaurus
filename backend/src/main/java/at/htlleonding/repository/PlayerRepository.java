package at.htlleonding.repository;

import at.htlleonding.dto.PlayerNameDto;
import at.htlleonding.model.EntityGroup;
import at.htlleonding.model.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
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

    @Transactional
    public Player createPlayerFromDto(PlayerNameDto player) throws NotFoundException {
        List<Player> playersWithSameName = em.createQuery(
                        "SELECT p FROM Player p WHERE p.name = :name", Player.class)
                .setParameter("name", player.name())
                .getResultList();

        if (!playersWithSameName.isEmpty()) {
            throw new IllegalArgumentException("Player with name " + player.name() + " already exists");
        }

        try {
            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(player.password(), salt);

            Player createdPlayer = new Player(player.name(), hashedPassword);
            em.persist(createdPlayer);

            return createdPlayer;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    @Transactional
    public Player login(PlayerNameDto loginDto) {
        List<Player> players = em.createQuery(
                        "SELECT p FROM Player p LEFT JOIN FETCH p.groups WHERE p.name = :name", Player.class)
                .setParameter("name", loginDto.name())
                .getResultList();

        if (players.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        Player player = players.get(0);

        try {
            if (!verifyPassword(loginDto.password(), player.getPassword())) {
                throw new IllegalArgumentException("Invalid username or password");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password verification failed", e);
        }

        return player;
    }


    public static String hashPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String stored)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = stored.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String hashToCompare = hashPassword(password, salt);
        return stored.equals(hashToCompare);
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
