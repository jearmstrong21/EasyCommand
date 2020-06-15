package p0nki.easycommand;

import p0nki.easycommand.annotations.Argument;
import p0nki.easycommand.annotations.Command;
import p0nki.easycommand.annotations.CommandCog;
import p0nki.easycommand.annotations.Source;
import p0nki.easycommand.arguments.Parsers;

import java.util.Arrays;

@CommandCog(name = "test")
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

    @Command(names = "testGreedy")
    public void testGreedy() {
        System.out.println("testGreedy no args");
    }

    @Command(names = "testGreedy")
    public void testGreedy(@Argument(name = "str", modifiers = Parsers.GREEDY_STRING) String str) {
        System.out.println("testGreedy arg=" + str);
    }

    @Command(names = "echo")
    public void echo(@Source Object source, @Argument(name = "value", modifiers = Parsers.GREEDY_STRING) String value) {
        System.out.println(value);
    }

    @Command(names = "testSource")
    public void testSource(@Source int source) {
        System.out.println("testSource " + source);
    }

    @Command(names = "testRequirement", requirements = IntValueRequirement.class)
    public void testRequirement(@Source int source) {
        System.out.println("testRequirement " + source);
    }

}
