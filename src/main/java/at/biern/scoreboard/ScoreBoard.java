package at.biern.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    public static final String GAME_NOT_FOUND = "Game not found.";
    public static final String GAME_ALREADY_STARTED = "One of the teams is already in a live game.";

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
        if (game == null) {
            throw new IllegalArgumentException(GAME_NOT_FOUND);
        }
        liveGames.remove(game);
    }

    public boolean isTeamInLiveGame(String team) {
        return liveGames.stream()
                .anyMatch(game -> game.isTeamInGame(team));
    }

    private Game findGame(String homeTeam, String awayTeam) {
        return liveGames.stream()
                .filter(game -> game.getHomeTeam().equals(homeTeam) && game.getAwayTeam().equals(awayTeam))
                .findFirst()
                .orElse(null);
    }
}
