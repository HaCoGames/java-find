package dev.hafnerp.arguments;

/**
 * The Argument --word [String].
 * This argument is given to search every file for the string that was given with the argument.
 */
public class Word extends Argument {
    /**
     * The content that was given by the argument call.
     */
    private String content = null;

    private static final String caller = "--word";

    public static String getCaller() {
        return caller;
    }

    public Word(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return getCaller() + " " + getContent();
    }
}
