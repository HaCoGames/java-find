package dev.hafnerp;

import dev.hafnerp.async.HashSetWrapper;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * This is an algorithm that searches for a specific word given by the constructor and puts the path of the file where the word had been found into a list.
 */
public class SearchA implements Runnable {

    private final boolean first;

    private final String word;

    private final Path directory;

    private static final HashSetWrapper<Path> foundPaths = new HashSetWrapper<>();

    public SearchA(boolean first, String word, Path directory) {
        this.first = first;
        this.word = word;
        this.directory = directory;
    }

    public SearchA(String word, Path directory) {
        this.first = false;
        this.word = word;
        this.directory = directory;
    }

    public boolean isFirst() {
        return first;
    }

    public String getWord() {
        return word;
    }

    public Path getDirectory() {
        return directory;
    }

    public static List<Path> getFoundPaths() {
        return foundPaths.getSet();
    }

    @Override
    public void run() {
        try {
            File file = new File(String.valueOf(directory));
            if (file.isDirectory()) {
                DirectoryStream<Path> direct = Files.newDirectoryStream(directory);
                for (Path path : direct) {
                    Runnable r = new SearchA(first, word, path);
                    r.run();
                }
            }
            else if (file.isFile()) {
                Scanner scanner = new Scanner(file);
                boolean found = false;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    found = (line.contains(word));
                    if (found) break;
                }
                if (found) {
                    foundPaths.add(directory);
                    if (first) System.exit(0);
                }
                scanner.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
