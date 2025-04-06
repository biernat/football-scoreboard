package at.biern.scoreboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreBoardTest {

    private static final String GAME_NOT_FOUND = "Game not found.";
    private static final String HOME_TEAM = "Mexico";
    private static final String AWAY_TEAM = "Canada";

    @Test
    public void returnsEmptySummary_when_scoreboardIsEmpty() {
        ScoreBoard board = new ScoreBoard();
        List<Game> summary = board.getSummary();

        assertThat(summary)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void returnsGameInSummary_when_gameIsStarted() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(HOME_TEAM, AWAY_TEAM);
        List<Game> summary = board.getSummary();

        assertThat(summary)
                .isNotNull()
                .hasSize(1);

        assertThat(summary.get(0))
                .isNotNull()
                .extracting("homeTeam", "awayTeam", "homeScore", "awayScore")
                .containsExactly(HOME_TEAM, AWAY_TEAM, 0, 0);
    }

    @Test
    public void returnsSummaryWithRemainingGames_when_oneGameIsFinished() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(HOME_TEAM, AWAY_TEAM);
        board.startGame("Germany", "France");

        assertThat(board.getSummary())
                .isNotNull()
                .hasSize(2);

        board.finishGame(HOME_TEAM, AWAY_TEAM);

        List<Game> summary = board.getSummary();

        assertThat(summary)
                .isNotNull()
                .hasSize(1);

        Game remainingGame = summary.get(0);
        assertThat(remainingGame.getHomeTeam()).isEqualTo("Germany");
        assertThat(remainingGame.getAwayTeam()).isEqualTo("France");
    }

    @Test
    public void throwsException_when_teamIsAlreadyInLiveGame_onStartingGame() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(HOME_TEAM, AWAY_TEAM);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.startGame(HOME_TEAM, "Germany"));

        assertThat(thrown.getMessage()).isEqualTo("One of the teams is already in a live game.");
    }

    @Test
    public void throwsException_when_gameNotFound_onFinishingGame() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(HOME_TEAM, AWAY_TEAM);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.finishGame("Germany", "Spain"));

        assertThat(thrown.getMessage()).isEqualTo(GAME_NOT_FOUND);
    }

    @Test
    public void updatesScore_when_gameExists() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(HOME_TEAM, AWAY_TEAM);

        board.updateScore(HOME_TEAM, AWAY_TEAM, 2, 3);

        Game game = board.getSummary().get(0);
        assertThat(game.getHomeScore()).isEqualTo(2);
        assertThat(game.getAwayScore()).isEqualTo(3);
    }

    @Test
    public void throwsException_when_gameNotFound_onUpdatingScore() {
        ScoreBoard board = new ScoreBoard();
        board.startGame("Mexico", "Canada");

        assertThatThrownBy(() -> board.updateScore("Germany", "France", 2, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(GAME_NOT_FOUND);
    }
}
