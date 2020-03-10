package input;

import java.util.Scanner;

public class Input {
    public static String getString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
