package dev.hafnerp;

import dev.hafnerp.arguments.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

            DirectoryStream<Path> dirs = Files.newDirectoryStream(directory);
            List<Path> foundFiles = searchForFound(dirs, searchedWord, first);

            System.out.println(foundFiles);
        }
        catch (ParameterNotGiven e) {
            e.printStackTrace();
            //ToDo: Print the Help! (for all commands)
            System.exit(-1);
        }
        catch (IOException ignore) {}

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

    private static List<Path> searchForFound(DirectoryStream<Path> stream, String word, boolean first) throws IOException {
        List<Path> paths = new ArrayList<>();
        for (Path path : stream) {
            File file = new File(String.valueOf(path));
            if (file.isFile()) {
                Stream<String> stream2 = Files.lines(path);
                if (stream2.anyMatch(lines -> lines.contains(word))) {
                    System.out.println("Added Path: "+ path);
                    paths.add(path);
                }
            }
            else paths.addAll(searchForFound(Files.newDirectoryStream(path), word, first));
        }
        return paths;
    }
}