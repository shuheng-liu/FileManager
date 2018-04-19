package CommandLine;

public class ArgumentNumberException extends Exception {
    public ArgumentNumberException(int expected, int got) {
        super("Expected args count = " + expected + ", got " + got);
    }
}
