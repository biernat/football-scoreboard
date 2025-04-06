package at.biern.scoreboard;

import java.time.Instant;

public class Game {
    private static final String SCORES_MUST_BE_NON_NEGATIVE = "Scores must be non-negative.";

    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final Instant createdAt;

    public Game(String homeTeam, String awayTeam) {
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

    public boolean isTeamInGame(String team) {
        return homeTeam.equals(team) || awayTeam.equals(team);
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
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
