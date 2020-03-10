package coordinateGenerator;

import javafx.util.Pair;

public class CoordinateGenerator {

    public static Pair<Integer, Integer> getField(int max) {
        Integer xPos = (int) (Math.random() * (max + 1));
        Integer yPos = (int) (Math.random() * (max + 1));
        return new Pair<>(xPos, yPos);
    }
}
