package at.biern.scoreboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoard {
    private static final String GAME_NOT_FOUND = "Game not found.";
    private static final String GAME_ALREADY_STARTED = "One of the teams is already in a live game.";

    private final List<Game> liveGames = new ArrayList<>();

    public List<Game> getSummaryByTotalScore() {
        Comparator<Game> byTotalScoreAndCreationTime = Comparator
                .comparingInt((Game game) -> game.getHomeScore() + game.getAwayScore())
                .reversed()
                .thenComparing(Game::getCreatedAt, Comparator.reverseOrder());

        return liveGames.stream()
                .sorted(byTotalScoreAndCreationTime)
                .collect(Collectors.toList());
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
