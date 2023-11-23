package dev.hafnerp.arguments;

public class First extends Argument {

    private static final String caller = "--first";

    public static String getCaller() {
        return caller;
    }

    @Override
    public String toString() {
        return getCaller();
    }
}
