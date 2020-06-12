package p0nki.easycommand;

import org.junit.Test;
import p0nki.easycommand.utils.Optional;

@SuppressWarnings("unused")
public class CommandDispatcherTests {

    // TODO optional arguments

    @Test
    public void test() {
        CommandDispatcher dispatcher = new CommandDispatcher();
        dispatcher.addPrimitives();
        dispatcher.createCog(new TestCog(), TestCog.class);
        dispatcher.run(null, "testArray 1 2 3 4 5");
        dispatcher.run(null, "testCharSequence str");
        dispatcher.run(null, "testGreedyString str a");
        dispatcher.run(null, "overlap str");
        dispatcher.run(null, "overlap 5");
        dispatcher.run("233", "returnSource");
        dispatcher.run(null, "returnSource");
        dispatcher.run(Optional.empty(), "returnSource");
        dispatcher.run(null, "echo @everyone");
    }

}
