package p0nki.easycommand;

import p0nki.easycommand.annotations.Argument;
import p0nki.easycommand.annotations.Command;
import p0nki.easycommand.annotations.CommandCog;
import p0nki.easycommand.arguments.Parsers;

import java.util.Arrays;

@CommandCog("test")
@SuppressWarnings("unused")
public class TestCog {

    @Command
    public void testArray(@Argument(name = "array") Integer[] array) {
        System.out.println(array.length + ": " + Arrays.toString(array));
    }

    @Command
    public void testCharSequence(@Argument(name = "str") CharSequence str) {
        System.out.println("CharSequence " + str);
    }

    @Command
    public void testGreedyString(@Argument(name = "str", modifiers = Parsers.GREEDY_STRING) String str) {
        System.out.println("Str " + str);
    }

    @Command(names = "overlap")
    public void testSameInt(@Argument(name = "in") int in) {
        System.out.println("ININT " + in);
    }

    @Command(names = "overlap")
    public void testSameString(@Argument(name = "in") String in) {
        System.out.println("INSTR " + in);
    }

    @Command(names = "returnSource")
    public void returnSource(@Argument(name = "source", parse = ReturnSourceParser.class) Object source) {
        System.out.println("returnSource " + source);
    }

}
