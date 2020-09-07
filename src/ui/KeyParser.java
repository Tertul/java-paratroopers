package ui;

import exceptions.GameOverException;
import exceptions.InvalidKeyCodeException;
import model.Game;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class KeyParser {

    private final int KB_SPACE = KeyEvent.VK_SPACE;
    private final int KB_LEFT = KeyEvent.VK_LEFT;
    private final int KB_RIGHT = KeyEvent.VK_RIGHT;
    private final int KB_Z = KeyEvent.VK_Z;
    private final int KB_S = KeyEvent.VK_S;
    private final int KB_H = KeyEvent.VK_H;
    private final int KB_R = KeyEvent.VK_R;
    private final List<Integer> validKeys = Arrays.asList(KB_SPACE, KB_RIGHT, KB_LEFT, KB_Z,KB_S,KB_H,KB_R);

    public boolean spaceDown;
    public boolean leftDown;
    public boolean rightDown;
    public boolean inputEnabled;

    private final Game game;

    public KeyParser(Game game){
        this.game = game;
        spaceDown = false;
        leftDown = false;
        rightDown = false;
        inputEnabled = true;

    }

    void keyPressed(int kc) throws GameOverException {
        if (!inputEnabled){
            if (kc == KB_R) {
                game.scorePanel.highscoreTable.isVisible = false;
                game.scorePanel.highscoreTable.setVisible(false);
                game.newGame();
            }
            else {
                throw new GameOverException();
            }
        }
        else {
            switch (kc){
                case KB_SPACE:
                    spaceDown = true;
                    break;
                case KB_RIGHT:
                    rightDown = true;
                    break;
                case KB_LEFT:
                    leftDown = true;
                    break;
                case KB_Z:
                    if (game.abilityUses > 0)
                        game.turret.ability();
                    break;
                case KB_S:
                    game.scorePanel.toggleShowStats();
                    break;
                case KB_H:
                    game.scorePanel.highscoreTable.toggleVisible();
                    break;
            }
        }
    }

    void keyReleased(int kc) throws InvalidKeyCodeException {
        if (!validKeys.contains(kc)) {
            throw new InvalidKeyCodeException();
        } else {
            switch (kc) {
                case KB_SPACE:
                    spaceDown = false;
                    break;
                case KB_RIGHT:
                    rightDown = false;
                    break;
                case KB_LEFT:
                    leftDown = false;
                    break;
            }
        }
    }
}

