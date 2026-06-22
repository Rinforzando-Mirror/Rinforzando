package adris.rinforzando.commandsystem.args;

import adris.rinforzando.commandsystem.exception.BadCommandSyntaxException;
import adris.rinforzando.commandsystem.exception.CommandException;
import adris.rinforzando.commandsystem.StringReader;

import java.util.stream.Stream;

public class IntArg extends Arg<Integer> {

    public IntArg(String name) {
        super(name);
    }

    public IntArg(String name, Integer defaultValue) {
        super(name, defaultValue);
    }

    public IntArg(String name, Integer defaultValue, boolean showDefault) {
        super(name, defaultValue, showDefault);
    }

    public static Integer parse(StringReader parser) throws CommandException {
        String value = parser.next();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BadCommandSyntaxException("Failed to parse '"+ value + "' into an Integer");
        }
    }

    @Override
    public Stream<String> getSuggestions(StringReader reader) {
        return Stream.empty();
    }

    @Override
    protected StringParser<Integer> getParser() {
        return IntArg::parse;
    }

    @Override
    public String getTypeName() {
        return "Integer";
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

}
