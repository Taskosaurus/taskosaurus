package at.htlleonding.model;

import jakarta.persistence.*;
import java.util.List;

@NamedQuery(name= Question.GET_ALL_QUESTIONS, query="SELECT q from Question q")
@Entity

public class Question {

    public static final String GET_ALL_QUESTIONS = "Question.getAll";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<GroupQuestion> groupQuestions;

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<GroupQuestion> getGroupQuestions() {
        return groupQuestions;
    }

    public void setGroupQuestions(List<GroupQuestion> groupQuestions) {
        this.groupQuestions = groupQuestions;
    }
}
