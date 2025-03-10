package at.htlleonding.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@NamedQuery(name= EntityGroup.GET_ALL_GROUPS, query="SELECT g from EntityGroup g")
public class EntityGroup {
    public static final String GET_ALL_GROUPS = "Group.getAll";

    @Id
    @GeneratedValue
    Long id;
    String name;
    String link;

    @OneToMany(mappedBy = "group")
    @JsonIgnoreProperties({ "group" })
    List<Player> players;

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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
