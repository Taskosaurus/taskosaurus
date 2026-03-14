package at.htlleonding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@NamedQuery(name= Player.GET_ALL_PLAYERS, query="SELECT p from Player p")
public class Player {
    public static final String GET_ALL_PLAYERS = "Player.getAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    @JsonIgnore
    String password;

    @ManyToMany
    @JoinTable(
            name="player_group",
            joinColumns = @JoinColumn(name="player_id"),
            inverseJoinColumns = @JoinColumn(name="group_id")
    )
    @JsonIgnoreProperties({ "players" })
    Set<EntityGroup> groups;

    public Player(String name) {
        this.name = name;
    }

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EntityGroup> getGroups() {
        return groups;
    }

    public void setGroup(Set<EntityGroup> group) {
        this.groups = group;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
