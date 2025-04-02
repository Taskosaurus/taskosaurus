package at.htlleonding.model;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDate;

@NamedQuery(name= GroupQuestion.GET_ALL_GROUPQUESTIONS, query="SELECT gq from GroupQuestion gq")
@Entity

public class GroupQuestion {

    public static final String GET_ALL_GROUPQUESTIONS = "GroupQuestion.getAll";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private EntityGroup group;

    private LocalDate date;

    @OneToMany(mappedBy = "groupQuestion", cascade = CascadeType.ALL)
    private List<GroupQuestionAnswer> answers;

    public GroupQuestion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public EntityGroup getGroup() {
        return group;
    }

    public void setGroup(EntityGroup group) {
        this.group = group;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<GroupQuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<GroupQuestionAnswer> answers) {
        this.answers = answers;
    }
}
