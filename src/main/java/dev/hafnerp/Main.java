package dev.hafnerp;

import dev.hafnerp.arguments.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            List<Argument> arguments = getArgumentListed(args);
            System.out.println(arguments);

            Path directory = Paths.get(".");
            for (Argument argument : arguments) if (argument.getClass() == Directory.class) directory = Paths.get(((Directory) argument).getContent());

        }
        catch (ParameterNotGiven parameterNotGiven) {
            parameterNotGiven.getCause();
            //ToDo: Print the Help! (for all commands)
            System.exit(-1);
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