package p0nki.easycommand;

import org.junit.Test;

@SuppressWarnings("unused")
public class CommandDispatcherTests {

    // TODO optional arguments

    @Test
    public void test() {
        CommandDispatcher dispatcher = new CommandDispatcher();
        dispatcher.addPrimitives();
        dispatcher.createCog(new TestCog(), TestCog.class);
        dispatcher.run(Optional.of("source"), "acceptSource 5 greedy string here");
        dispatcher.run(Optional.empty(), "first1 second2 third name1 5 a b c d");
        dispatcher.run(Optional.empty(), "first2 second1 third 3 4 a b c d");
    }

}
