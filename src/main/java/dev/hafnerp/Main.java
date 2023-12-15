package dev.hafnerp;

import dev.hafnerp.arguments.*;
import dev.hafnerp.async.ListWrapper;
import dev.hafnerp.async.SearchA;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/*
 * @startuml doc-files/Main.png
 * class Main {
 *      void main();
 * }
 *
 * class SearchA {
 *      boolean first
 *      String word
 *      Path directory
 *      void run();
 *      List getFoundPaths();
 *      ArrayList getAllChildRunnable();
 *      ArrayList getAllChildThreads();
 * }
 *
 * class ListWrapper {
 *      void add();
 *      List<?> getList()
 *      String toString();
 * }
 *
 * Main "1" -- "1" SearchA : > runs new Thread
 * SearchA "1" -- "*" SearchA : > is parent thread of new thread
 * ListWrapper "1" -- "*" SearchA : < adds the found paths
 * ListWrapper "1" -- "*" SearchA : < check if a path was found
 * ListWrapper "1" -- "1" Main : < reads found paths
 * @enduml
 */

/**
 *  Hello!
 *  This is a project for an implementation of the shell command find written in Java with the extra functionality to
 *  search for a specific word; You will get the paths to every file with the given word.
 *  <p>DATA:<img src="doc-files/Main.png"></p>
 */

public class Main {

    public static void main(String[] args) {
        int exitCode = 0;
        try {
            List<Argument> arguments = getArgumentListed(args);
            System.out.println(arguments+":");

            Path directory = Paths.get(".");
            String searchedWord = null;
            boolean first = false;

            for (Argument argument : arguments) {
                if (argument.getClass() == First.class) first = true;
                else if (argument.getClass() == Word.class) searchedWord = ((Word) argument).getContent();
                else if (argument.getClass() == Directory.class) directory = Paths.get(((Directory) argument).getContent());
            }

            if (searchedWord != null) {
                Thread thread = new Thread(new SearchA(first, searchedWord, directory));
                thread.start();
                thread.join();
                if (ListWrapper.getPathInstance().getList().isEmpty()) exitCode = 2;
            }
            else {
                DirectoryStream<Path> files = Files.newDirectoryStream(directory);
                for (Path path : files) {
                    System.out.println(String.valueOf(path));
                    if (first) System.exit(0);
                }
            }
            System.out.println(ListWrapper.getPathInstance());

        }
        catch (ParameterNotGiven e) {
            System.out.println("**********************************************************************************************");
            System.out.println("* HELP                                                                                       *");
            System.out.println("* Usage: Main.java [--word \"SEARCHED_WORD\"] [--directory\"SEARCHED_DIRECTORY\"] [--first]      *");
            System.out.println("**********************************************************************************************");
            exitCode = -1;
        }
        catch (Exception e) {
            System.out.println("Out err - "+e.getMessage());
            exitCode = 1;
        }
        System.exit(exitCode);
    }

    private static List<Argument> getArgumentListed(String[] args) throws ParameterNotGiven {
        List<Argument> arguments = new ArrayList<Argument>();

        for (int index = 0; index < args.length; index++) {
            switch (args[index]) {
                case "--word":
                    if (index+1 >= args.length) throw new ParameterNotGiven("The argument --word must have a word given!");
                    if (!args[index+1].startsWith("--")) arguments.add(new Word(args[++index]));
                    else throw new ParameterNotGiven("The argument --word must have a word given!");
                    break;
                case "--directory":
                    if (index+1 >= args.length) throw new ParameterNotGiven("The argument --directory must have a path given!");
                    if (!args[index+1].startsWith("--")) arguments.add(new Directory(args[++index]));
                    else throw new ParameterNotGiven("The argument --directory must have a path given!");
                    break;
                case "--first":
                    arguments.add(new First());
                    break;
                default:
                    break;
            }
        }

        return arguments;
    }
}