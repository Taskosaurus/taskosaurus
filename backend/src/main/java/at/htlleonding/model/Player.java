package at.htlleonding.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@NamedQuery(name= Player.GET_ALL_PLAYERS, query="SELECT p from Player p")
public class Player {
    public static final String GET_ALL_PLAYERS = "Player.getAll";

    @Id
    @GeneratedValue
    Long id;
    String name;

    @ManyToOne
    @JoinColumn(name = "groupId")
    @JsonIgnoreProperties({ "players" })
    Groups group;

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

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
}
