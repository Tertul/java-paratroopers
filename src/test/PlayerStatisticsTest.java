package test;

import model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.PlayerStatistics;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerStatisticsTest {

    private PlayerStatistics ps;

    @BeforeEach
    void setUp(){
        ps = new PlayerStatistics(new Game());
        ps.highscores = new ArrayList<>();
        createHighscores();
    }



    @Test
    void testCheckScore() {
        assertEquals(ps.highscores.size(), 10);
        assertEquals(ps.highscores.get(4).score, 40);
        ps.score = 55;
        ps.checkScore();
        assertEquals(ps.highscores.size(), 10);
    }
    
    private void createHighscores() {
        for (int i = 0 ; i < 10 ; i++){
            ps.highscores.add(ps.new Entry("BOT","1/1/11",0,10*i));
            ps.sortScores();
        }
    }
}
