package at.biern.scoreboard.exception;

import at.biern.scoreboard.domain.Team;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Team homeTeam, Team awayTeam) {
        super(String.format("Game not found for teams: %s vs %s.", homeTeam, awayTeam));
    }
}
