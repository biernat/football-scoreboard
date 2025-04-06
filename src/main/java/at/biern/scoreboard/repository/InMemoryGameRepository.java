package at.biern.scoreboard.repository;

import at.biern.scoreboard.domain.Game;
import at.biern.scoreboard.domain.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryGameRepository implements GameRepository {
    private final Map<String, Game> liveGames = new ConcurrentHashMap<>();

    @Override
    public void save(Game game) {
        liveGames.put(getKey(game), game);
    }

    @Override
    public void remove(Team homeTeam, Team awayTeam) {
        liveGames.remove(getKey(homeTeam, awayTeam));
    }

    @Override
    public Optional<Game> find(Team homeTeam, Team awayTeam) {
        return Optional.ofNullable(liveGames.get(getKey(homeTeam, awayTeam)));
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(liveGames.values());
    }

    private String getKey(Game game) {
        return getKey(game.getHomeTeam(), game.getAwayTeam());
    }

    private String getKey(Team homeTeam, Team awayTeam) {
        return homeTeam + "-vs-" + awayTeam;
    }
}
