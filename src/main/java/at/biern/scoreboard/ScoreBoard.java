package at.biern.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    private static final String GAME_NOT_FOUND = "Game not found.";
    private static final String GAME_ALREADY_STARTED = "One of the teams is already in a live game.";

    private final List<Game> liveGames = new ArrayList<>();

    public List<Game> getSummary() {
        return liveGames;
    }

    public void startGame(Team homeTeam, Team awayTeam) {
        if (isTeamInLiveGame(homeTeam) || isTeamInLiveGame(awayTeam)) {
            throw new IllegalArgumentException(GAME_ALREADY_STARTED);
        }
        liveGames.add(new Game(homeTeam, awayTeam));
    }

    public void finishGame(Team homeTeam, Team awayTeam) {
        Game game = findGame(homeTeam, awayTeam);
        liveGames.remove(game);
    }

    public void updateScore(Team homeTeam, Team awayTeam, int homeScore, int awayScore) {
        Game game = findGame(homeTeam, awayTeam);
        game.updateScore(homeScore, awayScore);
    }

    public boolean isTeamInLiveGame(Team team) {
        return liveGames.stream()
                .anyMatch(game -> game.isTeamInGame(team));
    }

    private Game findGame(Team homeTeam, Team awayTeam) {
        return liveGames.stream()
                .filter(game -> game.getHomeTeam() == homeTeam && game.getAwayTeam() == awayTeam)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(GAME_NOT_FOUND));
    }
}
