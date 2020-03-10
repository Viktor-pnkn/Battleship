package game;

import coordinateGenerator.CoordinateGenerator;
import fileio.FileIO;
import input.Input;
import javafx.util.Pair;
import ship.Ship;
import shiplist.ShipList;
import validation.Validation;

import java.io.*;
import java.util.*;

public class Game {
    private ShipList playerShips;
    private ShipList deathPlayerShips;
    private ShipList computerShips;
    private ShipList deathComputerShips;
    private Set<Pair<Integer, Integer>> compShots;

    public Game(int number, int maxPos) {
        this.playerShips = makePlayerShips(number, maxPos);
        this.deathPlayerShips = new ShipList();
        System.out.println("Loading computer settings...");
        this.computerShips = makeComputerShips(number, maxPos + 1);
        this.deathComputerShips = new ShipList();
        System.out.println("Computer settings generated!");
        this.compShots = new HashSet<>();
        System.out.println("Press any key to continue...");
        Input.getString();
    }

    public static void work(File file) throws IOException {
        System.out.println(
                "+==========================================================+" + "\n" +
                        "|                                                          |" + "\n" +
                        "|     Welcome to the Battleship Game - With a Twist!!!     |" + "\n" +
                        "|                                                          |" + "\n" +
                        "+==========================================================+");
        System.out.println("The game will use the grid size defined in the settings file.");
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String[] str = bf.readLine().split(" ");
        int size = Integer.parseInt(str[4]);
        str = bf.readLine().split(" ");
        boolean multiple = Boolean.parseBoolean(str[3]);
        str = bf.readLine().split(" ");
        boolean visible = Boolean.parseBoolean(str[3]);
        str = bf.readLine().split(" ");
        int number = Integer.parseInt(str[3]);

        System.out.println("Playing grid size set as (" + size * number + " X " + size * number + ")");
        System.out.println("Maximum number of ships allowed as " + number);
        System.out.println("Multiple hits allowed per ships set as " + multiple);
        System.out.println("Computer ships visible: " + visible);
        System.out.println();

        Game game = new Game(number, size * number - 1);

        while (!game.playerShips.isEmpty() && !game.computerShips.isEmpty()) {
            game.drawGrids(size * number);
            while (!game.computerShips.isEmpty()) {
                if (game.playerTurn(size * number - 1)) {
                    System.out.println("----------------------------------------------------");
                    game.drawGrids(size * number);
                } else {
                    System.out.println("----------------------------------------------------");
                    break;
                }
            }
            while (!game.playerShips.isEmpty()) {
                int turn = game.computerTurn(size, number);
                if (turn == 0) {
                    break;
                } else if (turn == 1) {
                    game.drawGrids(size * number);
                }
            }
            System.out.println("----------------------------------------------------");
        }
        if (game.playerShips.isEmpty()) {
            System.out.println("YOU LOSE!!!");
            FileIO.writeOutput("Computer wins, you suck");
        } else {
            System.out.println("CONGRATULATIONS! PLAYER WINS!");
            FileIO.writeOutput("Player wins, computer sucks");
        }
    }

    private ShipList makePlayerShips(int number, int maxPos) {
        ShipList shipList = new ShipList();
        for (int i = 0; i < number; i++) {
            System.out.println("Please enter the details for the " + (i + 1) + " ship:");
            int xPos = enterPosition(maxPos, 'x');
            int yPos = enterPosition(maxPos, 'y');
            int hits = (int) (Math.random() * 5) + 1;
            shipList.ships.add(new Ship(xPos, yPos, hits));
        }
        return shipList;
    }

    private int enterPosition(int maxPos, char c) {
        int pos = maxPos + 1;
        while (Validation.outSide(pos, maxPos)) {
            System.out.println("Ship " + c + " position (0 - " + maxPos + "):");
            String s = Input.getString();
            if (Validation.isInt(s)) {
                pos = Integer.parseInt(s);
                if (Validation.outSide(pos, maxPos)) {
                    System.err.println("Ship " + c + " position must be between 0 and " + maxPos);
                }
            } else {
                System.err.println("Ship " + c + " position must be between 0 and " + maxPos);
            }
        }
        return pos;
    }

