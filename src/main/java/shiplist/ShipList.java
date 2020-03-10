package shiplist;

import input.Input;
import javafx.util.Pair;
import ship.Ship;
import validation.Validation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class ShipList {
    public ArrayList<Ship> ships;

    public ShipList() {
        this.ships = new ArrayList<Ship>();
    }


    public int getHealth() {
        int health = 0;
        for (Ship ship : this.ships) {
            if (ship.getNoOfHitsNeeded() - ship.getNoOfHitsMade() > 0) {
                health += ship.getNoOfHitsNeeded() - ship.getNoOfHitsMade();
            }
        }
        return health;
    } // можно заменить на удаление кораблей

    public boolean isEmpty() {
        return this.ships.isEmpty();
    }

    @Override
    public String toString() {
        return "ShipList{" +
                "ships=" + ships +
                '}';
    }


}
