package program;

import game.Game;

import java.io.File;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        Game.work(new File("gamesettings.txt"));
    }
}
