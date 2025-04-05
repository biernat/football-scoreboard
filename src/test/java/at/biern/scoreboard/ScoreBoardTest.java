package at.biern.scoreboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScoreBoardTest {

    @Test
    public void returnsEmptySummary_when_scoreboardIsEmpty() {
        ScoreBoard board = new ScoreBoard();
        List<Game> summary = board.getSummary();

        assertNotNull(summary);
        assertTrue(summary.isEmpty());
    }
}
