package at.htlleonding.dto;

import java.time.LocalDate;

public record DailyQuestionAnswerDto(Long playerId, Long answerId, Long groupId, LocalDate date) {
}
