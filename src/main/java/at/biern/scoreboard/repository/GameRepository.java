package at.biern.scoreboard.repository;

import at.biern.scoreboard.domain.Game;
import at.biern.scoreboard.domain.Team;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    void save(Game game);

    void remove(Team homeTeam, Team awayTeam);

    Optional<Game> find(Team homeTeam, Team awayTeam);

    List<Game> findAll();
}
