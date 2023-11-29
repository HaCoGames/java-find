package dev.hafnerp;

import dev.hafnerp.async.ListWrapper;

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

    private static int instanceCounter = 0;

    private static final ListWrapper<Path> foundPaths = new ListWrapper<>();

    public SearchA(boolean first, String word, Path directory) {
        instanceCounter++;
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
        return foundPaths.getList();
    }

    public static int getInstanceCounter() {
        return instanceCounter;
    }

    @Override
    public void run() {
        try {
            File file = new File(String.valueOf(directory));
            if (file.isDirectory()) {
                DirectoryStream<Path> direct = Files.newDirectoryStream(directory);
                for (Path path : direct) {
                    Thread th = new Thread(new SearchA(first, word, path));
                    th.start();
                    th.join();
                    if (foundPaths.isFound()) break;
                    System.gc();
                }
            }
            else if (file.isFile()) {
                Scanner scanner = new Scanner(file);
                boolean found = false;
                while (scanner.hasNextLine() && !found) {
                    String line = scanner.nextLine();
                    found = (line.contains(word));
                }
                if (found) {
                    foundPaths.add(directory);
                    if (first) foundPaths.setFound(true);
                }
                scanner.close();
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (--instanceCounter <= 0 && foundPaths.getList().isEmpty()) {
            System.exit(2);
        }
    }
}
