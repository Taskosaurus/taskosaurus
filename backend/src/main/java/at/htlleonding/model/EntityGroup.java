package at.htlleonding.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity(name="GroupEntity")
@NamedQuery(name= EntityGroup.GET_ALL_GROUPS, query="SELECT g from GroupEntity g ORDER BY g.name")
public class EntityGroup {
    public static final String GET_ALL_GROUPS = "Group.getAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String link;

    @ManyToMany(mappedBy = "groups")
    @JsonIgnoreProperties({ "groups" })
    @OrderBy("name ASC")
    Set<Player> players = new LinkedHashSet<>();

    public EntityGroup(String name) {
        this.name = name;
    }

    public EntityGroup() {
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
