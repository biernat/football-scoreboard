package at.biern.scoreboard.service;

import at.biern.scoreboard.domain.Game;
import at.biern.scoreboard.domain.Team;

import java.util.List;

public interface ScoreBoard {
    void startGame(Team homeTeam, Team awayTeam);

    void finishGame(Team homeTeam, Team awayTeam);

    void updateScore(Team homeTeam, Team awayTeam, int homeScore, int awayScore);

    List<Game> getSummaryByTotalScore();
}
