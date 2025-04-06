package at.biern.scoreboard.service;

import at.biern.scoreboard.domain.Game;
import at.biern.scoreboard.domain.Team;
import at.biern.scoreboard.exception.GameNotFoundException;
import at.biern.scoreboard.exception.TeamAlreadyPlayingException;
import at.biern.scoreboard.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ScoreBoardServiceTest {

    private GameRepository mockGameRepository;
    private ScoreBoardService scoreBoardService;

    @BeforeEach
    void setUp() {
        mockGameRepository = mock(GameRepository.class);
        scoreBoardService = new ScoreBoardService(mockGameRepository);
    }

    @Test
    public void returnsEmptySummary_when_gameRepositoryIsEmpty() {
        when(mockGameRepository.findAll()).thenReturn(List.of());

        List<Game> summary = scoreBoardService.getSummaryByTotalScore();

        assertThat(summary)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void returnsSortedSummary_when_gamesExist() {
        Game game1 = new Game(Team.MEXICO, Team.CANADA);
        game1.updateScore(2, 3);
        Game game2 = new Game(Team.BRAZIL, Team.SPAIN);
        game2.updateScore(10, 4);

        when(mockGameRepository.findAll()).thenReturn(List.of(game1, game2));

        List<Game> summary = scoreBoardService.getSummaryByTotalScore();

        assertThat(summary)
                .hasSize(2)
                .extracting(Game::toString)
                .containsExactly("Brazil 10 - Spain 4", "Mexico 2 - Canada 3");
    }

    @Test
    public void throwsException_when_homeTeamIsAlreadyInLiveGame_onStartingGame() {
        when(mockGameRepository.findAll()).thenReturn(List.of(new Game(Team.MEXICO, Team.CANADA)));

        assertThatThrownBy(() -> scoreBoardService.startGame(Team.MEXICO, Team.GERMANY))
                .isInstanceOf(TeamAlreadyPlayingException.class)
                .hasMessageContaining("Cannot start game (Mexico vs Germany): at least one team is already in a live game.");

        verify(mockGameRepository, times(1)).findAll();
    }

    @Test
    public void throwsException_when_gameNotFound_onFinishingGame() {
        when(mockGameRepository.find(Team.MEXICO, Team.CANADA)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scoreBoardService.finishGame(Team.MEXICO, Team.CANADA))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessageContaining("Game not found for teams: Mexico vs Canada.");
    }

    @Test
    public void removesGame_when_gameExists_onFinishingGame() {
        Team homeTeam = Team.MEXICO;
        Team awayTeam = Team.CANADA;
        Game game = new Game(homeTeam, awayTeam);
        when(mockGameRepository.find(homeTeam, awayTeam)).thenReturn(Optional.of(game));

        scoreBoardService.finishGame(homeTeam, awayTeam);

        verify(mockGameRepository, times(1)).remove(homeTeam, awayTeam);
    }

    @Test
    public void throwsException_when_gameNotFound_onUpdatingScore() {
        when(mockGameRepository.find(Team.MEXICO, Team.CANADA)).thenReturn(Optional.empty());  // Simulating game not found

        assertThatThrownBy(() -> scoreBoardService.updateScore(Team.MEXICO, Team.CANADA, 2, 3))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessageContaining("Game not found for teams: Mexico vs Canada.");
    }

    @Test
    public void updatesScore_when_gameExists_onUpdatingScore() {
        Team homeTeam = Team.MEXICO;
        Team awayTeam = Team.CANADA;
        Game game = new Game(homeTeam, awayTeam);
        when(mockGameRepository.find(homeTeam, awayTeam)).thenReturn(Optional.of(game));

        scoreBoardService.updateScore(homeTeam, awayTeam, 2, 3);

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(mockGameRepository).save(captor.capture());
        Game savedGame = captor.getValue();

        assertThat(savedGame.getHomeScore()).isEqualTo(2);
        assertThat(savedGame.getAwayScore()).isEqualTo(3);
    }

    @Test
    public void startsNewGame_when_teamsAreNotPlaying() {
        when(mockGameRepository.findAll()).thenReturn(List.of());

        scoreBoardService.startGame(Team.MEXICO, Team.CANADA);

        verify(mockGameRepository, times(1)).save(any(Game.class));
    }
}
