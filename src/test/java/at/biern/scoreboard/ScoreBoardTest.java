package at.biern.scoreboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreBoardTest {

    private static final String GAME_NOT_FOUND = "Game not found.";

    @Test
    public void returnsEmptySummary_when_scoreboardIsEmpty() {
        ScoreBoard board = new ScoreBoard();
        List<Game> summary = board.getSummaryByTotalScore();

        assertThat(summary)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void returnsGameInSummary_when_gameIsStarted() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);
        List<Game> summary = board.getSummaryByTotalScore();

        assertThat(summary)
                .isNotNull()
                .hasSize(1);

        assertThat(summary.get(0))
                .isNotNull()
                .extracting("homeTeam", "awayTeam", "homeScore", "awayScore")
                .containsExactly(Team.MEXICO, Team.CANADA, 0, 0);
    }

    @Test
    public void returnsSummaryWithRemainingGames_when_oneGameIsFinished() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);
        board.startGame(Team.GERMANY, Team.FRANCE);

        assertThat(board.getSummaryByTotalScore())
                .isNotNull()
                .hasSize(2);

        board.finishGame(Team.MEXICO, Team.CANADA);

        List<Game> summary = board.getSummaryByTotalScore();

        assertThat(summary)
                .isNotNull()
                .hasSize(1);

        Game remainingGame = summary.get(0);
        assertThat(remainingGame.getHomeTeam()).isEqualTo(Team.GERMANY);
        assertThat(remainingGame.getAwayTeam()).isEqualTo(Team.FRANCE);
    }

    @Test
    public void returnsSummaryOrderedByTotalScoreAndRecency_whenMultipleGamesExist() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);
        board.updateScore(Team.MEXICO, Team.CANADA, 0, 5);
        board.startGame(Team.SPAIN, Team.BRAZIL);
        board.updateScore(Team.SPAIN, Team.BRAZIL, 10, 2);
        board.startGame(Team.GERMANY, Team.FRANCE);
        board.updateScore(Team.GERMANY, Team.FRANCE, 2, 2);
        board.startGame(Team.URUGUAY, Team.ITALY);
        board.updateScore(Team.URUGUAY, Team.ITALY, 6, 6);
        board.startGame(Team.ARGENTINA, Team.AUSTRALIA);
        board.updateScore(Team.ARGENTINA, Team.AUSTRALIA, 3, 1);

        List<Game> summary = board.getSummaryByTotalScore();

        assertThat(summary)
                .isNotNull()
                .hasSize(5)
                .extracting(Game::toString)
                .containsExactly(
                        "Uruguay 6 - Italy 6",
                        "Spain 10 - Brazil 2",
                        "Mexico 0 - Canada 5",
                        "Argentina 3 - Australia 1",
                        "Germany 2 - France 2"
                );
    }

    @Test
    public void throwsException_when_homeTeamIsAlreadyInLiveGame_onStartingGame() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> board.startGame(Team.MEXICO, Team.GERMANY));

        assertThat(thrown.getMessage()).isEqualTo("One of the teams is already in a live game.");
    }

    @Test
    public void throwsException_when_awayTeamIsAlreadyInLiveGame_onStartingGame() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> board.startGame(Team.GERMANY, Team.CANADA));

        assertThat(thrown.getMessage()).isEqualTo("One of the teams is already in a live game.");
    }

    @Test
    public void throwsException_when_gameNotFound_onFinishingGame() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> board.finishGame(Team.GERMANY, Team.SPAIN));

        assertThat(thrown.getMessage()).isEqualTo(GAME_NOT_FOUND);
    }

    @Test
    public void updatesScore_when_gameExists() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);

        board.updateScore(Team.MEXICO, Team.CANADA, 2, 3);

        Game game = board.getSummaryByTotalScore().get(0);
        assertThat(game.getHomeScore()).isEqualTo(2);
        assertThat(game.getAwayScore()).isEqualTo(3);
    }

    @Test
    public void throwsException_when_gameNotFound_onUpdatingScore() {
        ScoreBoard board = new ScoreBoard();
        board.startGame(Team.MEXICO, Team.CANADA);

        assertThatThrownBy(() -> board.updateScore(Team.GERMANY, Team.FRANCE, 2, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(GAME_NOT_FOUND);
    }
}
