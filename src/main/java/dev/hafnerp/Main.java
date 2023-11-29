package dev.hafnerp;

import dev.hafnerp.arguments.*;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
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
            }
            else {
                DirectoryStream<Path> files = Files.newDirectoryStream(directory);
                for (Path path : files) {
                    System.out.println(String.valueOf(path));
                    if (first) System.exit(0);
                }
            }


        }
        catch (ParameterNotGiven e) {
            System.out.println("**********************************************************************************************");
            System.out.println("* HELP                                                                                       *");
            System.out.println("* Usage: Main.java [--word \"SEARCHED_WORD\"] [--directory\"SEARCHED_DIRECTORY\"] [--first]      *");
            System.out.println("**********************************************************************************************");
            System.exit(-1);
        }
        catch (Exception e) {
            System.out.println("Out err - "+e.getMessage());
        }

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