package at.htlleonding.dto;

import at.htlleonding.model.GroupQuestionAnswer;

import java.time.LocalDate;
import java.util.List;

public record DailyQuestionResponseDto(LocalDate date, String question, List<GroupQuestionAnswerDto> answers) {
}
