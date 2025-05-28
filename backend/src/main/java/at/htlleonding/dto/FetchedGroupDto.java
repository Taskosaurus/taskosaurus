package at.htlleonding.dto;

import at.htlleonding.model.Player;

import java.util.List;

public record FetchedGroupDto(Long id, String name, String link, List<Player> players, Long count, boolean answered) {
}
