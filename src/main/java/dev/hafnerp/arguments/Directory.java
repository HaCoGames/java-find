package dev.hafnerp.arguments;

public class Directory extends Argument {

    private String content = null;

    private static final String caller = "--directory";

    public Directory(String content) {
        this.content = content;
    }

    public String  getCaller() {
        return caller;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return getCaller() + " " + getContent();
    }
}
