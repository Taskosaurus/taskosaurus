package at.htlleonding.model;

import jakarta.persistence.*;

@NamedQuery(name= GroupQuestionAnswer.GET_ALL_GROUPQUESTIONANSWER, query="SELECT gqa from GroupQuestionAnswer gqa")
@Entity

public class GroupQuestionAnswer {

    public static final String GET_ALL_GROUPQUESTIONANSWER = "GroupQuestionAnswer.getAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "answering_player_id")
    private Player answeringPlayer;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "group_question_id")
    private GroupQuestion groupQuestion;

    public GroupQuestionAnswer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getAnsweringPlayer() {
        return answeringPlayer;
    }

    public void setAnsweringPlayer(Player answeringPlayer) {
        this.answeringPlayer = answeringPlayer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public GroupQuestion getGroupQuestion() {
        return groupQuestion;
    }

    public void setGroupQuestion(GroupQuestion groupQuestion) {
        this.groupQuestion = groupQuestion;
    }
}
