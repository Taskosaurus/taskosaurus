package at.htlleonding.dto;

import java.time.LocalDate;

public record DailyQuestionAnswerDto(Long playerId, Long answerId, LocalDate date) {
}
