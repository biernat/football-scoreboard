package at.biern.scoreboard.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class GameTest {

    private static final String SCORES_MUST_BE_NON_NEGATIVE = "Scores must be non-negative.";

    @Test
    public void updatesScore_when_validScoresAreGiven() {
        Game game = new Game(Team.MEXICO, Team.CANADA);

        game.updateScore(2, 3);

        assertThat(game.getHomeScore()).isEqualTo(2);
        assertThat(game.getAwayScore()).isEqualTo(3);
    }

    @Test
    public void throwsException_when_negativeHomeScoreIsGiven() {
        Game game = new Game(Team.MEXICO, Team.CANADA);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.updateScore(-1, 3))
                .withMessage(SCORES_MUST_BE_NON_NEGATIVE);
    }

    @Test
    public void throwsException_when_negativeAwayScoreIsGiven() {
        Game game = new Game(Team.MEXICO, Team.CANADA);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.updateScore(2, -1))
                .withMessage(SCORES_MUST_BE_NON_NEGATIVE);
    }

    @Test
    public void throwsException_when_negativeScoresAreGiven() {
        Game game = new Game(Team.MEXICO, Team.CANADA);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.updateScore(-1, -1))
                .withMessage(SCORES_MUST_BE_NON_NEGATIVE);
    }

    @Test
    public void returnsCorrectStringRepresentation_when_toStringIsCalled() {
        Game game = new Game(Team.MEXICO, Team.CANADA);
        game.updateScore(2, 3);

        String result = game.toString();

        assertThat(result).isEqualTo("Mexico 2 - Canada 3");
    }
}
