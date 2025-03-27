package at.htlleonding.dto;

import java.time.LocalDate;

public record DailyQuestionRequestDto(Long id, String name, LocalDate date) {
}
