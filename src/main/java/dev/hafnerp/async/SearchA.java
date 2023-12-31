package dev.hafnerp.async;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is an algorithm that searches for a specific word given by the constructor and puts the path of the file where the word had been found into a list.
 */
public class SearchA implements Runnable {

    private final boolean first;

    private final String word;

    private final Path directory;

    private static final ListWrapper<Path> foundPaths = ListWrapper.getPathInstance();

    private ArrayList<Thread> allChildThreads;

    private ArrayList<SearchA> allChildRunnable;

    private static int instanceCounter = 0;

    public SearchA(boolean first, String word, Path directory) {
        instanceCounter++;
        this.first = first;
        this.word = word;
        this.directory = directory;
        allChildThreads = new ArrayList<>();
        allChildRunnable = new ArrayList<>();
    }

    public SearchA(String word, Path directory) {
        this.first = false;
        this.word = word;
        this.directory = directory;
        allChildThreads = new ArrayList<>();
        allChildRunnable = new ArrayList<>();
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

    public ArrayList<SearchA> getAllChildRunnable() {
        return new ArrayList<>(allChildRunnable);
    }

    public ArrayList<Thread> getAllChildThreads() {
        return new ArrayList<>(allChildThreads);
    }

    @Override
    public void run() {
        try {
            File file = new File(String.valueOf(directory));
            if (file.isDirectory()) {
                DirectoryStream<Path> direct = Files.newDirectoryStream(directory);
                for (Path path : direct) {
                    if (foundPaths.isFound()) break;
                    SearchA runnable;
                    Thread th;

                    runnable = new SearchA(first, word, path);
                    th = new Thread(runnable);

                    allChildThreads.add(th);
                    allChildRunnable.add(runnable);

                    th.start();
                    th.join();
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
            System.out.println(e.toString());
        }

    }

    public static void interruptThreads(ArrayList<Thread> threads, ArrayList<SearchA> runnableS) {
        threads.forEach(SearchA::interruptThread);
        runnableS.forEach(SearchA::interruptRunnableThreads);
    }

    /**
     * This function interrupts a Thread
     * @param aThread A thread that should be interrupted.
     */
    private static void interruptThread(Thread aThread) {
        System.out.println("SearchA - interrupt thread "+aThread.getName());
        aThread.interrupt();
    }

    private static void interruptRunnableThreads(SearchA searchA) {
        System.out.println("SearchA - interrupting all the threads in runnable "+ searchA);
        interruptThreads(searchA.getAllChildThreads(), searchA.getAllChildRunnable());
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (--instanceCounter <= 0 && foundPaths.getList().isEmpty()) {
            System.exit(2);
        }
    }
}
