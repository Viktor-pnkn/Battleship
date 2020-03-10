package ship;

import java.util.Objects;

public class Ship {
    private int xPos;
    private int yPos;
    private int noOfHitsMade = 0;
    private int noOfHitsNeeded;

    public Ship(int xPos, int yPos, int noOfHitsNeeded) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.noOfHitsNeeded = noOfHitsNeeded;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public boolean hitShip() {
        this.noOfHitsMade++;
        return noOfHitsNeeded == noOfHitsMade;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "xPos=" + xPos +
                ", yPos=" + yPos +
                '}';
    }

    public int getNoOfHitsMade() {
        return noOfHitsMade;
    }

    public int getNoOfHitsNeeded() {
        return noOfHitsNeeded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return xPos == ship.xPos &&
                yPos == ship.yPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos);
    }
}
