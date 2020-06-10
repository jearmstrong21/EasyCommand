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
        dispatcher.run(Optional.empty(), "testArray 1 2 3 4 5");
        dispatcher.run(Optional.empty(), "testCharSequence str");
        dispatcher.run(Optional.empty(), "testGreedyString str a");
        dispatcher.run(Optional.empty(), "overlap str");
        dispatcher.run(Optional.empty(), "overlap 5");
    }

}
