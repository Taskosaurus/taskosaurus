package at.htlleonding.dto;

import java.time.LocalDate;
import java.util.List;

public record DailyQuestionResponseDto(
        boolean answered,
        LocalDate date,
        String question,
        String shortenedQuestion,
        String currentLeader,
        List<GroupQuestionAnswerCollectedDto> answers
) {
}
