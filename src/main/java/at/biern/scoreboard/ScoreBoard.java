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

    public void startGame(String homeTeam, String awayTeam) {
        if (isTeamInLiveGame(homeTeam) || isTeamInLiveGame(awayTeam)) {
            throw new IllegalArgumentException(GAME_ALREADY_STARTED);
        }
        liveGames.add(new Game(homeTeam, awayTeam));
    }

    public void finishGame(String homeTeam, String awayTeam) {
        Game game = findGame(homeTeam, awayTeam);
        liveGames.remove(game);
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Game game = findGame(homeTeam, awayTeam);
        game.updateScore(homeScore, awayScore);
    }

    public boolean isTeamInLiveGame(String team) {
        return liveGames.stream()
                .anyMatch(game -> game.isTeamInGame(team));
    }

    private Game findGame(String homeTeam, String awayTeam) {
        return liveGames.stream()
                .filter(game -> game.getHomeTeam().equals(homeTeam) && game.getAwayTeam().equals(awayTeam))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(GAME_NOT_FOUND));
    }
}
