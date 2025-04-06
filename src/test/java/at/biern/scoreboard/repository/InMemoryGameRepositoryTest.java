package at.biern.scoreboard.repository;

import at.biern.scoreboard.domain.Game;
import at.biern.scoreboard.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class InMemoryGameRepositoryTest {

    private InMemoryGameRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryGameRepository();
    }

    @Test
    public void findsGame_when_gameIsSaved() {
        Game game = new Game(Team.MEXICO, Team.CANADA);
        repository.save(game);

        assertThat(repository.find(Team.MEXICO, Team.CANADA))
                .isPresent()
                .contains(game);
    }

    @Test
    public void cannotFindGame_when_gameIsRemoved() {
        Game game = new Game(Team.MEXICO, Team.CANADA);
        repository.save(game);

        repository.remove(Team.MEXICO, Team.CANADA);

        assertThat(repository.find(Team.MEXICO, Team.CANADA))
                .isNotPresent();
    }

    @Test
    public void findsAllGames_when_multipleGamesAreSaved() {
        Game game1 = new Game(Team.MEXICO, Team.CANADA);
        Game game2 = new Game(Team.SPAIN, Team.BRAZIL);
        repository.save(game1);
        repository.save(game2);

        List<Game> allGames = repository.findAll();

        assertThat(allGames)
                .isNotNull()
                .hasSize(2)
                .contains(game1, game2);
    }

    @Test
    public void throwsException_when_gameNotFound() {
        assertThat(repository.find(Team.GERMANY, Team.FRANCE))
                .isNotPresent();
    }

    @Test
    public void updatesGame_when_gameIsSavedAgain() {
        Game game = new Game(Team.MEXICO, Team.CANADA);
        repository.save(game);

        game.updateScore(2, 3);
        repository.save(game);

        List<Game> games = repository.findAll();
        assertThat(games).hasSize(1);
        assertThat(games.get(0).getHomeScore()).isEqualTo(2);
        assertThat(games.get(0).getAwayScore()).isEqualTo(3);
    }
}