    private ShipList makeComputerShips(int number, int maxPos) {
        ShipList shipList = new ShipList();
        for (int i = 0; i < number; i++) {
            Pair<Integer, Integer> pair = CoordinateGenerator.getField(maxPos);
            int hits = (int) (Math.random() * 5) + 1;
            shipList.ships.add(new Ship(pair.getKey(), pair.getValue(), hits));
        }
        return shipList;
    }

    private void drawGrids(int max) {
        System.out.println("Dispaying player Grid");
        drawGrid(this.playerShips, this.deathPlayerShips, max);
        System.out.println("Dispaying computer Grid");
        drawGrid(this.computerShips, this.deathComputerShips, max);
    }

    private void drawGrid(ShipList shipList, ShipList deathList, int max) {
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                Ship ship = findShip(deathList, i, j);
                if (ship != null) {
                    System.out.print("X");
                } else {
                    ship = findShip(shipList, i, j);
                    if (ship == null || ship.getNoOfHitsMade() == 0) {
                        System.out.print("~");
                    } else {
                        if (ship.getNoOfHitsNeeded() - ship.getNoOfHitsMade() > 0) {
                            System.out.print("D");
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    private Ship findShip(ShipList shipList, int i, int j) {
        Ship find = new Ship(i, j, 0);
        for (Ship ship : shipList.ships) {
            if (find.equals(ship)) {
                return ship;
            }
        }
        return null;
    }

    public boolean playerTurn(int maxPos) {
        System.out.println("Player to make a guess");
        int xPos = enterPosition(maxPos, 'x');
        int yPos = enterPosition(maxPos, 'y');
        Ship hit = new Ship(xPos, yPos, 0);
        boolean t = false;
        Iterator<Ship> iterator = computerShips.ships.iterator();
        while (iterator.hasNext()) {
            Ship ship = iterator.next();
            if (ship.equals(hit)) {
                t = true;
                if (ship.hitShip()) {
                    this.computerShips.ships.remove(ship);
                    this.deathComputerShips.ships.add(ship);
                }
                break;
            }
        }
        if (t) {
            System.out.println("PLAYER HITTTTS!!!!!");
            return true;
        }
        System.out.println("PLAYER MISSSSS!!!!!");
        return false;
    }

    /**
     * Метод возвращает 0, если комп промахнулся; 1, если ранил или убил.
     */
    private int computerTurn(int size, int number) {
        int max = size * number - 1;
        int xPos, yPos;
        while (true) { // проверка, чтобы комп не стрелял в одинаковые точки
            Pair<Integer, Integer> pair = CoordinateGenerator.getField(max);
            if (!compShots.contains(pair)) {
                compShots.add(pair);
                xPos = pair.getKey();
                yPos = pair.getValue();
                break;
            }
        }
        System.out.println("Computer x guess: " + xPos);
        System.out.println("Computer y guess: " + yPos);
        Ship hit = new Ship(xPos, yPos, 0);
        boolean t = false;
        Iterator<Ship> iterator = playerShips.ships.iterator();
        Ship ship = new Ship(0,0,0);
        while (iterator.hasNext()) {
            ship = iterator.next();
            if (ship.equals(hit)) {
                System.out.println("COMPUTER HITTTTS!!!!!");
                System.out.println("----------------------------------------------------");
                if (ship.hitShip()) {
                    this.playerShips.ships.remove(ship);
                    this.deathPlayerShips.ships.add(ship);
                    return 1;
                } else {
                    kill(ship, size, number);
                    return 2;
                }
            }
        }
        System.out.println("COMPUTER MISSSSS!!!!!");
        return 0;
    }

    private void kill(Ship ship, int size, int number) {
        int xPos = ship.getxPos();
        int yPos = ship.getyPos();
        drawGrids(size * number);
        while (playerShips.ships.contains(ship)) {
            System.out.println("Computer x guess: " + xPos);
            System.out.println("Computer y guess: " + yPos);
            if (ship.hitShip()) {
                this.playerShips.ships.remove(ship);
                this.deathPlayerShips.ships.add(ship);
                break;
            }
            System.out.println("COMPUTER HITTTTS!!!!!");
            System.out.println("----------------------------------------------------");
            drawGrids(size * number);
        }
        System.out.println("COMPUTER HITTTTS!!!!!");
        System.out.println("----------------------------------------------------");
        drawGrids(size * number);
    }
}
