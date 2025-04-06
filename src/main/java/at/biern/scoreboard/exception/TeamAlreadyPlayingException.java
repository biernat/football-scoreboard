package at.biern.scoreboard.exception;

import at.biern.scoreboard.domain.Team;

public class TeamAlreadyPlayingException extends RuntimeException {
    public TeamAlreadyPlayingException(Team homeTeam, Team awayTeam) {
        super(String.format("Cannot start game (%s vs %s): at least one team is already in a live game.", homeTeam, awayTeam));
    }
}
