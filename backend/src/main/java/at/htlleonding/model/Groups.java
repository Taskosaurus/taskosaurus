package at.htlleonding.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name= Groups.GET_ALL_GROUPS, query="SELECT g from Groups g")
public class Groups {
    public static final String GET_ALL_GROUPS = "Group.getAll";

    @Id
    @GeneratedValue
    Long id;
    String name;
    String link;

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
}
