package at.biern.scoreboard.service;

import at.biern.scoreboard.domain.Game;
import at.biern.scoreboard.domain.Team;
import at.biern.scoreboard.exception.GameNotFoundException;
import at.biern.scoreboard.exception.TeamAlreadyPlayingException;
import at.biern.scoreboard.repository.GameRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoardService implements ScoreBoard {

    private final GameRepository repository;

    public ScoreBoardService(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Game> getSummaryByTotalScore() {
        Comparator<Game> byTotalScoreAndCreationTime = Comparator
                .comparingInt((Game game) -> game.getHomeScore() + game.getAwayScore())
                .reversed()
                .thenComparing(Game::getCreatedAt, Comparator.reverseOrder());

        return repository.findAll().stream()
                .sorted(byTotalScoreAndCreationTime)
                .collect(Collectors.toList());
    }

    @Override
    public void startGame(Team homeTeam, Team awayTeam) {
        if (isTeamInGameAlready(homeTeam) || isTeamInGameAlready(awayTeam)) {
            throw new TeamAlreadyPlayingException(homeTeam, awayTeam);
        }
        repository.save(new Game(homeTeam, awayTeam));
    }

    @Override
    public void finishGame(Team homeTeam, Team awayTeam) {
        if (repository.find(homeTeam, awayTeam).isEmpty()) {
            throw new GameNotFoundException(homeTeam, awayTeam);
        }
        repository.remove(homeTeam, awayTeam);
    }

    @Override
    public void updateScore(Team homeTeam, Team awayTeam, int homeScore, int awayScore) {
        Game game = repository.find(homeTeam, awayTeam)
                .orElseThrow(() -> new GameNotFoundException(homeTeam, awayTeam));
        game.updateScore(homeScore, awayScore);
    }

    private boolean isTeamInGameAlready(Team team) {
        return repository.findAll().stream().anyMatch(game -> game.isTeamInGame(team));
    }
}
