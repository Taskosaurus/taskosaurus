package at.htlleonding.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties({"groupQuestions"})
    private Question question;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnoreProperties({"groupQuestions"})
    private EntityGroup group;

    private LocalDate date;

    @OneToMany(mappedBy = "groupQuestion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"groupQuestion"})
    private List<GroupQuestionAnswer> answers;

    public GroupQuestion() {
    }

    public GroupQuestion(Question question, EntityGroup group, LocalDate date) {
        this.question = question;
        this.group = group;
        this.date = date;
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
