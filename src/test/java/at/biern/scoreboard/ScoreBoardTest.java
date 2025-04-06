package at.biern.scoreboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreBoardTest {

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
        board.startGame("Mexico", "Canada");
        List<Game> summary = board.getSummary();

        assertThat(summary)
                .isNotNull()
                .hasSize(1);

        assertThat(summary.get(0))
                .isNotNull()
                .extracting("homeTeam", "awayTeam", "homeScore", "awayScore")
                .containsExactly("Mexico", "Canada", 0, 0);
    }

    @Test
    public void returnsSummaryWithRemainingGames_when_oneGameIsFinished() {
        ScoreBoard board = new ScoreBoard();
        board.startGame("Mexico", "Canada");
        board.startGame("Germany", "France");

        assertThat(board.getSummary())
                .isNotNull()
                .hasSize(2);

        board.finishGame("Mexico", "Canada");

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
        board.startGame("Mexico", "Canada");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.startGame("Mexico", "Germany"));

        assertThat(thrown.getMessage()).isEqualTo("One of the teams is already in a live game.");
    }

    @Test
    public void throwsException_when_gameNotFound_onFinishingGame() {
        ScoreBoard board = new ScoreBoard();
        board.startGame("Mexico", "Canada");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> board.finishGame("Germany", "Spain"));

        assertThat(thrown.getMessage()).isEqualTo("Game not found.");
    }
}
