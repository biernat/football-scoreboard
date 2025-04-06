package at.biern.scoreboard;

import java.time.Instant;

public class Game {
    private static final String SCORES_MUST_BE_NON_NEGATIVE = "Scores must be non-negative.";

    private final Team homeTeam;
    private final Team awayTeam;
    private int homeScore;
    private int awayScore;
    private final Instant createdAt;

    public Game(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.createdAt = Instant.now();
    }

    public void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException(SCORES_MUST_BE_NON_NEGATIVE);
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public boolean isTeamInGame(Team team) {
        return homeTeam == team || awayTeam == team;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("%s %d - %s %d", homeTeam, homeScore, awayTeam, awayScore);
    }

}
