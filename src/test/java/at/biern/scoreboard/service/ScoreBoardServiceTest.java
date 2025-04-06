package at.biern.scoreboard.service;

import at.biern.scoreboard.domain.Game;
import at.biern.scoreboard.domain.Team;
import at.biern.scoreboard.exception.GameNotFoundException;
import at.biern.scoreboard.exception.TeamAlreadyPlayingException;
import at.biern.scoreboard.repository.InMemoryGameRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreBoardServiceTest {

    @Test
    public void returnsEmptySummary_when_scoreboardIsEmpty() {
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
        List<Game> summary = board.getSummaryByTotalScore();

        assertThat(summary)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void returnsGameInSummary_when_gameIsStarted() {
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
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
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
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
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
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
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
        board.startGame(Team.MEXICO, Team.CANADA);

        TeamAlreadyPlayingException thrown = assertThrows(TeamAlreadyPlayingException.class,
                () -> board.startGame(Team.MEXICO, Team.GERMANY));

        assertThat(thrown.getMessage()).isEqualTo("Cannot start game (Mexico vs Germany): at least one team is already in a live game.");
    }

    @Test
    public void throwsException_when_awayTeamIsAlreadyInLiveGame_onStartingGame() {
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
        board.startGame(Team.MEXICO, Team.CANADA);

        TeamAlreadyPlayingException thrown = assertThrows(TeamAlreadyPlayingException.class,
                () -> board.startGame(Team.GERMANY, Team.CANADA));

        assertThat(thrown.getMessage()).isEqualTo("Cannot start game (Germany vs Canada): at least one team is already in a live game.");
    }

    @Test
    public void throwsException_when_gameNotFound_onFinishingGame() {
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
        board.startGame(Team.MEXICO, Team.CANADA);

        GameNotFoundException thrown = assertThrows(GameNotFoundException.class,
                () -> board.finishGame(Team.GERMANY, Team.SPAIN));


        assertThat(thrown.getMessage()).isEqualTo("Game not found for teams: Germany vs Spain.");
    }

    @Test
    public void updatesScore_when_gameExists() {
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
        board.startGame(Team.MEXICO, Team.CANADA);

        board.updateScore(Team.MEXICO, Team.CANADA, 2, 3);

        Game game = board.getSummaryByTotalScore().get(0);
        assertThat(game.getHomeScore()).isEqualTo(2);
        assertThat(game.getAwayScore()).isEqualTo(3);
    }

    @Test
    public void throwsException_when_gameNotFound_onUpdatingScore() {
        ScoreBoardService board = new ScoreBoardService(new InMemoryGameRepository());
        board.startGame(Team.MEXICO, Team.CANADA);

        assertThatThrownBy(() -> board.updateScore(Team.GERMANY, Team.FRANCE, 2, 3))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessageContaining("Game not found for teams: Germany vs France.");
    }
}
